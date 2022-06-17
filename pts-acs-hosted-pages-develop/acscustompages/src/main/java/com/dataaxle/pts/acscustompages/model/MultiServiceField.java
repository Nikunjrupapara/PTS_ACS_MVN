package com.dataaxle.pts.acscustompages.model;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiServiceField<T> extends AbstractServiceField<T> implements Serializable {

	private final List<String> addServices;

	private final List<String> removeServices;

	public MultiServiceField(String fieldName, String formOnValue, String formOffValue, List<String> addServices,
							 List<String> removeServices) {
		super(fieldName, formOnValue, formOffValue);
		this.addServices = addServices;
		this.removeServices = removeServices;
	}

	@Override
	public boolean isSingleService() {
		return false;
	}

	@Override
	public String getServiceName() {
		return null;
	}

	@Override
	public List<String> getServiceNames() {
		return Stream.concat(addServices.stream(), removeServices.stream()).collect(Collectors.toList());
	}

	@Override
	public Map<String, ServiceAction> getServiceActions(String fieldValue) {
		if (StringUtils.hasText(fieldValue) && fieldValue.equals(formOnValue)) {
			Map<String, ServiceAction> actions = new HashMap<>();
			addServices.forEach(service -> actions.put(service, ServiceAction.ADD));
			removeServices.forEach(service -> actions.put(service, ServiceAction.REMOVE));
			return actions;
		}
		if (StringUtils.isEmpty(fieldValue) || (formOffValue != null && formOffValue.equals(fieldValue))) {
			return Collections.emptyMap();
		}
		throw new IllegalArgumentException(String.format("field: %s, value: %s, expected %s or %s", fieldName,
			fieldValue, formOnValue, formOffValue));
	}

}
