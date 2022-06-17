package com.dataaxle.pts.acscustompages.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TriggerEmailResponse {
	String email;

	String href;

	String status;

	String PKey;
}
