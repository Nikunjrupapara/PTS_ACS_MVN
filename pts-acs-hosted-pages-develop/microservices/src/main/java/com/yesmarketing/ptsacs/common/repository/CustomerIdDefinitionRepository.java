package com.yesmarketing.ptsacs.common.repository;

import com.yesmarketing.ptsacs.services.model.CustomerIdDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CustomerIdDefinitionRepository  extends MongoRepository<CustomerIdDefinition, String> {
    Optional<CustomerIdDefinition> findByCompany(String company);
}
