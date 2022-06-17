package com.dataaxle.pts.acscustompages.json.serialize;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ProfileSerializer extends JsonSerializer<Profile> {

	private static final String CUSTOM_FIELD_PREFIX = "cus";

	Predicate<String> isCustomField = key -> key.startsWith(CUSTOM_FIELD_PREFIX);

	Predicate<String> isNotCustomField = key -> !key.startsWith(CUSTOM_FIELD_PREFIX);

	Predicate<String> isObject = key -> key.contains(".");

	Predicate<String> isNotObject = key -> !key.contains(".");

	@Override
	public void serialize(Profile profile, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();
		Map<String, Object> fields = profile.getFields();
		// find and write standard ACS profile fields
		fields.keySet().stream()
			.filter(isNotCustomField)
			.filter(isNotObject)
			.sorted(Comparator.naturalOrder())
			.forEach(key -> {
				Object value = fields.get(key);
				writeValue(jsonGenerator, key, value);
			});
		boolean containsObjects = fields.keySet().stream().anyMatch(isObject);
		if (containsObjects) {
			Set<String> objectNames = fields.keySet().stream()
										   .filter(isObject)
										   .map(name -> name.split(Pattern.quote("."))[0])
										   .collect(Collectors.toSet());
			objectNames.stream()
				.sorted(Comparator.naturalOrder())
				.forEach(objectName -> {
					try {
						jsonGenerator.writeObjectFieldStart(objectName);
						fields.keySet().stream()
							.filter(name -> name.startsWith(String.format("%s.", objectName)))
							.sorted(Comparator.naturalOrder())
							.map(name -> name.split(Pattern.quote("."))[1])
							.forEach(fieldName -> {
								String key = String.format("%s.%s", objectName, fieldName);
								Object value = fields.get(key);
								writeValue(jsonGenerator, fieldName, value);
							});
						jsonGenerator.writeEndObject();
					} catch (IOException e) {
						throw new ServerErrorException("Error writing profile JSON", e);
					}
				});
		}
		boolean containsCustomFields = fields.keySet().stream().anyMatch(isCustomField);
		if (containsCustomFields) {
			jsonGenerator.writeObjectFieldStart("customFields");
			fields.keySet().stream()
				.filter(isCustomField)
				.filter(isNotObject)
				.sorted(Comparator.naturalOrder())
				.forEach(key -> {
					Object value = fields.get(key);
					writeValue(jsonGenerator, key, value);
				});
			jsonGenerator.writeEndObject();
		}
		jsonGenerator.writeEndObject();
	}

	private void writeValue(JsonGenerator gen, String key, Object value) {
		try {
			if (value instanceof String) {
				gen.writeStringField(key, (String) value);
			} else {
				gen.writeObjectField(key, value);
			}
		} catch (IOException e) {
			throw new ServerErrorException("Error writing profile JSON", e);
		}

	}
}
