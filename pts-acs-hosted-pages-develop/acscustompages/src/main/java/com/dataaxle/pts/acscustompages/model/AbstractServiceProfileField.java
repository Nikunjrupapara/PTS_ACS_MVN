package com.dataaxle.pts.acscustompages.model;

import org.springframework.util.StringUtils;

import java.io.Serializable;

public abstract class AbstractServiceProfileField<T> extends AbstractServiceField<T>
	implements ServiceProfileField<T>, Serializable {

	private static final long serialVersionUID = -6259002353732017678L;

	protected final String profileName;

	// The value sent to/received from ACS when the form control is checked
	protected final T acsOnValue;

	// The value sent to/received from ACS when the form control is not checked
	protected final T acsOffValue;

	protected AbstractServiceProfileField(String fieldName, String formOnValue, String formOffValue,
										  String profileName, T acsOnValue, T acsOffValue) {
		super(fieldName, formOnValue, formOffValue);
		this.acsOffValue = acsOffValue;
		this.acsOnValue = acsOnValue;
		this.profileName = profileName;
	}

	@Override
	public String getProfileName() {
		return profileName;
	}

	@Override
	public T getAcsValue(String fieldValue) {
		if (StringUtils.isEmpty(fieldValue) || (formOffValue != null && formOffValue.equals(fieldValue))) {
			return acsOffValue;
		}
		if (fieldValue.equals(formOnValue)) {
			return acsOnValue;
		}
		throw new IllegalArgumentException(String.format("field: %s, value: '%s', expected '%s' or '%s'", fieldName,
			fieldValue, formOnValue, formOffValue));
	}
}
