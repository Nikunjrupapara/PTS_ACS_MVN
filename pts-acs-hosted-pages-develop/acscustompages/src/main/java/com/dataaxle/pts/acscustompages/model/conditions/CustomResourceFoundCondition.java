package com.dataaxle.pts.acscustompages.model.conditions;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.PKEY;

import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CustomResourceFoundCondition extends BaseCondition implements Serializable {

	private static final long serialVersionUID = 5591627521457647214L;

	public CustomResourceFoundCondition(String name, int insertionOrder) {
		super(name, ActsUpon.CUSTOM_RESOURCE, 0, insertionOrder);
	}

	public boolean evaluate(DynamicFormBean value) {
		return false;
	}

	public boolean evaluate(ProfileResponse value) {
		return false;
	}

	@Override
	public boolean evaluate(CustomResourceResponse value) {
		LOG.debug("Evaluating CustomResourceFoundCondition: {}", getName());
		if (value == null) {
			return false;
		}
		String pKey = (String) value.getField(PKEY);
		return StringUtils.hasText(pKey);
	}
}
