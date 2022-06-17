package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Value
public class OnOffValueGenerator extends BaseValueGenerator<String> implements Serializable {

	private static final long serialVersionUID = -1281743393576450100L;

	String fieldName;

	String formOnValue;

	String acsOnValue;

	String acsOffValue;

	private OnOffValueGenerator(Builder builder) {
		super(builder);
		this.acsOffValue = builder.acsOffValue;
		this.acsOnValue = builder.acsOnValue;
		this.formOnValue = builder.formOnValue;
		this.fieldName = builder.fieldName;
	}

	@Override
	public ActsUpon getActsUpon() {
		return actsUpon;
	}

	@Override
	public String getValue(DynamicFormBean dynamicFormBean) {
		String fieldValue = dynamicFormBean.getItem(fieldName);
		if (StringUtils.isEmpty(fieldValue)) {
			return acsOffValue;
		}
		if (fieldValue.equals(formOnValue)) {
			return acsOnValue;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public String getValue(ProfileResponse source) {
		return acsOffValue;
	}

	@Override
	public String getValue() {
		return acsOffValue;
	}

	public static class Builder extends ValueGenerator.Builder<Builder> {

		String fieldName;

		String formOnValue;

		String acsOnValue;

		String acsOffValue;

		public Builder actsUpon(String actsUpon) {
			super.actsUpon(actsUpon);
			return self();
		}

		public Builder fieldName(String fieldName) {
			this.fieldName = fieldName;
			return self();
		}

		public Builder formOnValue(String formOnValue) {
			this.formOnValue = formOnValue;
			return self();
		}

		public Builder acsOnValue(String acsOnValue) {
			this.acsOnValue = acsOnValue;
			return self();
		}

		public Builder acsOffValue(String acsOffValue) {
			this.acsOffValue = acsOffValue;
			return self();
		}

		@Override
		public ValueGenerator<?> build() {
			return new OnOffValueGenerator(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
	}
}
