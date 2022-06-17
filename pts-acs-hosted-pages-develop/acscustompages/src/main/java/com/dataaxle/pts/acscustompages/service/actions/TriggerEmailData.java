package com.dataaxle.pts.acscustompages.service.actions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TriggerEmailData {

	private String eventId;

	private String email;

	private LocalDateTime scheduled;

	private LocalDateTime expiration;

	@JsonProperty(value = "ctx")
	private Map<String, Object> context = new HashMap<>();

	void addContextField(String name, Object value) {
		context.put(name, value);
	}
}
