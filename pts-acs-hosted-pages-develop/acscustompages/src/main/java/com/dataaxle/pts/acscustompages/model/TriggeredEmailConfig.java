package com.dataaxle.pts.acscustompages.model;

import com.dataaxle.pts.acscustompages.exception.NoEventIdForBrandException;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import lombok.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Value
public class TriggeredEmailConfig implements Serializable {

	private static final long serialVersionUID = 870411432199342680L;

	Map<String, String> eventIds;

	ValueGenerator<?> email;

	ValueGenerator<?> scheduled;

	ValueGenerator<?> expiration;

	Map<String, ValueGenerator<?>> contextFields;

	private TriggeredEmailConfig(Builder builder) {
		this.email = builder.email;
		this.expiration = builder.expiration;
		this.eventIds = builder.eventIds;
		this.scheduled = builder.scheduled;
		this.contextFields = builder.contextFields;
	}

	public String getEventId(String brand) {
		if (eventIds.containsKey(brand)) {
			return eventIds.get(brand);
		}
		throw new NoEventIdForBrandException(brand);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Map<String, String> eventIds;

		private ValueGenerator<?> email;

		private ValueGenerator<?> scheduled;

		private ValueGenerator<?> expiration;

		private final Map<String, ValueGenerator<?>> contextFields = new HashMap<>();

		public Builder eventIds(Map<String, String> eventIds) {
			this.eventIds = eventIds;
			return this;
		}

		public Builder email(ValueGenerator<?> email) {
			this.email = email;
			return this;
		}

		public Builder scheduled(ValueGenerator<?> scheduled) {
			this.scheduled = scheduled;
			return this;
		}

		public Builder expiration(ValueGenerator<?> expiration) {
			this.expiration = expiration;
			return this;
		}

		public Builder contextField(String name, ValueGenerator<?> valueGenerator) {
			this.contextFields.put(name, valueGenerator);
			return this;
		}

		public Builder contextField(ValueGenerator<?> valueGenerator) {
			this.contextFields.put(valueGenerator.getAcsFieldName(), valueGenerator);
			return this;
		}

		public TriggeredEmailConfig build() {
			return new TriggeredEmailConfig(this);
		}
	}
}
