package com.dataaxle.pts.acscustompages.model;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Value
@NoArgsConstructor
public class ServicesConfig implements Serializable {

	private static final long serialVersionUID = 1979236253787684708L;

	List<HiddenService> hiddenServices = new LinkedList<>();

	List<ServiceField<?>> serviceFields = new LinkedList<>();

	List<ServiceProfileField<?>> serviceProfileFields = new LinkedList<>();

	public ServicesConfig addHiddenService(HiddenService hiddenService) {
		hiddenServices.add(hiddenService);
		return this;
	}

	public ServicesConfig addServiceField(ServiceField<?> serviceField) {
		serviceFields.add(serviceField);
		return this;
	}

	public ServicesConfig addServiceProfileField(ServiceProfileField<?> serviceProfileField) {
		serviceProfileFields.add(serviceProfileField);
		return this;
	}
}
