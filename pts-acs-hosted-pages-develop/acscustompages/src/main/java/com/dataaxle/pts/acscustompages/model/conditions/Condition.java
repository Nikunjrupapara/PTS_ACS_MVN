package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;

public interface Condition {

	String getName();

	boolean evaluate(DynamicFormBean value);

	boolean evaluate(ProfileResponse value);

	boolean evaluate(ProfileResponse profileResponse, DynamicFormBean dynamicFormBean);

	boolean evaluate(CustomResourceResponse value);

	boolean evaluate();

	int getProcessingOrder();

	ActsUpon getActsUpon();

	enum ActsUpon {
		CUSTOM_RESOURCE,
		FORM_DATA,
		MULTIPLE,
		NONE,
		PROFILE
	}
}
