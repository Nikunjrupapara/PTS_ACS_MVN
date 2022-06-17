package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Value
public class OffsetDateValueGenerator extends BaseValueGenerator<LocalDateTime> implements Serializable {

	private static final long serialVersionUID = 7134244261124096069L;

	ValueGenerator<?> startDate;

	Long years;

	Long months;

	Long days;

	Long hours;

	Long minutes;

	Long seconds;

	private OffsetDateValueGenerator(Builder builder) {
		super(builder);
		this.startDate = builder.startDate;
		this.years = builder.years;
		this.months = builder.months;
		this.days = builder.days;
		this.hours = builder.hours;
		this.minutes = builder.minutes;
		this.seconds = builder.seconds;
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
		LocalDateTime ldt = (LocalDateTime)startDate.getValue();
		return applyOffsets(ldt);
	}

	private LocalDateTime applyOffsets(LocalDateTime value) {
		return value
				   .plusYears(years)
				   .plusMonths(months)
				   .plusDays(days)
				   .plusHours(hours)
				   .plusMinutes(minutes)
				   .plusSeconds(seconds);
	}

	public static class Builder extends ValueGenerator.Builder<Builder> {

		private ValueGenerator<?> startDate;

		private Long years = 0L;

		private Long months = 0L;

		private Long days = 0L;

		private Long hours = 0L;

		private Long minutes = 0L;

		private Long seconds = 0L;

		public Builder startDate(ValueGenerator<?> startDate) {
			this.startDate = startDate;
			this.actsUpon = startDate.getActsUpon();
			return self();
		}

		public Builder years(Long years) {
			this.years = years;
			return self();
		}

		public Builder months(Long months) {
			this.months = months;
			return self();
		}

		public Builder days(Long days) {
			this.days = days;
			return self();
		}

		public Builder hours(Long hours) {
			this.hours = hours;
			return self();
		}

		public Builder minutes(Long minutes) {
			this.minutes = minutes;
			return self();
		}

		public Builder seconds(Long seconds) {
			this.seconds = seconds;
			return self();
		}

		@Override
		public ValueGenerator<?> build() {
			return new OffsetDateValueGenerator(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
	}
}
