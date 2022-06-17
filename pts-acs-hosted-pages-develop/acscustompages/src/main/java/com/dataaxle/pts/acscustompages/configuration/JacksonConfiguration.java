package com.dataaxle.pts.acscustompages.configuration;

import com.dataaxle.pts.acscustompages.json.deserialize.ProfileResponseDeserializer;
import com.dataaxle.pts.acscustompages.json.serialize.CustomResourceRequestsSerializer;
import com.dataaxle.pts.acscustompages.json.serialize.ProfileRequestSerializer;
import com.dataaxle.pts.acscustompages.json.serialize.ProfileSerializer;
import com.dataaxle.pts.acscustompages.json.serialize.ServicesRequestSerializer;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequests;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ServicesRequest;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfiguration {
	@Bean
	public ObjectMapper objectMapper() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss.SSS'Z'");
		LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(formatter);
		LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(formatter);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss'Z'");

		SimpleModule profileSerializer = new SimpleModule("ProfileSerializer");
		profileSerializer.addSerializer(Profile.class, new ProfileSerializer());

		/*SimpleModule profileDeserializer = new SimpleModule("ProfileDeserializer");
		profileDeserializer.addDeserializer(ProfileResponse.class, new ProfileResponseDeserializer())*/

		SimpleModule servicesRequest = new SimpleModule("Services Request");
		servicesRequest.addSerializer((ServicesRequest.class), new ServicesRequestSerializer());

		SimpleModule customResourcesSerializer = new SimpleModule("CustomResourcesSerializer");
		customResourcesSerializer.addSerializer(CustomResourceRequests.class, new CustomResourceRequestsSerializer());

		SimpleModule compositeSerializer = new SimpleModule("Composite Serializer");
		compositeSerializer.addSerializer(ProfileRequest.class, new ProfileRequestSerializer(/*objectMapper*/));

		ObjectMapper objectMapper = new ObjectMapper()
				   .registerModule(new Jdk8Module())
				   .registerModule(new JavaTimeModule()
									   .addDeserializer(LocalDateTime.class, localDateTimeDeserializer)
									   .addSerializer(LocalDateTime.class, localDateTimeSerializer))
				   .setDateFormat(dateFormat)
				   .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
				   .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				   .registerModule(profileSerializer)
				   .registerModule(servicesRequest)
				   .registerModule(customResourcesSerializer)
				   .registerModule(compositeSerializer);

		SimpleModule customResourceRecordDeserializer = new SimpleModule("customResourceRecordDeserializer");
		customResourceRecordDeserializer.addDeserializer(CustomResourceRecord.class, new CustomResourceRecord.Deserializer(objectMapper));
		objectMapper.registerModule(customResourceRecordDeserializer);
		return objectMapper;
	}
}
