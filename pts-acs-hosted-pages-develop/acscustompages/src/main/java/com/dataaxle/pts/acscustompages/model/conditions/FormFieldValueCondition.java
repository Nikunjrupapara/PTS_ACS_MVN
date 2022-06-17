package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Value
@Slf4j
public class FormFieldValueCondition extends BaseCondition implements Serializable {

	private static final long serialVersionUID = 707762047635500477L;

	String fieldName;

	List<String> expectedValue;

	public FormFieldValueCondition(String name, int insertionOrder, String fieldName, String expectedValue) {
		super(name, ActsUpon.FORM_DATA, 0, insertionOrder);
		this.fieldName = fieldName;
		this.expectedValue = Collections.singletonList(expectedValue);
	}

	public FormFieldValueCondition(String name, int insertionOrder, String fieldName, List<String> expectedValues) {
		super(name, ActsUpon.FORM_DATA, 60, insertionOrder);
		this.fieldName = fieldName;
		this.expectedValue = expectedValues;
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		LOG.debug("Evaluating FormFieldValueCondition: {}", getName());
		String fieldValue = value.getItem(fieldName);
		if (StringUtils.isEmpty(fieldValue)) {
			return false;
		}
		return expectedValue.contains(fieldValue);
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		return false;
	}
}
