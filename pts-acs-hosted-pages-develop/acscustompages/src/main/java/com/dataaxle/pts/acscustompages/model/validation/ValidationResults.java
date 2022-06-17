package com.dataaxle.pts.acscustompages.model.validation;

import lombok.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Value
public class ValidationResults implements Serializable {

	private static final long serialVersionUID = -7892901018674233969L;

	Map<String, ValidationResult> results = new HashMap<>();

	public void addResult(String name, ValidationResult result) {
		results.put(name, result);
	}

	public void addResult(Map<String, ValidationResult> resultsMap) {
		results.putAll(resultsMap);
	}

	public void addResult(ValidationResult validationResult) { results.put(validationResult.getFieldName(), validationResult); }

	public boolean hasResult(String name) {
		return results.containsKey(name);
	}

	public ValidationResult getResult(String name) {
		return results.get(name);
	}

	public boolean hasErrors() {
		return results.values().stream()
			.anyMatch(ValidationResult::isNotValid);
	}

	public boolean isError(String name) {
		if (hasResult(name)) {
			return getResult(name).isNotValid();
		}
		return false;
	}

	public String errorCode(String name) {
		if (isError(name)) {
			return results.get(name).getErrorCode().getCode();
		}
		return "";
	}
}
