package com.yesmarketing.ptsacs.common.configuration;

import com.yesmarketing.ptsacs.services.model.FormConfig;
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
@EnableMongoRepositories("com.yesmarketing.ptsacs")
public class PtsMongoRepositoryConfiguration {

	private final MongoTemplate mongoTemplate;

	private IndexOperations indexOperations;

	@Autowired
	public PtsMongoRepositoryConfiguration(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initialiseIndexesAfterStartUp() {
		formConfigIndexes();
	}

	private void formConfigIndexes() {
		indexOperations = mongoTemplate.indexOps(FormConfig.class);
		indexOperations.ensureIndex(
			new Index()
				.named("formConfigCompanyCode")
				.on("company", Sort.Direction.ASC)
				.on("code", Sort.Direction.ASC)
				.unique()
		);
	}
}
