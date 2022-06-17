package com.dataaxle.pts.acscustompages.model.form;

import java.io.Serializable;

public class StringValueTypeMapper implements ValueTypeMapper<String>, Serializable {

	private static final long serialVersionUID = 5120456237874989506L;

	@Override
	public String mapFromForm(Object value) {
		return (String)value;
	}

	@Override
	public Object mapToForm(String value) {
		return value;
	}
}
