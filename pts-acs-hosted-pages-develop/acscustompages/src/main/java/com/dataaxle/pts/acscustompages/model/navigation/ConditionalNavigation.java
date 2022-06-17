package com.dataaxle.pts.acscustompages.model.navigation;

import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Value
public class ConditionalNavigation implements NavigationProcessor, Serializable {

	private static final long serialVersionUID = 8868286315218358380L;

	Map<String, String> conditions;

	String defaultViewName;

	boolean useRedirect;

	private ConditionalNavigation(Builder builder) {
		this.conditions = builder.conditions;
		this.defaultViewName = builder.defaultViewName;
		this.useRedirect = builder.useRedirect;
	}

	@Override
	public String deriveViewName(String conditionName) {
		return conditions.getOrDefault(conditionName, defaultViewName);
	}

	@Override
	public boolean useRedirect() {
		return useRedirect;
	}

	public static class Builder extends NavigationProcessor.Builder<Builder> {

		Map<String, String> conditions = new HashMap<>();

		String defaultViewName;

		public Builder condition(String conditionName, String viewName) {
			this.conditions.put(conditionName, viewName);
			return this;
		}

		public Builder defaultViewName(String defaultViewName) {
			this.defaultViewName = defaultViewName;
			return this;
		}

		@Override
		public NavigationProcessor build() {
			if (StringUtils.isEmpty(defaultViewName)) {
				defaultViewName = "error";
			}
			return new ConditionalNavigation(this);
		}

		@Override
		public Builder self() {
			return this;
		}
	}
}
