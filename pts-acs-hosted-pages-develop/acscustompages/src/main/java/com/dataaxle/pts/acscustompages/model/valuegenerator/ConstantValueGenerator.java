package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Value
public class ConstantValueGenerator<T> extends BaseValueGenerator<T> implements Serializable {

	private static final long serialVersionUID = 228640304470075640L;

	T value;

	private ConstantValueGenerator(Builder<T> builder) {
		super(builder);
		this.value = builder.value;
	}

	@Override
	public T getValue(CustomResourceResponse source) {
		return value;
	}

	@Override
	public T getValue(DynamicFormBean source) {
		return value;
	}

	@Override
	public T getValue(ProfileResponse source) {
		return value;
	}

	public T getValue() {
		return value;
	}

	public static class Builder<R> extends ValueGenerator.Builder<Builder<R>> {
		R value;

		public Builder<R> value(R value) {
			this.value = value;
			return self();
		}

		@Override
		public ValueGenerator<?> build() {
			this.actsUpon = ActsUpon.CONSTANT;
			return new ConstantValueGenerator<>(this);
		}

		@Override
		protected Builder<R> self() {
			return this;
		}
	}
}
