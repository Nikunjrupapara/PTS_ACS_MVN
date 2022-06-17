package com.dataaxle.pts.acscustompages.model;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.ACS_DATETIME_REGEX;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.ACS_DATE_REGEX;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.ADOBE_FORMAT;

import com.dataaxle.pts.acscustompages.exception.UnsupportedNodeTypeException;
import com.dataaxle.pts.acscustompages.model.loadaction.LoadActionConfig;
import com.dataaxle.pts.acscustompages.model.loadaction.ValueProvider;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomResourceRecord implements Serializable, ValueProvider {

	private static final long serialVersionUID = 3953413545146017569L;

	@JsonValue
	Map<String, Object> values = new HashMap<>();

	public void addValue(String name, Object value) {
		values.put(name, value);
	}

	public void addValue(Map<String, Object> valueMap) {
		values.putAll(valueMap);
	}

	public Optional<Object> getValue(String name) {
		if (values.containsKey(name)) {
			return Optional.of(values.get(name));
		}
		return Optional.empty();
	}

	public String getValueAsString(String name) {
		return getValue(name).orElse("").toString();
	}

	public boolean hasField(String name) { return values.containsKey(name); }

	public static CustomResourceRecord copy(CustomResourceRecord source) {
		CustomResourceRecord copy = new CustomResourceRecord();
		copy.addValue(source.getValues());
		return copy;
	}

	public static Comparator<CustomResourceRecord> comparator(List<SortParameter> sortParameters) {
		return ChainComparator.of(sortParameters);
	}

	@Override
	public LoadActionConfig.ParameterSource getParameterSource() { return LoadActionConfig.ParameterSource.CUSTOM_RESOURCE_RECORD; }

	@Override
	public String provideValue(String name) { return getValueAsString(name); }

	/**
	 * Custom comparator that sorts CustomResourceRecords based on the provided fieldName using the provided SortDirection.
	 */
	@Value
	@AllArgsConstructor
	private static class FieldComparator implements java.util.Comparator<CustomResourceRecord> {

		String fieldName;

		@Override
		public int compare(CustomResourceRecord o1, CustomResourceRecord o2) {
			return o1.getValueAsString(fieldName).compareTo(o2.getValueAsString(fieldName));
		}

		public static FieldComparator of(SortParameter sortParameter) {
			FieldComparator fieldComparator = new FieldComparator(sortParameter.getFieldName());
			if (sortParameter.getDirection() == SortDirection.DESCENDING) {
				fieldComparator.reversed();
			}
			return fieldComparator;
		}
	}

	/**
	 * Wrapper class that combines multiple FieldComparators to facilitate sorting CustomResourceRecords using multiple
	 * columns.
	 * Reference: https://medium.com/@lonell.liburd/chaining-comparators-and-sorting-in-java-498b8e1e34a8
	 */
	@Value
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ChainComparator implements Comparator<CustomResourceRecord> {

		List<FieldComparator> comparators;

		@Override
		public int compare(CustomResourceRecord o1, CustomResourceRecord o2) {
			int result;
			for (FieldComparator comparator : comparators) {
				if ((result = comparator.compare(o1, o2)) != 0) {
					return result;
				}
			}
			return 0;
		}

		public static ChainComparator of(List<SortParameter> parameters) {
			return new ChainComparator(parameters.stream().map(FieldComparator::of).collect(Collectors.toList()));
		}
	}

	public static class Deserializer extends JsonDeserializer<CustomResourceRecord> {

		private final ObjectMapper objectMapper;

		private final CustomResourceParameters parameters;

		@Deprecated
		public Deserializer(ObjectMapper objectMapper) {
			this.objectMapper = objectMapper;
			this.parameters = CustomResourceParameters.builder().build();
		}

		public Deserializer(ObjectMapper objectMapper, CustomResourceParameters parameters) {
			this.objectMapper = objectMapper;
			this.parameters = parameters;
		}

		@Override
		public CustomResourceRecord deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
			Map<String, Object> values = new HashMap<>();
			JsonNode rootNode = objectMapper.readTree(jsonParser);
			Iterator<String> keys = rootNode.fieldNames();
			while (keys.hasNext()) {
				String key = keys.next();
				JsonNodeType nodeType = rootNode.get(key).getNodeType();
				String value;
				switch (nodeType) {
					case BOOLEAN:
						values.put(key, rootNode.get(key).asBoolean());
						break;
					case NUMBER:
						value = rootNode.get(key).asText();
						processNumber(values, key, value);
						break;
					case STRING:
						value = rootNode.get(key).asText();
						processString(values, key, value);
						break;
					case ARRAY:
					case BINARY:
					case MISSING:
					case NULL:
					case OBJECT:
					case POJO:
					default:
						throw new UnsupportedNodeTypeException(nodeType.name());
				}
			}
			return new CustomResourceRecord(values);
		}

		private void processNumber(Map<String, Object> values, String key, String value) {
			try {
				Integer val = Integer.parseInt(value);
				values.put(key, val);
			} catch (NumberFormatException i) {
				try {
					Long val = Long.parseLong(value);
					values.put(key, val);
				} catch (NumberFormatException l) {
					try {
						Double val = Double.parseDouble(value);
						values.put(key, val);
					} catch (NumberFormatException d) {
						try {
							Float val = Float.parseFloat(value);
							values.put(key, val);
						} catch (NumberFormatException f) {
							throw new IllegalArgumentException(String.format("Unable to convert value to a number: %s", value));
						}
					}
				}
			}
		}

		private void processString(Map<String, Object> values, String key, String value) {
			if (value.matches("[0-9]+") && !numberAsString(key)) {
				processNumber(values, key, value);
			} else if (value.matches(ACS_DATE_REGEX)) {
				LocalDate ld = LocalDate.parse(value);
				values.put(key, ld);
			} else if (value.matches(ACS_DATETIME_REGEX)) {
				LocalDateTime ldt = LocalDateTime.parse(value, ADOBE_FORMAT);
				values.put(key, ldt);
			} else {
				values.put(key, value);
			}
		}

		private boolean numberAsString(String fieldName) {
			return parameters.getNumbersAsString().contains(fieldName);
		}
	}
}
