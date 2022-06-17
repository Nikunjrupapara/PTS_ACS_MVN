package com.yesmarketing.ptsacs.common.repository;

import com.yesmarketing.ptsacs.services.model.FormConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormConfigRepository extends MongoRepository<FormConfig, String> {

	Optional<FormConfig> findByUuid(String uuid);

	List<FormConfig> findByCompany(String company);
}
