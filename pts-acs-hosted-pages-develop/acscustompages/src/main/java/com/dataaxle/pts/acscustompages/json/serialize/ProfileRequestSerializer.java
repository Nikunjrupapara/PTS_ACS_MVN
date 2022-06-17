package com.dataaxle.pts.acscustompages.json.serialize;

import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProfileRequestSerializer extends JsonSerializer<ProfileRequest> {

	@Override
	public void serialize(ProfileRequest profileRequest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();
		if (profileRequest.isUpdateProfile()) {
			jsonGenerator.writeObjectField("profile", profileRequest.getProfile());
		}

		if (profileRequest.isUpdateServices()) {
			jsonGenerator.writeObjectField("services", profileRequest.getServicesRequest());
		}

		if (profileRequest.isCustomResourcesRequest()) {
			jsonGenerator.writeObjectField("customResources", profileRequest.getCustomResourceRequests());
		}

		if (profileRequest.isTriggerEmail()) {
			jsonGenerator.writeArrayFieldStart("emails");
			jsonGenerator.writeObject(profileRequest.getTriggerEmailData());
			jsonGenerator.writeEndArray();
		}
		jsonGenerator.writeEndObject();
	}
}
