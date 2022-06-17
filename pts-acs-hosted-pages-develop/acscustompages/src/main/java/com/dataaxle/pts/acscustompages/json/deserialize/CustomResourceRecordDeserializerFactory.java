package com.dataaxle.pts.acscustompages.json.deserialize;

import com.dataaxle.pts.acscustompages.model.CustomResourceParameters;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.HashMap;
import java.util.Map;

public class CustomResourceRecordDeserializerFactory {

	private static final Map<String, CustomResourceRecord.Deserializer> deserializers = new HashMap<>();

	public static ObjectMapper getMapper(ObjectMapper objectMapperIn, CustomResourceParameters parameters) {
		// Copy the provided ObjectMapper so that in process configuration changes do not affect the underlying object
		ObjectMapper objectMapperOut = objectMapperIn.copy();
		SimpleModule simpleModule = new SimpleModule();
		CustomResourceRecord.Deserializer deserializer = getDeserializer(objectMapperOut, parameters);
		simpleModule.addDeserializer(CustomResourceRecord.class, deserializer/*new CustomResourceRecord.Deserializer(objectMapper,
				parameters)*/);
		objectMapperOut.registerModule(simpleModule);
		return objectMapperOut;
	}

	private static CustomResourceRecord.Deserializer getDeserializer(ObjectMapper objectMapper, CustomResourceParameters parameters) {
		String customResourceId = parameters.getResourceId();
		if (deserializers.containsKey(customResourceId)) {
			return deserializers.get(customResourceId);
		}
		CustomResourceRecord.Deserializer deserializer = new CustomResourceRecord.Deserializer(objectMapper, parameters);
		deserializers.put(customResourceId, deserializer);
		return deserializer;
	}
}
