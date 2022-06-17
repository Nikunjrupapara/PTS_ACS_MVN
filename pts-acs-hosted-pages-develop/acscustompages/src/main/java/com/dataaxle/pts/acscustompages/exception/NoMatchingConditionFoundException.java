package com.dataaxle.pts.acscustompages.exception;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class NoMatchingConditionFoundException extends CustomPagesException {

	private static final long serialVersionUID = 4134893218788987544L;

	AppDetails.AppDetailsId appId;

	String viewName;

	public NoMatchingConditionFoundException(AppDetails.AppDetailsId appId, String viewName) {
		this.appId = appId;
		this.viewName = viewName;
	}

	@Override
	public String getMessage() {
		return String.format("No condition matched for %s, page %s", appId, viewName);
	}
}
