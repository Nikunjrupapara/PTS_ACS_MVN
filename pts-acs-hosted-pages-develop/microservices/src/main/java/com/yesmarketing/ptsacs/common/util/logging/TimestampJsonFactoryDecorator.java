package com.yesmarketing.ptsacs.common.util.logging;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.logstash.logback.decorate.JsonFactoryDecorator;

public class TimestampJsonFactoryDecorator implements JsonFactoryDecorator {

	public JsonFactory decorate(JsonFactory factory) {
		((MappingJsonFactory) factory).getCodec()
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.registerModule(new JavaTimeModule());
		return factory;
	}
}
