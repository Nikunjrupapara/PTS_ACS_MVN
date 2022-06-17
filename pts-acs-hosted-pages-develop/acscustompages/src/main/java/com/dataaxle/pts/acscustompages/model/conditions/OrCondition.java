package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrCondition extends BaseCondition implements Serializable {

	private static final long serialVersionUID = -3514542322156091970L;

	List<Condition> conditions = new ArrayList<>();

	public OrCondition(String name, int insertionOrder, Condition... conditions) {
		super(name, ActsUpon.MULTIPLE, 0, insertionOrder);
		this.conditions.addAll(Arrays.asList(conditions));
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		return conditions.stream()
				   .sorted(Comparator.comparingInt(Condition::getProcessingOrder))
				   .map(cond -> cond.evaluate(value))
				   .reduce(Boolean::logicalOr)
				   .orElse(false);
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		return conditions.stream()
				   .sorted(Comparator.comparingInt(Condition::getProcessingOrder))
				   .map(cond -> cond.evaluate(value))
				   .reduce(Boolean::logicalOr)
				   .orElse(false);
	}

	@Override
	public boolean evaluate(ProfileResponse profileResponse, DynamicFormBean dynamicFormBean) {
		return conditions.stream()
				   .sorted(Comparator.comparingInt(Condition::getProcessingOrder))
				   .map(cond -> {
				   		switch(cond.getActsUpon()) {
							case FORM_DATA:
								return cond.evaluate(dynamicFormBean);
							case PROFILE:
								return cond.evaluate(profileResponse);
							default:
								return false;
						}
				   })
				   .reduce(Boolean::logicalOr)
				   .orElse(false);
	}
}
