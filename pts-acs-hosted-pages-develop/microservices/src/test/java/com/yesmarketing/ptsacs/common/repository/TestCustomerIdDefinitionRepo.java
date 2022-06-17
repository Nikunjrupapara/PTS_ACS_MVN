package com.yesmarketing.ptsacs.common.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yesmarketing.ptsacs.common.exception.NoCustomerIdDefinitionException;
import com.yesmarketing.ptsacs.services.model.CustomerIdDefinition;
import com.yesmarketing.ptsacs.services.model.CustomerIdField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
@ActiveProfiles("mongotest")
public class TestCustomerIdDefinitionRepo {

    @Autowired
    CustomerIdDefinitionRepository customerIdDefinitionRepository;

    private String company;

    @BeforeEach
     void setupMongoDocuments(){
        CustomerIdDefinition customerIdDefinition = new CustomerIdDefinition();
        List<CustomerIdField> fields = new LinkedList<>();
        fields.add(new CustomerIdField("email", true));

        customerIdDefinition.setFields(fields);
        customerIdDefinition.setCompany("ymnewsolutions");
        customerIdDefinitionRepository.save(customerIdDefinition);
    }

    @Test
    void testIdDefinitionRepoGet(){
        company = "ymnewsolutions";
        CustomerIdDefinition customerIdDefinition = customerIdDefinitionRepository.findByCompany(company)
                                                        .orElseThrow(() -> new NoCustomerIdDefinitionException(company));
        Assert.isTrue(customerIdDefinition.getCompany().equals(company),"Unable to retrieve CustomerIdDefinition from mongo");
    }

    @Test
    void testIdDefinitionRepoGetNonExistent(){

        assertThatThrownBy(() -> customerIdDefinitionRepository.findByCompany("test").get())
                .isInstanceOf(NoSuchElementException.class);
    }
}
