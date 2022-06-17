package com.dataaxle.pts.acscustompages.model.navigation;

import lombok.Value;

import java.io.Serializable;

@Value
public class SimpleNavigation implements NavigationProcessor, Serializable {

	private static final long serialVersionUID = 1660905455702351494L;

	String viewName;

	boolean useRedirect;

	private SimpleNavigation(Builder builder) {
		this.viewName = builder.viewName;
		this.useRedirect = builder.useRedirect;
	}

	@Override
	public String deriveViewName(String condition) {
		return viewName;
	}

	@Override
	public boolean useRedirect() {
		return useRedirect;
	}

	public static class Builder extends NavigationProcessor.Builder<Builder> {

		String viewName;

		public Builder viewName(String viewName) {
			this.viewName = viewName;
			return this;
		}

		@Override
		public NavigationProcessor build() {
			return new SimpleNavigation(this);
		}

		@Override
		public Builder self() {
			return this;
		}
	}
}
