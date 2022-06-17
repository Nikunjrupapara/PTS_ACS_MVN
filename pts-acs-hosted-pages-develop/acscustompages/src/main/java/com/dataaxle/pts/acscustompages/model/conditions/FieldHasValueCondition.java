package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class FieldHasValueCondition extends BaseCondition implements Serializable {
	/**
	 * This condition takes a ValueGenerator as its parameter and returns true if the supplied ValueGenerator yields
	 * a value, or returns false.  This is added to provide a way to test if a form field contains a value or not as the
	 * FormFieldValueCondition always returns false for an empty string so can not be used to test if a field is empty.
	 */

	private static final long serialVersionUID = -1871810551055918368L;

	ValueGenerator<?> valueGenerator;

	public FieldHasValueCondition(String name, int insertionOrder, ActsUpon actsUpon, ValueGenerator<?> valueGenerator) {
		super(name, actsUpon, 0, insertionOrder);
		this.valueGenerator = valueGenerator;
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		return StringUtils.hasText(valueGenerator.getValue(value).toString());
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		return StringUtils.hasText(valueGenerator.getValue(value).toString());
	}
}
