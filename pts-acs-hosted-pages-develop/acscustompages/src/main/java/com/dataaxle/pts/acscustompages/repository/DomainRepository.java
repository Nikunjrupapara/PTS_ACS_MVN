package com.dataaxle.pts.acscustompages.repository;

import com.dataaxle.pts.acscustompages.model.Domain;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DomainRepository extends MongoRepository<Domain, String> {
}
