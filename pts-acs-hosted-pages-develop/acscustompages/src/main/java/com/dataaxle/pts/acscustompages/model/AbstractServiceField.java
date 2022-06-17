package com.dataaxle.pts.acscustompages.model;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstractServiceField<T> implements ServiceField<T>, Serializable {

	private static final long serialVersionUID = -7316644793996300014L;

	protected final String fieldName;

	// The value sent by the form when the form control is checked
	protected final String formOnValue;

	// The value sent by the form when the form control is not checked
	protected final String formOffValue;

	protected final boolean ignoreWhenEmpty;

	protected AbstractServiceField(String fieldName, String formOnValue, String formOffValue) {
		this.fieldName = fieldName;
		this.formOnValue = formOnValue;
		this.formOffValue = formOffValue;
		this.ignoreWhenEmpty = false;
	}

	protected AbstractServiceField(String fieldName, String formOnValue, String formOffValue, boolean ignoreWhenEmpty) {
		this.fieldName = fieldName;
		this.formOnValue = formOnValue;
		this.formOffValue = formOffValue;
		this.ignoreWhenEmpty = ignoreWhenEmpty;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public abstract Map<String, ServiceAction> getServiceActions(String fieldValue);

	@Override
	public String getFormValue(Object acsValue) {
		String tmpValue = "";
		if (!StringUtils.isEmpty(acsValue)) {
			tmpValue = acsValue.toString();
		}
		if (tmpValue.equals(formOffValue)) {
			return formOffValue;
		}
		if (tmpValue.equals(formOnValue)) {
			return formOnValue;
		}
		throw new IllegalArgumentException(String.format("field: %s, value: %s, expected %s or %s", fieldName,
			tmpValue, formOnValue, formOffValue));
	}

	@Override
	public String getFormOnValue() {
		return formOnValue;
	}

	@Override
	public String getFormOffValue() {
		return formOffValue;
	}

	@Override
	public boolean isIgnoreWhenEmpty() {
		return ignoreWhenEmpty;
	}

	@Override
	public boolean isReverseAction() {
		return false;
	}
}
