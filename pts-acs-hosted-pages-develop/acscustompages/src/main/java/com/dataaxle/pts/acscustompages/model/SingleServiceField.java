package com.dataaxle.pts.acscustompages.model;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SingleServiceField<T> extends AbstractServiceField<T> implements Serializable {

	private static final long serialVersionUID = -6609039128986275399L;

	private final String serviceName;

	private final boolean reverseAction;

	public SingleServiceField(String fieldName, String formOnValue, String formOffValue, String serviceName) {
		super(fieldName, formOnValue, formOffValue);
		this.serviceName = serviceName;
		this.reverseAction = false;
	}

	public SingleServiceField(String fieldName, String formOnValue, String formOffValue, String serviceName, boolean ignoreWhenEmpty) {
		super(fieldName, formOnValue, formOffValue, ignoreWhenEmpty);
		this.serviceName = serviceName;
		this.reverseAction = false;
	}

	public SingleServiceField(String fieldName, String formOnValue, String formOffValue, String serviceName,
							  boolean ignoreWhenEmpty, boolean reverseAction) {
		super(fieldName, formOnValue, formOffValue, ignoreWhenEmpty);
		this.serviceName = serviceName;
		this.reverseAction = reverseAction;
	}

	@Override
	public boolean isSingleService() {
		return true;
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public List<String> getServiceNames() {
		return Collections.singletonList(serviceName);
	}

	@Override
	public Map<String, ServiceAction> getServiceActions(String fieldValue) {
		if (StringUtils.isEmpty(fieldValue)) {
			return Map.of(serviceName, reverseAction ? ServiceAction.ADD : ServiceAction.REMOVE);
		}
		if (formOnValue.equals(fieldValue)) {
			return Map.of(serviceName, reverseAction ? ServiceAction.REMOVE : ServiceAction.ADD);
		}
		if (formOffValue != null && formOffValue.equals(fieldValue)) {
			return Map.of(serviceName, reverseAction ? ServiceAction.ADD : ServiceAction.REMOVE);
		}
		throw new IllegalArgumentException(String.format("field: %s, value: %s, expected %s or %s", fieldName,
			fieldValue, formOnValue, formOffValue));
	}

	@Override
	public boolean isReverseAction() {
		return false;
	}
}
