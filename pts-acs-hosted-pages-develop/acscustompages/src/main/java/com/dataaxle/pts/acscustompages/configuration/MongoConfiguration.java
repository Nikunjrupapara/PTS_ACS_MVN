package com.dataaxle.pts.acscustompages.configuration;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("com.dataaxle.pts")
public class MongoConfiguration {

	private final MongoTemplate mongoTemplate;

	private IndexOperations indexOperations;

	@Autowired
	public MongoConfiguration(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initialiseIndexesAfterStartup() {
		/*indexOperations = mongoTemplate.indexOps(AppDetails.class);
		indexOperations.ensureIndex(
			new Index()
				.named("appDetailsCompanyAndPath")
				.on("appId", Sort.Direction.ASC)
				.unique()
		);
		indexOperations = mongoTemplate.indexOps(Domain.class);
		indexOperations.ensureIndex(
			new Index()
				.named("domainName")
				.on("name", Sort.Direction.ASC)
				.unique()
		);*/
	}
}
