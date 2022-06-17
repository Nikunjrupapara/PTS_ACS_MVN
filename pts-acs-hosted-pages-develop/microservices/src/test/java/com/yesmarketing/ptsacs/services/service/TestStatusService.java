package com.yesmarketing.ptsacs.services.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmarketing.acsapi.auth.model.CredentialModel;
import com.yesmarketing.acsapi.auth.service.CredentialManagementService;
import com.yesmarketing.acsapi.exception.SagasUnauthorizedException;
import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ResponseModel;
import com.yesmarketing.ptsacs.common.exception.AppException;
import com.yesmarketing.ptsacs.common.exception.HealthCheckException;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchDto;
import com.yesmarketing.ptsacs.services.service.impl.StatusServiceImpl;
import com.yesmarketing.ptsacs.services.util.CustomerIdDefinitionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TestStatusService {

    StatusService statusService; //= Mockito.mock(StatusService.class);

    ProfileService profileService = Mockito.mock(ProfileService.class);

    CredentialManagementService credentialManagementService = Mockito.mock(CredentialManagementService.class);


    @BeforeEach
    void setup() {

        Mockito
                .when(profileService.getProfileByCustomerIdHash(anyString(), anyString())).thenAnswer(
                        invocation -> {
                            String company = invocation.getArgument(0);
                            String customerId = invocation.getArgument(1);

                            if(company.equals("existing") && customerId.equals("existing"))
                                return new ResponseModel<ProfileModel>();

                            ObjectNotFoundException companyNotFoundException =  new ObjectNotFoundException("resourceNotFound.message","");
                            ObjectNotFoundException profileNotFoundException =  new ObjectNotFoundException("profileNotFoundByEncId.message","");

                            if(company.equals("expired"))throw new SagasUnauthorizedException();

                            if(company.equals("existing")) throw profileNotFoundException;
                            throw companyNotFoundException;

                            // which error will we get when credentials are expired? need to verify...
                        }
                );

        Mockito
                .when(credentialManagementService.getCredentials()).thenAnswer(
                        invocation -> {
                            List<CredentialModel> credentialModelList = new LinkedList<>();
                            CredentialModel credentialModel1 = new CredentialModel();
                            credentialModel1.setCompany("company2");
                            credentialModelList.add(credentialModel1);

                            CredentialModel credentialModel2 = new CredentialModel();
                            credentialModel2.setCompany("company1");
                            credentialModelList.add(credentialModel2);

                            return credentialModelList;
                        }
                );


        statusService = new StatusServiceImpl(profileService,credentialManagementService);
    }


    @Test
    public void serviceIsUpExistingCompanyExistingUser(){
        String company ="existing";
        Health health = statusService.getStatus(company,"existing");
        assertAll("Check correct status and message fore existing company and existing user",
            ()-> assertEquals(health.getStatus().getCode(),"UP"),
            ()-> assertEquals(health.getDetails().get("message"),String.format("Company '%s' is found. User is found.",company))
        );
    }

    @Test
    public void serviceIsUpExistingCompanyNonExistingUser(){
        String company = "existing";
        Health health = statusService.getStatus(company,"nonexisting");
        assertAll("Check correct status and message fore existing company and non existing user",
                ()-> assertEquals(health.getStatus().getCode(),"UP"),
                ()-> assertEquals(health.getDetails().get("message"),String.format("Company '%s' is found. User is not found.",company))
        );
    }


    @Test
    public void checkCompanyNotFound(){
        String company = "nonexisting";

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            statusService.getStatus(company,"nonexisting");
        });

        String expectedMessage = String.format("No credential found for company '%s'.",company);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    public void checkExpiredCredentials(){
        // TBD

        String company = "expired";

        Exception exception = assertThrows(HealthCheckException.class, () -> {
            statusService.getStatus(company,"nonexisting");
        });

        String expectedMessage = "Please check credential for the company, it might be expired.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void checkCompaniesEndpointAndSortOrderForCompanies(){
        List<String> companies = statusService.getCompanies();
        assertTrue(companies.get(0).equals("company1"));
        assertTrue(companies.get(1).equals("company2"));
    }


}
