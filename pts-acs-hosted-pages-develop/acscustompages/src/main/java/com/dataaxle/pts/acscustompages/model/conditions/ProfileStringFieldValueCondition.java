package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ProfileStringFieldValueCondition extends BaseCondition implements Condition {

	private static final long serialVersionUID = -6885658176869694186L;

	String fieldName;

	List<String> expectedValues;

	boolean caseSensitive;

	boolean matchEmptyString;

	public ProfileStringFieldValueCondition(String name, int insertionOrder, String fieldName, List<String> expectedValues) {
		super(name, ActsUpon.PROFILE, 0, insertionOrder);
		this.expectedValues = expectedValues;
		this.fieldName = fieldName;
		this.caseSensitive = true;
		this.matchEmptyString = false;
	}

	public ProfileStringFieldValueCondition(String name, int insertionOrder, String fieldName, List<String> expectedValues,
											   boolean caseSensitive) {
		super(name, ActsUpon.PROFILE, 0, insertionOrder);
		this.expectedValues = expectedValues;
		this.fieldName = fieldName;
		this.caseSensitive = caseSensitive;
		this.matchEmptyString = false;
	}

	public ProfileStringFieldValueCondition(String name, int insertionOrder, String fieldName, List<String> expectedValues,
											boolean caseSensitive, boolean matchEmptyString) {
		super(name, ActsUpon.PROFILE, 0, insertionOrder);
		this.expectedValues = expectedValues;
		this.fieldName = fieldName;
		this.caseSensitive = caseSensitive;
		this.matchEmptyString = matchEmptyString;
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		return false;
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		LOG.debug("Evaluating ProfileStringFieldValueCondition: {}", getName());
		String fieldValue = (String)value.getField(fieldName);
		if (StringUtils.isEmpty(fieldValue)) {
			return matchEmptyString;
		}
		if (caseSensitive) {
			return expectedValues.contains(fieldValue);
		}
		return expectedValues.stream().anyMatch(fieldValue::equalsIgnoreCase);
	}
}
