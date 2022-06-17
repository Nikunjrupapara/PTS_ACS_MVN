package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.AdobeDataType;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.Value;

import java.io.Serializable;

@Deprecated
// TODO: will be superseded by ProfileValueGenerator
public abstract class StringValueGenerator extends BaseValueGenerator<String> implements Serializable {

	private static final long serialVersionUID = -3257001783013076681L;

	ActsUpon actsUpon = ActsUpon.CONSTANT;

	String fieldName;

	protected StringValueGenerator(String acsFieldName, AdobeDataType acsDataType, ActsUpon actsUpon) {
		super(acsFieldName, acsDataType, actsUpon);
	}

	@Override
	public ActsUpon getActsUpon() {
		return null;
	}

	@Override
	public String getValue(DynamicFormBean dynamicFormBean) {
		return dynamicFormBean.getItem(fieldName);
	}

	@Override
	public String getValue(ProfileResponse source) {
		return null;
	}

	@Override
	public String getValue() {
		return null;
	}
}
