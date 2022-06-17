package com.dataaxle.pts.acscustompages.model;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SingleServiceProfileField<T> extends AbstractServiceProfileField<T> implements Serializable {

	private static final long serialVersionUID = -4167780217204084555L;

	String serviceName;

	public SingleServiceProfileField(String fieldName, String formOnValue, String formOffValue, String profileName,
									 T acsOnValue, T acsOffValue, String serviceName) {
		super(fieldName, formOnValue, formOffValue, profileName, acsOnValue, acsOffValue);
		this.serviceName = serviceName;
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
			return Map.of(serviceName, ServiceAction.REMOVE);
		}
		if (formOnValue.equals(fieldValue)) {
			return Map.of(serviceName, ServiceAction.ADD);
		}
		if (formOffValue != null && formOffValue.equals(fieldValue)) {
			return Map.of(serviceName, ServiceAction.REMOVE);
		}
		throw new IllegalArgumentException(String.format("field: %s, value: %s, expected %s or %s", fieldName,
			fieldValue, formOnValue, formOffValue));
	}
}
