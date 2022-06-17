package com.dataaxle.pts.acscustompages.model.navigation;

public interface NavigationProcessor {

	String deriveViewName(String condition);

	boolean useRedirect();

	abstract class Builder<T extends Builder<T>> {

		boolean useRedirect = true;

		public Builder<?> disableRedirect() {
			this.useRedirect = false;
			return this;
		}

		public abstract NavigationProcessor build();

		public abstract T self();
	}
}
