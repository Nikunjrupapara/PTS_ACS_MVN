package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.AdobeDataType;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Value
public class CurrentDateValueGenerator extends BaseValueGenerator<LocalDate> implements Serializable {

	private static final long serialVersionUID = -6142369045755829167L;

	private CurrentDateValueGenerator(Builder builder) {
		super(builder);
	}

	@Override
	public LocalDate getValue(DynamicFormBean source) {
		return null;
	}

	@Override
	public LocalDate getValue(ProfileResponse source) {
		return null;
	}

	@Override
	public LocalDate getValue() {
		return LocalDate.now();
	}

	public static class Builder extends ValueGenerator.Builder<Builder> {

		@Override
		public ValueGenerator<?> build() {
			this.actsUpon = ActsUpon.CONSTANT;
			this.acsDataType = AdobeDataType.DATE;
			return new CurrentDateValueGenerator(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
	}
}
