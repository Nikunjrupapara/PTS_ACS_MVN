package com.dataaxle.pts.acscustompages.model;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_ID_HASH;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileResponse implements Serializable {

	private static final long serialVersionUID = -7618653700855321997L;

	Profile profile;

	final List<CurrentService> currentServices = new ArrayList<>();

	public ProfileResponse(ProfileResponse original) {
		this.profile = new Profile(original.profile);
		original.currentServices.stream().map(CurrentService::new).forEach(currentServices::add);
	}

	public boolean hasField(String name) {
		return profile.hasField(name);
	}

	public Object getField(String name) {
		return profile.getField(name).orElse("");
	}

	public void addCurrentServices(List<CurrentService> currentServices) {
		this.currentServices.addAll(currentServices);
	}

	public String getUniqueId() {
		return (String) getField(CUS_CUSTOMER_ID_HASH);
	}

	public boolean hasCurrentService(String serviceName) {
		return currentServices.stream()
			.anyMatch(service -> service.getName().equals(serviceName));
	}
}
