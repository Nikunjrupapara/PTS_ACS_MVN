package com.yesmarketing.ptsacs.services.service.impl;

import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.ptsacs.common.exception.UnknownHashingFunctionException;
import com.yesmarketing.ptsacs.admin.service.CustomerIdDefinitionService;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchDto;
import com.yesmarketing.ptsacs.services.exception.CustomerIdDefinitionMismatchException;
import com.yesmarketing.ptsacs.services.exception.MissingCustomerIdFieldValueException;
import com.yesmarketing.ptsacs.services.model.CustomerIdDefinition;
import com.yesmarketing.ptsacs.services.model.CustomerIdField;
import com.yesmarketing.ptsacs.services.service.CustomerIdService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerIdServiceImpl implements CustomerIdService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final CustomerIdDefinitionService customerIdDefinitionService;

    @Autowired
    public CustomerIdServiceImpl(CustomerIdDefinitionService customerIdDefinitionService){
        this.customerIdDefinitionService = customerIdDefinitionService;
    }

    public String getCustomerId(String company, ProfileModel profileModel){
        CustomerIdDefinition customerIdDefinition = customerIdDefinitionService.get(company);

        List<CustomerIdField> customerIdFields = customerIdDefinition.getFields();
        Map<String, Object> profileFields = profileModel.getAttributes();

        String key = customerIdFields.stream()
                .map(field -> {
                    String fieldName = field.getName();
                    Object fieldValue = profileFields.getOrDefault(fieldName, null);
                    // TODO: install validation at the request level for null values on CreateProfileDto?
                    if (!hasText(fieldValue) && field.isRequired()) {
                        throw new MissingCustomerIdFieldValueException(company, field.getName());
                    }
                    return fieldValue == null ? "" : fieldValue.toString();
                })
                .collect(Collectors.joining(customerIdDefinition.getDelimiter()));

        return key;
    }

    private boolean hasText(Object fieldValue){
        if (fieldValue == null) { return false; }
        if (fieldValue.toString().length() < 1) { return false; }
        return true;
    }

    public String getCustomerId(String company, ProfileSearchDto profileSearchDto){
        // get CustomerIdDefinition from DB based on company
        // get necessary fields from profileModel
        // would throw run time exception

        CustomerIdDefinition customerIdDefinition = customerIdDefinitionService.get(company);

        List<CustomerIdField> customerIdFields = customerIdDefinition.getFields();
        Map<String, String> profileSearchFields = profileSearchDto.getSearchFields();

        if (profileSearchFields.size() != customerIdFields.size()) {
            String msg = String.format("company: %s, customerIdFields.size=%d, profileSearchFields.size=%d",
                    company, customerIdFields.size(), profileSearchFields.size());
            throw new CustomerIdDefinitionMismatchException(msg);
        }

        customerIdFields.forEach(field -> {
            if (!profileSearchFields.containsKey(field.getName())) {
                String msg = String.format("company: %s, customerId definition expects field %s but was not present",
                        company, field.getName());
                throw new CustomerIdDefinitionMismatchException(msg);
            }
        });

        String key = customerIdFields.stream()
                .map(field -> {
                    String fieldName = field.getName();
                    String fieldValue = profileSearchFields.getOrDefault(fieldName, "");
                    if (!StringUtils.hasText(fieldValue) && field.isRequired()) {
                        throw new MissingCustomerIdFieldValueException(company, field.getName());
                    }
                    return fieldValue;
                })
                .collect(Collectors.joining(customerIdDefinition.getDelimiter()));

        return key;
    }

    public String getCustomerIdHash(String company, ProfileSearchDto profileSearchDto){
        CustomerIdDefinition customerIdDefinition = customerIdDefinitionService.get(company);
        String key = getCustomerId(company, profileSearchDto);

        switch(customerIdDefinition.getFunction()) {
            case MD5:
                return DigestUtils.md5Hex(key);
            case SHA256:
                return DigestUtils.sha256Hex(key);
            case SHA512:
                return DigestUtils.sha512Hex(key);
            default:
                throw new UnknownHashingFunctionException(customerIdDefinition.getFunction());
        }
    }

    public String getCustomerIdHash(String company, ProfileModel profileModel){
        CustomerIdDefinition customerIdDefinition = customerIdDefinitionService.get(company);
        String key = getCustomerId(company, profileModel);

        switch(customerIdDefinition.getFunction()) {
            case MD5:
                return DigestUtils.md5Hex(key);
            case SHA256:
                return DigestUtils.sha256Hex(key);
            case SHA512:
                return DigestUtils.sha512Hex(key);
            default:
                throw new UnknownHashingFunctionException(customerIdDefinition.getFunction());
        }
    }

    public void matchCustomerIdDefinition(String company, ProfileModel profileModel){
        CustomerIdDefinition customerIdDefinition = customerIdDefinitionService.get(company);

        matchCustomerIdDefinition(company, customerIdDefinition.getFields(), profileModel.getAttributes());
    }

    public void matchCustomerIdDefinition(String company, List<CustomerIdField> customerIdFields, Map<String, Object> profileFields){
        customerIdFields.forEach(field -> {
            if (!profileFields.containsKey(field.getName())) {
                String msg = String.format("company: %s, customerId definition expects field %s but was not present",
                        company, field.getName());
                throw new CustomerIdDefinitionMismatchException(msg);
            }
        });
    }

}
