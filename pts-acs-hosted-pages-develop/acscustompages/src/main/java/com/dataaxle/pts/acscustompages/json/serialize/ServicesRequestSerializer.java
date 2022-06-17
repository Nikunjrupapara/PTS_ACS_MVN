package com.dataaxle.pts.acscustompages.json.serialize;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.ServicesRequest;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServicesRequestSerializer extends JsonSerializer<ServicesRequest> {

	Predicate<Map.Entry<String, ServiceAction>> isAdd = entry -> entry.getValue().equals(ServiceAction.ADD);

	Predicate<Map.Entry<String, ServiceAction>> isRemove = entry -> entry.getValue().equals(ServiceAction.REMOVE);

	@Override
	public void serialize(ServicesRequest servicesRequest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		Map<String, ServiceAction> actions = servicesRequest.getServices();

		jsonGenerator.writeStartObject();
		// Write add services
		jsonGenerator.writeArrayFieldStart("add");
		List<String> add = actions.entrySet().stream()
						   .filter(isAdd)
						   .map(Map.Entry::getKey)
						   .collect(Collectors.toList());
		//jsonGenerator.writeArray(add, 0, add.length);
		add.forEach(service -> {
			try {
				jsonGenerator.writeString(service);
			} catch (IOException e) {
				throw new ServerErrorException("Error writing Services Request JSON", e);
			}
		});
		//add.forEach()
		jsonGenerator.writeEndArray();

		// Write remove services
		jsonGenerator.writeArrayFieldStart("remove");
		List<String> remove = actions.entrySet().stream()
							  .filter(isRemove)
							  .map(Map.Entry::getKey)
							  .collect(Collectors.toList());
		//jsonGenerator.writeArray(remove, 0, remove.length);
		remove.forEach(service -> {
			try {
				jsonGenerator.writeString(service);
			} catch (IOException e) {
				throw new ServerErrorException("Error writing Services Request JSON", e);
			}
		});

		jsonGenerator.writeEndArray();
		jsonGenerator.writeEndObject();
	}
}
