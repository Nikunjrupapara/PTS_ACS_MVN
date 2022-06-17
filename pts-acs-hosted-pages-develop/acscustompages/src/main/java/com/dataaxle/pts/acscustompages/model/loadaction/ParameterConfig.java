package com.dataaxle.pts.acscustompages.model.loadaction;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode
@ToString
public class ParameterConfig implements Serializable {
	/**
	 * This class is used to define the configuration to derive the value for a query parameter in a request to the
	 * Microservices API.
	 */

	private static final long serialVersionUID = 6386039960288348004L;

	/**
	 * The source of the value
	 */
	LoadActionConfig.ParameterSource source;

	/**
	 * The name of the holder of the in the source
	 */
	String name;

	private ParameterConfig(Builder builder) {
		this.source = builder.source;
		this.name = builder.name;
	}

	public static Builder builder() { return new Builder(); }

	public static class Builder {
		private Builder() {}

		private LoadActionConfig.ParameterSource source;

		private String name;

		private Builder self() { return this; }

		public Builder source(LoadActionConfig.ParameterSource source) {
			this.source = source;
			return self();
		}

		public Builder name(String name) {
			this.name = name;
			return self();
		}

		public ParameterConfig build() {
			ParameterConfig result = new ParameterConfig(this);
			validate(result);
			return result;
		}

		private void validate(ParameterConfig obj) {

		}
	}
}
