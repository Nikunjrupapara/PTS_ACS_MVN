package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.exception.UnsupportedDataTypeException;
import com.dataaxle.pts.acscustompages.model.AdobeDataType;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public interface ValueGenerator<T> {

	String getAcsFieldName();

	AdobeDataType getAcsDataType();

	ActsUpon getActsUpon();

	boolean isIgnoreWhenEmpty();

	T getValue(DynamicFormBean source);

	T getValue(ProfileResponse source);

	T getValue(CustomResourceResponse source);

	T getValue();

	enum ActsUpon {
		CONSTANT("constant"),
		CUSTOM_RESOURCE("customResource"),
		FORM_DATA("formData"),
		PROFILE("profile");

		String name;

		ActsUpon(String name) {
			this.name = name;
		}

		@JsonValue
		public String getName() {
			return name;
		}

		@JsonCreator
		public static ActsUpon fromValue(String value) {
			for (ActsUpon actsUpon : values()) {
				if (actsUpon.getName().equals(value)) {
					return actsUpon;
				}
			}
			return null;
		}
	}

	static ValueGenerator<?> from() {
		return null;
	}

	abstract static class Builder<T extends Builder<T>> {

		protected String acsFieldName;

		protected AdobeDataType acsDataType;

		protected ActsUpon actsUpon;

		protected boolean ignoreWhenEmpty = false;

		public T acsFieldName(String acsFieldName) {
			this.acsFieldName = acsFieldName;
			return self();
		}

		public T adobeDataType(String acsDataType) {
			Optional<AdobeDataType> adobeDataTypeOpt = Optional.ofNullable(AdobeDataType.fromValue(acsDataType));
			this.acsDataType = adobeDataTypeOpt.orElseThrow(() -> new UnsupportedDataTypeException(acsDataType));
			return self();
		}

		protected T actsUpon(String actsUponStr) {
			Optional<ActsUpon> actsUponOpt = Optional.ofNullable(ActsUpon.fromValue(actsUponStr));
			this.actsUpon = actsUponOpt.orElseThrow(() -> new IllegalArgumentException(actsUponStr));
			return self();
		}

		public T ignoreWhenEmpty() {
			this.ignoreWhenEmpty = true;
			return self();
		}

		public abstract ValueGenerator<?> build();

		protected abstract T self();
	}
}
