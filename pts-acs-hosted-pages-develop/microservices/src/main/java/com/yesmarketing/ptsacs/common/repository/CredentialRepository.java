package com.yesmarketing.ptsacs.common.repository;

import com.yesmarketing.acsapi.auth.model.CredentialModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends MongoRepository<CredentialModel, String> {

    Optional<CredentialModel> findByCompany(String company);

}
