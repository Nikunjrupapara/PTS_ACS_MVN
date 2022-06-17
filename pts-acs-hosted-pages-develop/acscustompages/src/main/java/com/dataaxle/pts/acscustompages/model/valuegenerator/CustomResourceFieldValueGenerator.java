package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Value
public class CustomResourceFieldValueGenerator<R> extends BaseValueGenerator<R> implements Serializable {

	private static final long serialVersionUID = -2634063376827253785L;

	String fieldName;

	private CustomResourceFieldValueGenerator(Builder builder) {
		super(builder);
		this.fieldName = builder.fieldName;
	}

	@Override
	public R getValue(DynamicFormBean source) {
		return mapToAcs(source.getItem(fieldName));
	}

	@Override
	public R getValue(ProfileResponse source) {
		return mapToAcs(source.getField(fieldName));
	}

	@Override
	public R getValue(CustomResourceResponse source) {
		return mapToAcs(source.getField(fieldName));
	}

	@Override
	public R getValue() {
		return null;
	}

	public static class Builder extends ValueGenerator.Builder<Builder> {

		String fieldName;

		public Builder fieldName(String fieldName) {
			this.fieldName = fieldName;
			return self();
		}

		@Override
		public ValueGenerator<?> build() {
			this.actsUpon = ActsUpon.CUSTOM_RESOURCE;
			return new CustomResourceFieldValueGenerator<>(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
	}
}
