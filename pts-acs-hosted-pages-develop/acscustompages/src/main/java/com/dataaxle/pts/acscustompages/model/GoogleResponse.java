package com.dataaxle.pts.acscustompages.model;

import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
	"success",
	"score",
	"action",
	"challenge_ts",
	"hostname",
	"error-codes"
})
@Data
@ToString
@NoArgsConstructor
public class GoogleResponse {

	@JsonProperty("success")
	private boolean success = false;

	@JsonProperty("challenge_ts")
	private String challengeTs;

	@JsonProperty("hostname")
	private String hostname;

	@JsonProperty("error-codes")
	private ErrorCode[] errorCodes;

	@JsonProperty("score")
	private double score;

	@JsonProperty("action")
	private String action;

	public String responseString() {
		return String.format("%s;%s;%s;%s", success, action != null ? action : "", score,
			errorCodes != null ?
				Arrays.stream(errorCodes).map(Objects::toString).collect(Collectors.joining(","))
				: ""
		);
	}

	public static String responseStringNull() { return "false;;0;"; }

	enum ErrorCode {
		BAD_REQUEST(ErrorCode.BAD_REQUEST_NAME),
		MISSING_SECRET(ErrorCode.MISSING_SECRET_NAME),
		INVALID_SECRET(ErrorCode.INVALID_SECRET_NAME),
		MISSING_RESPONSE(ErrorCode.MISSING_RESPONSE_NAME),
		INVALID_RESPONSE(ErrorCode.INVALID_RESPONSE_NAME),
		TIMEOUT_OR_DUPLICATE(ErrorCode.TIMEOUT_OR_DUPLICATE_NAME);

		private final String name;

		public String getName() {
			return name;
		}

		ErrorCode(String name) {
			this.name = name;
		}

		@JsonCreator
		public static ErrorCode of(String nameStr) {
			return Arrays.stream(values())
					.filter(value -> value.getName().equals(nameStr))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException(String.format("Unknown ErrorCode '%s'", nameStr)));
		}

		@Override
		public String toString() { return name; }

		public static final String BAD_REQUEST_NAME = "bad-request";
		public static final String MISSING_SECRET_NAME = "missing-input-secret";
		public static final String INVALID_SECRET_NAME = "invalid-input-secret";
		public static final String MISSING_RESPONSE_NAME = "missing-input-response";
		public static final String INVALID_RESPONSE_NAME = "invalid-input-response";
		public static final String TIMEOUT_OR_DUPLICATE_NAME = "timeout-or-duplicate";
	}
}
