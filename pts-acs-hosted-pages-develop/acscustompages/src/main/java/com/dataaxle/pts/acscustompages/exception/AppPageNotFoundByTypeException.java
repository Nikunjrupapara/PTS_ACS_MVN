package com.dataaxle.pts.acscustompages.exception;

import com.dataaxle.pts.acscustompages.model.PageType;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class AppPageNotFoundByTypeException extends CustomPagesException {
	private static final long serialVersionUID = -3886079716415928983L;

	PageType pageType;

	public AppPageNotFoundByTypeException(PageType pageType) {
		super();
		this.pageType = pageType;
	}

	@Override
	public String getMessage() {
		return String.format("App has no page of type %s configured", pageType.name());
	}
}
