package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;
import java.util.Arrays;

@EqualsAndHashCode(callSuper = true)
@Value
public class OffsetNumberValueGenerator<T extends Number> extends BaseValueGenerator<T> implements Serializable {

	private static final long serialVersionUID = 6460139794882159800L;

	ValueGenerator<T> leftSide;

	ValueGenerator<T> rightSide;

	Operator operator;

	private OffsetNumberValueGenerator(Builder<T> builder) {
		super(builder);
		this.leftSide = builder.leftSide;
		this.rightSide = builder.rightSide;
		this.operator = builder.operator;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getValue(DynamicFormBean source) {
		T leftVal = leftSide.getValue(source);
		T rightVal = rightSide.getValue(source);
		return (T)operator.calculate(leftVal.intValue(), rightVal.intValue());
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getValue(ProfileResponse source) {
		Number leftVal = leftSide.getValue(source);
		Number rightVal = rightSide.getValue(source);
		return (T) operator.calculate(leftVal.intValue(), rightVal.intValue());
	}

	@Override
	public T getValue() {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getValue(CustomResourceResponse source) {
		Number leftVal = leftSide.getValue(source);
		Number rightVal = rightSide.getValue(source);
		return (T)operator.calculate(leftVal.intValue(), rightVal.intValue());
	}

	public static class Builder<T> extends ValueGenerator.Builder<Builder<T>> {

		ValueGenerator<T> leftSide;

		ValueGenerator<T> rightSide;

		Operator operator;

		public Builder<T> leftSide(ValueGenerator<T> leftSide) {
			this.leftSide = leftSide;
			return self();
		}

		public Builder<T> rightSide(ValueGenerator<T> rightSide) {
			this.rightSide = rightSide;
			return self();
		}

		public Builder<T> operator(Operator operator) {
			this.operator = operator;
			return self();
		}

		@Override
		public ValueGenerator<?> build() {
			// NB In V2 this may need to be changed to be CONSTANT
			this.actsUpon = ActsUpon.CUSTOM_RESOURCE;
			return new OffsetNumberValueGenerator(this);
		}

		@Override
		protected Builder<T> self() {
			return this;
		}
	}

	public interface Calculate {

		// Java does not properly support mathematical operations using Generics so the best option seems to be
		// to use overloads for the different numeric types.  Add additional overloads as required.
		Integer calculate(Integer leftVal, Integer rightVal);

	}

	public enum Operator implements Calculate {
		ADD(Operator.ADD_NM) {
			@Override
			public Integer calculate(Integer leftVal, Integer rightVal) { return leftVal + rightVal; }
		},
		DIVIDE(Operator.DIVIDE_NM) {
			@Override
			public Integer calculate(Integer leftVal, Integer rightVal) { return Math.abs(leftVal / rightVal); }
		},
		MULTIPLY(Operator.MULTIPLY_NM) {
			@Override
			public Integer calculate(Integer leftVal, Integer rightVal) { return leftVal * rightVal; }
		},
		SUBTRACT(Operator.SUBTRACT_NM) {
			@Override
			public Integer calculate(Integer leftVal, Integer rightVal) { return leftVal - rightVal; }
		};

		private final String name;

		Operator(String name) {
		this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

		public static Operator of(String nameStr) {
			return Arrays.stream(values())
				.filter(val -> val.name.equals(nameStr))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(String.format("Unknown operator '%s'", nameStr)));
		}

		public static final String ADD_NM = "add";

		public static final String DIVIDE_NM = "divide";

		public static final String MULTIPLY_NM = "multiply";

		public static final String SUBTRACT_NM = "subtract";
	}
}
