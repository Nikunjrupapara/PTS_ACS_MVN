package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Value
public class HasServiceCondition extends BaseCondition implements Serializable {

	private static final long serialVersionUID = -5523019750781950372L;

	String service;

	public HasServiceCondition(String name, int insertionOrder, String service) {
		super(name, ActsUpon.PROFILE, 0, insertionOrder);
		this.service = service;
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		LOG.debug("Evaluating HasServiceCondition: {}", getName());
		return value.hasCurrentService(service);
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		return false;
	}
}
