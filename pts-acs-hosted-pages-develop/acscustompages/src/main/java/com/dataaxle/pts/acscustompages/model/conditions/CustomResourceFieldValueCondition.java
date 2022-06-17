package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Value
@Slf4j
public class CustomResourceFieldValueCondition extends BaseCondition implements Serializable {

	private static final long serialVersionUID = 707762047635500477L;

	String fieldName;

	boolean matchEmptyValue;

	List<String> expectedValue;

	public CustomResourceFieldValueCondition(String name, int insertionOrder, String fieldName, boolean matchEmptyValue, List<String> expectedValues) {
		super(name, ActsUpon.CUSTOM_RESOURCE, 0, insertionOrder);
		this.fieldName = fieldName;
		this.matchEmptyValue = matchEmptyValue;
		this.expectedValue = expectedValues;
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		return false;
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		return false;
	}

	@Override
	public boolean evaluate(CustomResourceResponse value) {
		LOG.debug("Evaluating CustomResourceFieldValueCondition: {}", getName());
		if (value == null) {
			return false;
		}
		String fieldValue = (String) value.getField(fieldName);
		if (StringUtils.isEmpty(fieldValue)) {
			return matchEmptyValue;
		}
		return expectedValue.contains(fieldValue);
	}
}
