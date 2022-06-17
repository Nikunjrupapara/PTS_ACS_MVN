package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.AdobeDataType;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@EqualsAndHashCode(callSuper = true)
@Value
public class CurrentDateTimeValueGenerator extends BaseValueGenerator<LocalDateTime> implements Serializable {

	private static final long serialVersionUID = -6142369045755829167L;

	String zoneId;

	private CurrentDateTimeValueGenerator(Builder builder) {
		super(builder);
		this.zoneId = builder.zoneId;
	}

	@Override
	public LocalDateTime getValue(DynamicFormBean source) {
		return null;
	}

	@Override
	public LocalDateTime getValue(ProfileResponse source) {
		return null;
	}

	@Override
	public LocalDateTime getValue() {
		return LocalDateTime.now(ZoneId.of(zoneId));
	}

	public static class Builder extends ValueGenerator.Builder<Builder> {

		String zoneId;

		public Builder zoneId(String zoneId) {
			this.zoneId = zoneId;
			return self();
		}

		@Override
		public ValueGenerator<?> build() {
			this.actsUpon = ActsUpon.CONSTANT;
			this.acsDataType = AdobeDataType.DATETIME;
			if (StringUtils.isEmpty(this.zoneId)) {
				this.zoneId = "UTC";
			}
			return new CurrentDateTimeValueGenerator(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
	}
}
