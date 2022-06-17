package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Value
public class BooleanValueGenerator extends BaseValueGenerator<Boolean> implements Serializable {

	private static final long serialVersionUID = -1281743393576450100L;

	String fieldName;

	String trueValue;

	String falseValue;

	private BooleanValueGenerator(Builder builder) {
		super(builder);
		this.falseValue = builder.falseValue;
		this.fieldName = builder.fieldName;
		this.trueValue = builder.trueValue;
	}

	@Override
	public ActsUpon getActsUpon() {
		return actsUpon;
	}

	@Override
	public Boolean getValue(DynamicFormBean dynamicFormBean) {
		String fieldValue = dynamicFormBean.getItem(fieldName);
		if (fieldValue == null) {
			return false;
		}
		if (fieldValue.equals(trueValue)) {
			return true;
		}
		if (fieldValue.equals(falseValue)) {
			return false;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public Boolean getValue(ProfileResponse source) {
		return null;
	}

	@Override
	public Boolean getValue() {
		return null;
	}

	public static class Builder extends ValueGenerator.Builder<Builder> {

		String fieldName;

		String trueValue;

		String falseValue;

		public Builder actsUpon(String actsUpon) {
			super.actsUpon(actsUpon);
			return self();
		}

		public Builder fieldName(String fieldName) {
			this.fieldName = fieldName;
			return self();
		}

		public Builder falseValue(String falseValue) {
			this.falseValue = falseValue;
			return self();
		}

		public Builder trueValue(String trueValue) {
			this.trueValue = trueValue;
			return self();
		}

		@Override
		public ValueGenerator<?> build() {
			return new BooleanValueGenerator(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
	}
}
