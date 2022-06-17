package com.yesmarketing.ptsacs.services.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class FormClaim {

	@JsonProperty("company")
	String company;

	@JsonProperty("uuid")
	String uuid;
}
