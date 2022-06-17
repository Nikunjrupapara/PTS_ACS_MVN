package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.AdobeDataType;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;
@EqualsAndHashCode(callSuper = true)
@Value
public class FormFieldValueGenerator<R> extends BaseValueGenerator<R> implements Serializable {

	private static final long serialVersionUID = 4414519081543172542L;

	String fieldName;

	@Deprecated
	FormFieldValueGenerator(String acsFieldName, String fieldName, AdobeDataType acsDataType) {
		super(acsFieldName, acsDataType, ActsUpon.FORM_DATA);
		this.fieldName = fieldName;
	}

	private FormFieldValueGenerator(Builder builder) {
		super(builder);
		this.fieldName = builder.fieldName;
	}

	public R getValue(DynamicFormBean source) {
		String value = source.getItem(fieldName);
		if(StringUtils.isEmpty(value)){
			value = "";
		}
		return mapToAcs(value);
	}

	@Override
	public R getValue(ProfileResponse source) {
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
			this.actsUpon = ActsUpon.FORM_DATA;
			return new FormFieldValueGenerator<>(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
	}
}
