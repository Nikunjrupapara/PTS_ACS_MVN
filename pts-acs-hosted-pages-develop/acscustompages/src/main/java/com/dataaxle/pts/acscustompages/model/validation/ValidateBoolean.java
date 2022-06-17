package com.dataaxle.pts.acscustompages.model.validation;

import com.dataaxle.pts.acscustompages.model.form.BooleanValueTypeMapper;
import com.dataaxle.pts.acscustompages.model.form.ValueTypeMapper;
import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;

import java.io.Serializable;
import java.util.Map;

public class ValidateBoolean extends AbstractValidator implements Serializable {

	private static final long serialVersionUID = -4814849809912312632L;

	ValueTypeMapper<Boolean> valueTypeMapper = new BooleanValueTypeMapper();

	public ValidateBoolean(String fieldName, boolean required) {
		super(fieldName, required, 0, 0);
	}

	@Override
	public ValidationResult validate(Map<String, Object> values) {
		Boolean value = valueTypeMapper.mapFromForm(values.get(fieldName));
		if (required && !value) {
			return getResult(value.toString(), ErrorCode.REQUIRED);
		}
		return getResult(value.toString(), ErrorCode.VALID);
	}
}
