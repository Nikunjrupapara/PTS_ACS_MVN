package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Value
@Slf4j
public class AlwaysTrueCondition extends BaseCondition implements Serializable {

	private static final long serialVersionUID = 3489127427681522406L;

	public AlwaysTrueCondition(String name, int insertionOrder) {
		super(name, ActsUpon.NONE, 0, insertionOrder);
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		LOG.debug("Evaluating AlwaysTrueCondition: {}", getName());
		return true;
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		LOG.debug("Evaluating AlwaysTrueCondition: {}", getName());
		return true;
	}

	@Override
	public boolean evaluate() {
		LOG.debug("Evaluating AlwaysTrueCondition: {}", getName());
		return true;
	}
}
