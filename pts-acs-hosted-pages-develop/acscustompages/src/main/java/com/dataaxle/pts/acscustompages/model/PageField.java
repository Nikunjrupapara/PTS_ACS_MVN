package com.dataaxle.pts.acscustompages.model;

import com.dataaxle.pts.acscustompages.model.form.ValueType;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;
import com.dataaxle.pts.acscustompages.model.validation.ValidationRule;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Map;

@Value
@AllArgsConstructor
public class PageField<T> implements Serializable {

	private static final long serialVersionUID = 5093563813698687405L;

	String name;

	boolean hidden;

	boolean submit;

	T defaultValue;

	ValidationRule validationRule;

	ValueType valueType;

	public boolean isValidated() {
		return validationRule != null;
	}
}
