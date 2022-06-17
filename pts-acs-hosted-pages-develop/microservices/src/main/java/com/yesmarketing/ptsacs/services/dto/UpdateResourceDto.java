package com.yesmarketing.ptsacs.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.yesmarketing.acsapi.model.GeoUnit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UpdateResourceDto implements Serializable {

	private static final long serialVersionUID = 168991326636880125L;

	@JsonProperty("PKey")
	private String PKey;

	@JsonProperty("href")
	private String href;

}
