package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class HasEverHadServiceCondition extends BaseCondition implements Serializable {

	private static final long serialVersionUID = 1840564135995452166L;

	private static final String PROFILE_PREFIX = "cusEverSub";

	String serviceName;

	String profileFieldName;

	public HasEverHadServiceCondition(String name, int insertionOrder, String serviceName) {
		super(name, ActsUpon.PROFILE, 0, insertionOrder);
		this.serviceName = serviceName;
		this.profileFieldName = String.format("%s%s", PROFILE_PREFIX, StringUtils.capitalize(serviceName));
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		return false;
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		LOG.debug("Evaluating HasEverHadServiceCondition: {}", getName());
		if (!value.hasField(profileFieldName)) {
			throw new IllegalStateException(String.format("Service membership flag '%s' not found in profile!", profileFieldName));
		}
		try {
			return (boolean) value.getField(profileFieldName);
		} catch(ClassCastException e) {
			throw new IllegalStateException(String.format("Service membership flag '%s' should be boolean!", profileFieldName));
		}
	}
}
