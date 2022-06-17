package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.ToString;

import java.io.Serializable;

@ToString
public abstract class BaseCondition implements Condition, Serializable {

	private static final long serialVersionUID = -5588465571210894926L;

	private final String name;

	private final ActsUpon actsUpon;

	private final int processingOrder;

	private final int insertionOrder;

	protected BaseCondition(String name, ActsUpon actsUpon, int processingOrder, int insertionOrder) {
		this.name = name;
		this.actsUpon = actsUpon;
		this.processingOrder = processingOrder;
		this.insertionOrder = insertionOrder;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean evaluate() {
		return false;
	}

	@Override
	public boolean evaluate(ProfileResponse profileResponse, DynamicFormBean dynamicFormBean) {
		return false;
	}

	@Override
	public ActsUpon getActsUpon() {
		return actsUpon;
	}

	@Override
	public boolean evaluate(CustomResourceResponse value) {
		return false;
	}

	@Override
	public int getProcessingOrder() {
		return processingOrder + insertionOrder;
	}
}
