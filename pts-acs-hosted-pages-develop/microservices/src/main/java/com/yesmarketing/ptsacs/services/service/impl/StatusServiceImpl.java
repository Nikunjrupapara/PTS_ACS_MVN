package com.yesmarketing.ptsacs.services.service.impl;

import com.google.common.base.Throwables;
import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmarketing.acsapi.auth.service.CredentialManagementService;
import com.yesmarketing.acsapi.exception.SagasUnauthorizedException;
import com.yesmarketing.ptsacs.common.exception.HealthCheckException;
import com.yesmarketing.ptsacs.services.service.ProfileService;
import com.yesmarketing.ptsacs.services.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class StatusServiceImpl implements StatusService {

    ProfileService profileService;

    CredentialManagementService credentialManagementService;

    @Autowired
    public StatusServiceImpl(ProfileService profileService,CredentialManagementService credentialManagementService){
        this.profileService=profileService;
        this.credentialManagementService = credentialManagementService;
    }

    public Health getStatus(String company, String lookupValue){

        try {

            profileService.getProfileByCustomerIdHash(company, lookupValue);
            return new Health.Builder().up().withDetail("message",String.format("Company '%s' is found. User is found.",company)).build();

        }catch(ObjectNotFoundException nfe) {

            if(nfe.getMessageCode().equals("resourceNotFound.message")){
                throw new ObjectNotFoundException(String.format("No credential found for company '%s'.",company),"company is missing");
            }
            if(nfe.getMessageCode().equals("profileNotFoundByEncId.message")){
                return new Health.Builder().up().withDetail("message",String.format("Company '%s' is found. User is not found.",company)).build();
            }
            throw new HealthCheckException("Please check credential for the company, it might be expired.");

        }catch(SagasUnauthorizedException ex) {

            throw new HealthCheckException("Please check credential for the company, it might be expired.", ex);

        }catch(Exception e){

            Throwable cause = Throwables.getRootCause(e);
            LOG.error("Error occurred",cause);
            if(cause instanceof HttpClientErrorException.BadRequest){
                throw new HealthCheckException("Please check credential for the company, it might be expired.",cause);
            }
            throw new HealthCheckException("Unexpected error has occurred. Please contact support for assistance.",cause);

        }

    }

    public List<String> getCompanies(){
       return credentialManagementService.getCredentials().stream().map(e->e.getCompany()).sorted().collect(Collectors.toList());
    }

}
