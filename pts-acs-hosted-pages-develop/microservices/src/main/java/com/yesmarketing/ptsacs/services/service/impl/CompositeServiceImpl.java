package com.yesmarketing.ptsacs.services.service.impl;

import static com.yesmarketing.acsapi.model.AdobeCampaignConstants.PKEY;

import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmarketing.acsapi.admin.model.CustomResourceRecord;
import com.yesmarketing.acsapi.exception.SagasRuntimeException;
import com.yesmarketing.acsapi.model.EmailResponse;
import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ResponseModel;
import com.yesmarketing.ptsacs.common.exception.BadRequestException;
import com.yesmarketing.ptsacs.services.dto.CompositeServiceDto;
import com.yesmarketing.ptsacs.services.dto.CompositeServiceResponseDto;
import com.yesmarketing.ptsacs.services.dto.CustomResourceRecordDto;
import com.yesmarketing.ptsacs.services.dto.CustomResourcesDto;
import com.yesmarketing.ptsacs.services.dto.EmailDto;
import com.yesmarketing.ptsacs.services.dto.GetProfileDto;
import com.yesmarketing.ptsacs.services.dto.GetProfileResponseDto;
import com.yesmarketing.ptsacs.services.dto.GetServiceSubscriptionDto;
import com.yesmarketing.ptsacs.services.dto.UpdateResourceDto;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.model.GetProfileResponse;
import com.yesmarketing.ptsacs.services.service.CompositeService;
import com.yesmarketing.ptsacs.services.service.CustomResourceService;
import com.yesmarketing.ptsacs.services.service.CustomerIdService;
import com.yesmarketing.ptsacs.services.service.EmailService;
import com.yesmarketing.ptsacs.services.service.ProfileService;
import com.yesmarketing.ptsacs.services.util.ServicesModelMapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompositeServiceImpl implements CompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(CompositeServiceImpl.class);

    private static final String CUSTOMER_ID_HASH = "customerIdHash";

    private final ProfileService profileService;
    private final EmailService emailService;
    private final CustomResourceService resourcesService;
    private final CustomerIdService customerIdService;
    private final ServicesModelMapperHelper servicesModelMapperHelper;

    @Autowired
    public CompositeServiceImpl(
            ProfileService profileService,
            EmailService emailService,
            ServicesModelMapperHelper servicesModelMapperHelper,
            CustomResourceService resourcesService,
            CustomerIdService customerIdService){
        this.profileService = profileService;
        this.emailService = emailService;
        this.resourcesService = resourcesService;
        this.customerIdService = customerIdService;
        this.servicesModelMapperHelper = servicesModelMapperHelper;
    }

    @Override
    public CompositeServiceResponseDto updateOrCreate(CompositeServiceDto compositeServiceDto, FormConfig formConfig) {

        boolean profileExists = true;
        boolean profileHasHashId = true;

        CompositeServiceResponseDto response = new CompositeServiceResponseDto();
        GetProfileResponseDto getProfileResponseDto;
        String customerIdHash = "";
        ResponseModel<ProfileModel> responseModel = null;

        // PROFILE is mandatory for both profile and subscription updates
        if (compositeServiceDto.getProfile() != null) {
            ProfileModel profileModelOut;
            ProfileModel profileModel = servicesModelMapperHelper.fromDto(compositeServiceDto.getProfile(), ProfileModel.class);
            customerIdHash = customerIdService.getCustomerIdHash(formConfig.getCompany(), profileModel);

            try {
                responseModel = lookupProfile(formConfig, customerIdHash);
            } catch (ObjectNotFoundException one) { profileExists = false; }

            if (!profileExists) {
                profileExists = true;
                try{
                    responseModel = lookupProfileByCustomerId(profileModel,formConfig);
                    profileHasHashId = false;
                } catch (ObjectNotFoundException one) { profileExists = false;}
            }

            try {
                if (profileExists) {
                    if (profileHasHashId) {
                        profileModelOut = profileService.updateProfileAndGet(formConfig.getCompany(), customerIdHash, profileModel);
                    } else {
                        ProfileModel responseProfileModel = responseModel.getContent().get(0);
                        String pKey = responseProfileModel.getPKey();

                        profileModelOut = profileService.updateProfileAndGetByPKey(formConfig.getCompany(), pKey, profileModel);
                    }
                } else {
                    profileModelOut = profileService.createProfile(formConfig.getCompany(), profileModel);
                }
            } catch (Exception error) {
                response.setProfileSuccess(false);
                response.setErrorMessage(error.getMessage());
                return response;
            }

            response.setProfileSuccess(true);

            GetProfileDto getProfileDto = servicesModelMapperHelper.toDto(profileModelOut, GetProfileDto.class);
            getProfileResponseDto = new GetProfileResponseDto();
            getProfileResponseDto.setProfile(getProfileDto);
            response.setProfile(getProfileResponseDto);

            // SERVICES
            try {
                processSubscriptions(compositeServiceDto, customerIdHash, formConfig, response);
            } catch (Exception error) {
                response.setServicesSuccess(false);
                response.setErrorMessage(error.getMessage());
                return response;
            }
        }

        /**
         * Custom Resources
         * will only add new records,
         * first iteration: it will error out if the record already exists
         */
        if (compositeServiceDto.getCustomResources() != null) {
            processCustomResources(compositeServiceDto,formConfig,response);
        }


        // EMAILS
        try {
            sendEmails(compositeServiceDto, customerIdHash, formConfig, response);
        } catch (Exception error) {
            response.setEmailsSuccess(false);
            response.setErrorMessage(error.getMessage());
            return response;
        }

        return response;
    }

    @Override
    public CompositeServiceResponseDto updateByHash(CompositeServiceDto compositeServiceDto, String customerIdHash, FormConfig formConfig) {
        CompositeServiceResponseDto response = new CompositeServiceResponseDto();
        GetProfileResponseDto getProfileResponseDto;

        if (compositeServiceDto.getProfile() != null) {
            ProfileModel profileModel = servicesModelMapperHelper.fromDto(compositeServiceDto.getProfile(), ProfileModel.class);
            ProfileModel profileModelOut;

            try {
                profileModelOut = profileService.updateProfileAndGet(formConfig.getCompany(), customerIdHash, profileModel);
            } catch (Exception error) {
                response.setProfileSuccess(false);
                response.setErrorMessage(error.getMessage());
                return response;
            }

            response.setProfileSuccess(true);

            GetProfileDto getProfileDto = servicesModelMapperHelper.toDto(profileModelOut, GetProfileDto.class);
            getProfileResponseDto = new GetProfileResponseDto();
            getProfileResponseDto.setProfile(getProfileDto);
            response.setProfile(getProfileResponseDto);
        } else {
            try {
                ResponseModel<ProfileModel> responseModel = lookupProfile(formConfig, customerIdHash);
                ProfileModel profileModel = responseModel.getContent().get(0);
                GetProfileDto getProfileDto = servicesModelMapperHelper.toDto(profileModel, GetProfileDto.class);
                getProfileResponseDto = new GetProfileResponseDto();
                getProfileResponseDto.setProfile(getProfileDto);
                response.setProfile(getProfileResponseDto);
            } catch (ObjectNotFoundException one) {
                response.setProfileSuccess(false);
                response.setErrorMessage(one.getMessage());
                return response;
            }

        }

        // SERVICES
        try {
            processSubscriptions(compositeServiceDto, customerIdHash, formConfig, response);
        } catch (Exception error) {
            response.setServicesSuccess(false);
            response.setErrorMessage(error.getMessage());
            return response;
        }

        /**
         * Custom Resources
         * will only add new records,
         * first iteration: it will error out if the record already exists
         */
        if (compositeServiceDto.getCustomResources() != null) {
            processCustomResources(compositeServiceDto,formConfig,response);
        }


        // EMAILS
        try {
            sendEmails(compositeServiceDto, customerIdHash, formConfig, response);
        } catch (Exception error) {
            response.setEmailsSuccess(false);
            response.setErrorMessage(error.getMessage());
            return response;
        }

        return response;
    }

    @Override
    public void validateUpdateOrCreate(CompositeServiceDto compositeServiceDto, FormConfig formConfig) {

        if (compositeServiceDto.getProfile() != null) {
            ProfileModel profileModel = servicesModelMapperHelper.fromDto(compositeServiceDto.getProfile(), ProfileModel.class);
            profileService.validateCreateProfile(formConfig.getCompany(), profileModel);
        }else{
            throw new BadRequestException("Profile is missing.");
        }

        if (compositeServiceDto.getServices() != null) {
            profileService.validateUpdateSubscription(formConfig.getCompany(), compositeServiceDto.getServices());
        }

        if (compositeServiceDto.getEmails() != null) {
            for (EmailDto emailDto : compositeServiceDto.getEmails()) {
                if (emailDto != null) {
                    emailService.validateSendEmail(formConfig.getCompany(), emailDto);
                }
            }
        }
    }

    @Override
    public void validateUpdateByHash(CompositeServiceDto compositeServiceDto, String customerIdHash, FormConfig formConfig) {

        if (compositeServiceDto.getProfile() != null) {
            ProfileModel profileModel = servicesModelMapperHelper.fromDto(compositeServiceDto.getProfile(), ProfileModel.class);
            profileService.validateUpdateProfile(formConfig.getCompany(), customerIdHash, compositeServiceDto.getProfile());
        }

        if (compositeServiceDto.getServices() != null) {
            profileService.validateUpdateSubscription(formConfig.getCompany(), compositeServiceDto.getServices());
        }

        if (compositeServiceDto.getEmails() != null) {
            for (EmailDto emailDto : compositeServiceDto.getEmails()) {
                if (emailDto != null) {
                    emailService.validateSendEmail(formConfig.getCompany(), emailDto);
                }
            }
        }
    }

    private ResponseModel<ProfileModel> lookupProfile(FormConfig formConfig, String customerIdHash){
        ResponseModel<ProfileModel> responseModel = profileService.getProfileByCustomerIdHash(formConfig.getCompany(), customerIdHash);
        ProfileModel profileModelOut = responseModel.getContent().get(0);
        return responseModel;
    }

    private ResponseModel<ProfileModel> lookupProfileByCustomerId(ProfileModel profileModel, FormConfig formConfig){
        ResponseModel<ProfileModel> responseModel = profileService.getProfileByCustomerId(profileModel, formConfig.getCompany());
        ProfileModel profileModelOut = responseModel.getContent().get(0);
        return responseModel;
    }

    private void processSubscriptions(
            CompositeServiceDto compositeServiceDto, String customerIdHash,
            FormConfig formConfig, CompositeServiceResponseDto response
    ) {
        GetProfileResponse getProfileResponse;
        if (compositeServiceDto.getServices() != null) {
            getProfileResponse = profileService.updateSubscription(formConfig.getCompany(), customerIdHash, compositeServiceDto.getServices());
            response.setServicesSuccess(true);
        } else {
            getProfileResponse = profileService.getUserSubscriptions(formConfig.getCompany(), customerIdHash);
        }

        List<GetServiceSubscriptionDto> subs = getProfileResponse.getCurrentServices()
                .stream()
                .map(sub -> {
                    GetServiceSubscriptionDto getServiceSubscriptionDto =  new GetServiceSubscriptionDto();
                    LocalDateTime created = LocalDateTime.ofInstant(Instant.ofEpochMilli(sub.getCreated().getMillis()), ZoneId.of(sub.getCreated().getZone().getID()));
                    getServiceSubscriptionDto.setCreated(created);
                    getServiceSubscriptionDto.setName(sub.getService().getName());
                    getServiceSubscriptionDto.setLabel(sub.getService().getLabel());
                    return getServiceSubscriptionDto;
                }).collect(Collectors.toList());

        response.getProfile().setCurrentServices(subs);
    }

    private void sendEmails(
            CompositeServiceDto compositeServiceDto,
            String customerIdHash, FormConfig formConfig, CompositeServiceResponseDto response
    ){
        List<EmailResponse> ers = new ArrayList<>();
        if (compositeServiceDto.getEmails() != null) {
            for (EmailDto emailDto : compositeServiceDto.getEmails()) {
                if (emailDto != null) {
                    if (emailDto.getCtx().containsKey(CUSTOMER_ID_HASH) && !StringUtils.isEmpty(customerIdHash)) {
                        emailDto.getCtx().put(CUSTOMER_ID_HASH, customerIdHash);
                    }

                    if (!response.getProfile().getProfile().getEmail().equals(emailDto.getEmail())) {
                        throw new BadRequestException("The email address bound to profile/customerIdHash does not match the email 'TO' provided.");
                    }

                    ers.add(emailService.sendEmail(formConfig.getCompany(), emailDto));
                }
            }

            response.setEmails(ers);
            response.setEmailsSuccess(true);
        }
    }


    private void processCustomResources(CompositeServiceDto compositeServiceDto,FormConfig formConfig,CompositeServiceResponseDto response){
        response.setCustomResourcesSuccess(true);
        String company = formConfig.getCompany();
        response.setCustomResources(new LinkedList<>());

        for(CustomResourcesDto resource: compositeServiceDto.getCustomResources()){

            CustomResourcesDto customResourcesDtoResp = new CustomResourcesDto();
            customResourcesDtoResp.setName(resource.getName());
            customResourcesDtoResp.setRecords(new LinkedList<>());

            resource.getRecords().stream().forEach(r ->{

                CustomResourceRecord customResourceRecord = servicesModelMapperHelper.fromDto(r,CustomResourceRecord.class);
                CustomResourceRecord newRecord = null;
                if (isCustomResourceUpdate(customResourceRecord)) {
                    newRecord = updateCustomResource(company, resource.getName(), customResourceRecord, response);
                } else {
                    newRecord = submitCustomResource(company, resource.getName(), customResourceRecord, response);
                }
                if(newRecord!=null){
                    CustomResourceRecordDto record = servicesModelMapperHelper.toDto(newRecord,CustomResourceRecordDto.class);
                    customResourcesDtoResp.getRecords().add(record);

                }else{
                    response.getCustomResources().add(customResourcesDtoResp);
                    response.setErrorMessage(String.format("Failed to insert/update %s for %s",customResourceRecord.toString(),resource.getName()));
                    return;
                }

            });
            response.getCustomResources().add(customResourcesDtoResp);

        }


    }

    private CustomResourceRecord updateCustomResource(String company, String name, CustomResourceRecord customResourceRecord, CompositeServiceResponseDto response) {
        String pKey = (String) customResourceRecord.getValue(PKEY);
        customResourceRecord.getValues().remove(PKEY);
        UpdateResourceDto dto = resourcesService.updateRecord(company, name, pKey, customResourceRecord);
        return resourcesService.getRecord(company, name, dto.getPKey());
    }

    private CustomResourceRecord submitCustomResource(String company, String resourceName,CustomResourceRecord resource, CompositeServiceResponseDto response){
        try{
            return resourcesService.createRecord(company,resourceName,resource);
        }catch(SagasRuntimeException e){
            if(e.getMessage().toLowerCase().contains("violates unique")){
                // should we update record here?
                // find and update

                return resource;
            }
        }catch(Exception e){

        }
        response.setCustomResourcesSuccess(false);
        return null;
    }

    private boolean isCustomResourceUpdate(CustomResourceRecord customResourceRecord) {
        return customResourceRecord.hasValue(PKEY);
    }


}
