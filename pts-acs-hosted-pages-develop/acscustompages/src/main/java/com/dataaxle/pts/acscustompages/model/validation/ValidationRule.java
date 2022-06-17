package com.dataaxle.pts.acscustompages.model.validation;

import java.util.Map;

public interface ValidationRule {

	ValidationResult validate(Map<String, Object> values);

	//T getValue(String name);
}
