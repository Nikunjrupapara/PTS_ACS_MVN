package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Slf4j
public class ProfileFoundCondition extends BaseCondition implements Serializable {

	private static final long serialVersionUID = 5591627521457647214L;

	public ProfileFoundCondition(String name, int insertionOrder) {
		super(name, Condition.ActsUpon.PROFILE, 0, insertionOrder);
	}

	public boolean evaluate(DynamicFormBean value) {
		return false;
	}

	public boolean evaluate(ProfileResponse value) {
		LOG.debug("Evaluating ProfileFoundCondition: {}", getName());
		if (value == null) {
			return false;
		}
		return StringUtils.hasText(value.getUniqueId());
	}
}
