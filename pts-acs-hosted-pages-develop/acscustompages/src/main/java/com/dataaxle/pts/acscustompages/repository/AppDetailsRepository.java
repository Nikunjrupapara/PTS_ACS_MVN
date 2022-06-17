package com.dataaxle.pts.acscustompages.repository;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppDetailsRepository extends MongoRepository<AppDetails, AppDetails.AppDetailsId> {

}
