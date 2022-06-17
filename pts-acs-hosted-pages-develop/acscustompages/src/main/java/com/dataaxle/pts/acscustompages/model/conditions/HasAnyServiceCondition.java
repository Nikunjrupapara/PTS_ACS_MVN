package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Value
@Slf4j
public class HasAnyServiceCondition extends BaseCondition implements Serializable {
	private static final long serialVersionUID = 2494331537408198695L;

	List<String> services;

	HasAnyServiceCondition(String name, int insertionOrder, List<String> services) {
		super(name, ActsUpon.PROFILE, 0, insertionOrder);
		this.services = services;
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		return false;
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		LOG.debug("Evaluating HasAnyServiceCondition: {}", getName());
		return services.stream()
			.map(value::hasCurrentService)
			.reduce(Boolean::logicalOr).orElse(false);
	}
}
