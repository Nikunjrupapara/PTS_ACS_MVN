package com.dataaxle.pts.acscustompages.model.form;

public interface ValueTypeMapper<T> {

	T mapFromForm(Object value);

	Object mapToForm(T value);

	static ValueTypeMapper<?> getInstance(ValueType valueType) {
		switch (valueType) {
			case BOOLEAN:
				return new BooleanValueTypeMapper();
			case STRING:
				return new StringValueTypeMapper();
		}
		return new StringValueTypeMapper();
	}
}
