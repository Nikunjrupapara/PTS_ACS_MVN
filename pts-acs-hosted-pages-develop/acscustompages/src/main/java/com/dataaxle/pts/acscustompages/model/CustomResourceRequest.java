package com.dataaxle.pts.acscustompages.model;

import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Value
@AllArgsConstructor
public class CustomResourceRequest implements BaseCustomResourceRequest {

	@JsonProperty(value = "name")
	String resourceName;

	List<CustomResourceRecord> records;

	@JsonIgnore
	ProcessingAction action;

	@JsonIgnore
	List<String> numbersAsString;

	private CustomResourceRequest(Builder builder) {
		this.resourceName = builder.customResource;
		this.records = builder.records;
		this.action = builder.action;
		this.numbersAsString = builder.numbersAsString;
	}

	// retained for backwards compatibility with existing code
	@Deprecated
	public CustomResourceRequest(String resourceName) {
		this.resourceName = resourceName;
		this.records = new LinkedList<>();
		this.action = ProcessingAction.UPDATE_CUSTOM_RESOURCE;
		this.numbersAsString = Collections.emptyList();
	}

	@Deprecated
	public CustomResourceRequest(String resourceName, List<CustomResourceRecord> records) {
		this.resourceName = resourceName;
		this.records = records;
		this.action = ProcessingAction.CREATE_CUSTOM_RESOURCE;
		this.numbersAsString = Collections.emptyList();
	}

	@Deprecated
	public CustomResourceRequest(String resourceName, ProcessingAction action) {
		this.resourceName = resourceName;
		this.action = action;
		this.records = new LinkedList<>();
		this.numbersAsString = Collections.emptyList();
	}

	public void addRecord(CustomResourceRecord record) {
		records.add(record);
	}

	public boolean hasActions() {
		return !CollectionUtils.isEmpty(records);
	}

	@JsonIgnore
	public boolean isCreate() { return action == ProcessingAction.CREATE_CUSTOM_RESOURCE; }

	@JsonIgnore
	public boolean isUpdate() { return action == ProcessingAction.UPDATE_CUSTOM_RESOURCE; }

	@JsonIgnore
	public CustomResourceParameters getCustomResourceParameters(String company) {
		return CustomResourceParameters.builder()
			.company(company)
			.name(resourceName)
			.numberAsString(numbersAsString)
			.build();
	}

	public static Builder builder() { return new Builder(); }

	public static class Builder {
		private Builder() {}

		String customResource;

		ProcessingAction action;

		final List<CustomResourceRecord> records = new ArrayList<>();

		final List<String> numbersAsString = new ArrayList<>();

		private Builder self() { return this; }

		public Builder customResource(String name) {
			this.customResource = name;
			return self();
		}

		public Builder action(ProcessingAction action) {
			this.action = action;
			return self();
		}

		public Builder records(List<CustomResourceRecord> records) {
			this.records.addAll(records);
			return self();
		}

		public Builder numberAsString(List<String> names) {
			this.numbersAsString.addAll(names);
			return self();
		}

		public CustomResourceRequest build() {
			return new CustomResourceRequest(this);
		}
	}
}
