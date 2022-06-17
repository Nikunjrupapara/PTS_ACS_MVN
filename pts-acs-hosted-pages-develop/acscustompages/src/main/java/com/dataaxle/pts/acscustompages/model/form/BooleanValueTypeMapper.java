package com.dataaxle.pts.acscustompages.model.form;

import java.io.Serializable;

public class BooleanValueTypeMapper implements ValueTypeMapper<Boolean>, Serializable {

	private static final long serialVersionUID = 768159294984081454L;

	@Override
	public Boolean mapFromForm(Object value) {
		return value == null ? Boolean.FALSE : Boolean.TRUE;
	}

	@Override
	public Object mapToForm(Boolean value) {
		if (value) {
			return "";
		}
		return null;
	}
}
