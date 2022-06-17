package com.dataaxle.pts.acscustompages.model;

import java.util.List;
import java.util.Map;

public interface ServiceField<T> {

	String getFieldName();

	boolean isSingleService();

	String getServiceName();

	List<String> getServiceNames();

	String getFormOnValue();

	String getFormOffValue();

	Map<String, ServiceAction> getServiceActions(String fieldValue);

	String getFormValue(Object acsValue);

	boolean isIgnoreWhenEmpty();

	boolean isReverseAction();
}
