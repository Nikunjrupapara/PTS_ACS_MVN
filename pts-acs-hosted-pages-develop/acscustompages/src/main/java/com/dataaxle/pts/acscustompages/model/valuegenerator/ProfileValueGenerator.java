package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.AdobeDataType;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Value
public class ProfileValueGenerator<R> extends BaseValueGenerator<R> implements Serializable {

	private static final long serialVersionUID = -2634063376827253785L;

	String profileFieldName;

	@Deprecated
	ProfileValueGenerator(String acsFieldName, AdobeDataType acsDataType, String profileFieldName) {
		super(acsFieldName, acsDataType, ActsUpon.PROFILE);
		this.profileFieldName = profileFieldName;
	}

	private ProfileValueGenerator(Builder builder) {
		super(builder);
		this.profileFieldName = builder.profileFieldName;
	}

	@Override
	public R getValue(DynamicFormBean source) {
		return mapToAcs(source.getItem(profileFieldName));
	}

	@Override
	public R getValue(ProfileResponse source) {
		return mapToAcs(source.getField(profileFieldName));
	}

	@Override
	public R getValue() {
		return null;
	}

	public static class Builder extends ValueGenerator.Builder<Builder> {

		String profileFieldName;

		public Builder profileFieldName(String profileFieldName) {
			this.profileFieldName = profileFieldName;
			return self();
		}

		@Override
		public ValueGenerator<?> build() {
			this.actsUpon = ActsUpon.PROFILE;
			return new ProfileValueGenerator<>(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
	}
}
