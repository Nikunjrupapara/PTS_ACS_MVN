package com.dataaxle.pts.acscustompages.model;

import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Value
public class CustomResourceParameters implements Serializable {

	private static final long serialVersionUID = -3399044785892473295L;

	/**
	 * The name of the company
	 */
	String company;

	/**
	 * The name of the custom resource in Adobe Campaign Standard
	 */
	String name;

	/**
	 * A list of fields on the custom resource that contain numeric data that should be treated as a string
	 */
	List<String> numbersAsString;

	private CustomResourceParameters(Builder builder) {
		this.company = builder.company;
		this.name = builder.name;
		this.numbersAsString = builder.numbersAsString;
	}

	public String getResourceId() { return String.format("%s.%s", company, name); }

	public static Builder builder() { return new Builder(); }

	public static class Builder {

		private Builder() {}

		String company;

		String name;

		List<String> numbersAsString = new ArrayList<>();

		private Builder self() { return this; }

		public Builder company(String company) {
			this.company = company;
			return self();
		}

		public Builder name(String name) {
			this.name = name;
			return self();
		}

		public Builder numberAsString(String name) {
			this.numbersAsString.add(name);
			return self();
		}

		public Builder numberAsString(List<String> names) {
			this.numbersAsString.addAll(names);
			return self();
		}

		public CustomResourceParameters build() {
			CustomResourceParameters result = new CustomResourceParameters(this);
			validate(result);
			return result;
		}

		private void validate(CustomResourceParameters obj) {

		}
	}
}
