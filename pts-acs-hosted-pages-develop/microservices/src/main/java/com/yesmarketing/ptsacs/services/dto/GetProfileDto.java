package com.yesmarketing.ptsacs.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.yesmarketing.acsapi.model.GeoUnit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true)
public class GetProfileDto extends CreateProfileDto implements Serializable {

	private static final long serialVersionUID = 5365913266368802684L;

	@JsonProperty("PKey")
	private String PKey;

	private String acsId;

	private Integer age;

	private String created;

	private String cryptedId;

	private String customerId;

	private String domain;

	// geoUnit is deprecated by Adobe so placing it in this class to prevent setting it when creating a new profile.
	private GeoUnit geoUnit;

	private String href;

	private Boolean isExternal;

	private String lastModified;

	private LinkDto subscriptions;

	private String title;
}
