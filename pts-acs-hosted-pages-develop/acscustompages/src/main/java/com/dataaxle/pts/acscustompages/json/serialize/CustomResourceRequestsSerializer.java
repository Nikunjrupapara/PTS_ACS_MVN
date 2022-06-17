package com.dataaxle.pts.acscustompages.json.serialize;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequests;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CustomResourceRequestsSerializer extends JsonSerializer<CustomResourceRequests> {
	@Override
	public void serialize(CustomResourceRequests customResourceRequests, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		Map<String, List<CustomResourceRequest>> requestsMap = customResourceRequests.getCustomResourceRequests();
		List<CustomResourceRequest> requests = new LinkedList<>();
		requestsMap.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.map(Map.Entry::getValue)
			.forEach(requests::addAll);

		jsonGenerator.writeStartArray();
		requests.stream()
			.sorted(Comparator.comparing(CustomResourceRequest::getResourceName))
			.forEachOrdered(req -> {
				try {
					jsonGenerator.writeObject(req);
				} catch (IOException e) {
					throw new ServerErrorException("Error writing CustomResourceRequests JSON", e);
				}
			});
		jsonGenerator.writeEndArray();
	}
}
