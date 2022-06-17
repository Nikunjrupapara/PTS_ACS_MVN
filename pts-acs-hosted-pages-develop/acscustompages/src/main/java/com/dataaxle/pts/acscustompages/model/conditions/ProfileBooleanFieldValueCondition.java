package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProfileBooleanFieldValueCondition extends BaseCondition implements Serializable {

	private static final long serialVersionUID = 1800921054689972275L;

	String fieldName;

	public ProfileBooleanFieldValueCondition(String name, int insertionOrder, String fieldName) {
		super(name, ActsUpon.PROFILE, 0, insertionOrder);
		this.fieldName = fieldName;
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		return false;
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		return (boolean) value.getField(fieldName);
	}
}
