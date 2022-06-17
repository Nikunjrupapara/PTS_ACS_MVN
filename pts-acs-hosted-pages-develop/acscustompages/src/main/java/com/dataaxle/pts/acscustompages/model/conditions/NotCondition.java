package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Value
@Slf4j
public class NotCondition extends BaseCondition implements Serializable {

	private static final long serialVersionUID = -7412973430624721379L;

	Condition condition;

	public NotCondition(Condition condition) {
		super(String.format("Not %s", condition.getName()), condition.getActsUpon(), 0, -1);
		this.condition = condition;
	}

	public NotCondition(Condition condition, int insertionOrder) {
		super(String.format("Not %s", condition.getName()), condition.getActsUpon(), 0, insertionOrder);
		this.condition = condition;
	}

	@Override
	public boolean evaluate(ProfileResponse value) {
		LOG.debug("Evaluating NotCondition: {}", getName());
		return !condition.evaluate(value);
	}

	@Override
	public boolean evaluate(DynamicFormBean value) {
		LOG.debug("Evaluating NotCondition: {}", getName());
		return !condition.evaluate(value);
	}

	@Override
	public boolean evaluate(CustomResourceResponse value) {
		LOG.debug("Evaluating NotCondition: {}", getName());
		return !condition.evaluate(value);
	}

	@Override
	public int getProcessingOrder() {
		if (super.getProcessingOrder() < 0) {
			return condition.getProcessingOrder();
		}
		return super.getProcessingOrder();
	}
}
