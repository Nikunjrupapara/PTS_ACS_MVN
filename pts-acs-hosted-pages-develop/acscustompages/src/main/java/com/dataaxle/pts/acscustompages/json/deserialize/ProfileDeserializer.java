package com.dataaxle.pts.acscustompages.json.deserialize;

import com.dataaxle.pts.acscustompages.model.Profile;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class ProfileDeserializer extends JsonDeserializer<Profile> {

	private static final String CUSTOM_FIELDS_NODE = "customFields";

	private final ObjectMapper objectMapper;

	public ProfileDeserializer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Profile deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		JsonNode profileNode = objectMapper.readTree(jsonParser);
		return parseProfileNode(profileNode);
	}

	private Profile parseProfileNode(JsonNode profileNode) {
		Profile profile = new Profile();
		Iterator<String> fieldNames = profileNode.fieldNames();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			JsonNode fieldNode = profileNode.get(fieldName);
			if (fieldNode.isValueNode()) {
				processValueNode(fieldName, fieldNode, profile);
			} else if (fieldNode.isContainerNode()) {
				if (fieldName.equals(CUSTOM_FIELDS_NODE)) {
					processCustomFields(fieldNode, profile);
				} else {
					processNestedObject(fieldName, fieldNode, profile);
				}
			} else {
				LOG.warn("fieldName {} is unexpected nodeType {}", fieldName, fieldNode.getNodeType().name());
			}
		}
		return profile;
	}

	private void processValueNode(String fieldName, JsonNode fieldNode, Profile profile) {
		switch (fieldNode.getNodeType()) {
			case ARRAY:
			case BINARY:
			case MISSING:
			case OBJECT:
			case POJO:
				LOG.warn("fieldName {} has unexpected nodeType of {}", fieldName, fieldNode.getNodeType().name());
				break;
			case BOOLEAN:
				profile.addField(fieldName, fieldNode.booleanValue());
				break;
			case NULL:
				profile.addField(fieldName, "");
				break;
			case NUMBER:
				switch (fieldNode.numberType()) {
					case INT:
						profile.addField(fieldName, fieldNode.intValue());
						break;
					case LONG:
						profile.addField(fieldName, fieldNode.longValue());
						break;
					case BIG_INTEGER:
						profile.addField(fieldName, fieldNode.bigIntegerValue());
						break;
					case FLOAT:
						profile.addField(fieldName, fieldNode.floatValue());
						break;
					case DOUBLE:
						profile.addField(fieldName, fieldNode.doubleValue());
						break;
					case BIG_DECIMAL:
						profile.addField(fieldName, fieldNode.decimalValue());
						break;
				}
				break;
			case STRING:
				profile.addField(fieldName, fieldNode.textValue());
				break;
		}
	}

	private void processCustomFields(JsonNode customFieldsNode, Profile profile) {
		Iterator<String> fieldNames = customFieldsNode.fieldNames();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			JsonNode fieldNode = customFieldsNode.get(fieldName);
			if (fieldNode.isValueNode()) {
				processValueNode(fieldName, fieldNode, profile);
			} else if (fieldNode.isObject()) {
				processNestedObject(fieldName, fieldNode, profile);
			} else {
				LOG.warn("fieldName {} is unexpected nodeType {}", fieldName, fieldNode.getNodeType().name());
			}
		}
	}

	private void processNestedObject(String objectName, JsonNode objectNode, Profile profile) {
		Iterator<String> fieldNames = objectNode.fieldNames();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			JsonNode fieldNode = objectNode.get(fieldName);
			if (fieldNode.isValueNode()) {
				String profileName = String.format("%s.%s", objectName, fieldName);
				processValueNode(profileName, fieldNode, profile);
			} else {
				LOG.warn("fieldName {} is unexpected nodeType {}", fieldName, fieldNode.getNodeType().name());
			}
		}
	}
}
