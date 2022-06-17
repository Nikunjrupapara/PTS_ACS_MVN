package com.dataaxle.pts.acscustompages.model;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.conditions.AlwaysTrueCondition;
import com.dataaxle.pts.acscustompages.model.conditions.Condition;
import com.dataaxle.pts.acscustompages.model.loadaction.LoadActionConfig;
import com.dataaxle.pts.acscustompages.model.navigation.ConditionalNavigation;
import com.dataaxle.pts.acscustompages.model.navigation.NavigationProcessor;
import com.dataaxle.pts.acscustompages.model.navigation.SimpleNavigation;
import com.dataaxle.pts.acscustompages.service.actions.ActionProcessor;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Value
public class AppPage implements Serializable {

	private static final long serialVersionUID = -977946074994122717L;

	public static final String DEFAULT_CONDITION_NAME = "default";

	String viewName;

	PageType pageType;

	Controller postTo;

	String customResource;

	NavigationProcessor success;

	NavigationProcessor failure;

	boolean redirectOnError;

	NavigationProcessor error;

	Map<String, Condition> conditions;

	List<PageField<?>> fields;

	ServicesConfig servicesConfig;

	Map<String, List<ActionProcessor>> conditionalActions;

	String missingInputView;

	boolean redirectOnMissingInput;

	boolean useSessionStorage;

	boolean useRecaptcha;

	RecaptchaVersion recaptchaVersion;

	String recaptchaSecret;

	String recaptchaKey;

	String recaptchaAction;

	double recaptchaScore;

	boolean forwardQueryParameters;

	Set<String> forwardParameters;

	Map<String, String> queryParameterMapping;

	ListCustomResourceConfig listCustomResource;

	List<LoadActionConfig> loadActions;

	List<String> numbersAsString;

	String profileFilterName;

	/**
	 * Maps profile field names to a parameter name for use with the filter specified in the profileFilterName
	 */
	Map<String, String> profileFilterParameters;

	public AppPage(Builder builder) {
		this.viewName = builder.viewName;
		this.pageType = builder.pageType;
		this.postTo = builder.postTo;
		this.customResource = builder.customResource;
		this.success = builder.success;
		this.failure = builder.failure;
		this.redirectOnError = builder.redirectOnError;
		this.error = builder.error;
		this.conditions = builder.conditions;
		this.fields = builder.fields;
		this.servicesConfig = builder.servicesConfig;
		this.conditionalActions = builder.conditionalActions;
		this.missingInputView = builder.missingInputView;
		this.redirectOnMissingInput = !this.viewName.equals(this.missingInputView);
		this.useSessionStorage = builder.useSessionStorage;
		// If RecaptchaVersion is null but RecatpchaSecret is provided then default to Recaptcha Version 2
		// This is for backwards compatibility with form configs added before Recaptcha V3 support was added.
		this.recaptchaVersion = builder.recaptchaVersion != null ? builder.recaptchaVersion :
									StringUtils.hasText(builder.recaptchaSecret) ? RecaptchaVersion.V2 : null;
		this.recaptchaSecret = builder.recaptchaSecret;
		this.recaptchaKey = builder.recaptchaKey;
		this.recaptchaAction = builder.recaptchaAction;
		this.recaptchaScore = builder.recaptchaScore;
		this.useRecaptcha = this.recaptchaVersion != null;
		this.forwardQueryParameters = builder.forwardQueryParameters;
		this.forwardParameters = builder.forwardParameters;
		this.queryParameterMapping = builder.queryParameterMapping;
		this.listCustomResource = builder.listCustomResource;
		this.loadActions = builder.loadActions;
		this.numbersAsString = builder.numbersAsString;
		this.profileFilterName = builder.profileFilterName;
		this.profileFilterParameters = builder.profileFilterParameters;
	}

	public List<String> getFieldNames() {
		return fields.stream()
			.map(PageField::getName)
			.collect(Collectors.toList());
	}

	public List<String> getFieldsToSubmit() {
		return fields.stream()
				   .filter(PageField::isSubmit)
				   .map(PageField::getName)
				   .collect(Collectors.toList());
	}

	public List<Condition> getConditions() {
		return conditions.values().stream()
				   .sorted(Comparator.comparing(Condition::getProcessingOrder))
				   .collect(Collectors.toList());
	}

	/**
	 * Return the list of conditions associated with a NavigationProcessor so that only conditions associated with
	 * that NavigationProcessor are evaluated when deriving the view to render.
	 * @param viewType
	 * @return
	 */
	public List<Condition> getConditions(ViewType viewType) {
		NavigationProcessor navigationProcessor = null;
		switch (viewType) {
			case ERROR:
				navigationProcessor = error;
				break;
			case FAILURE:
				navigationProcessor = failure;
				break;
			case SUCCESS:
				navigationProcessor = success;
			default:
		}
		if (navigationProcessor == null) {
			return Collections.emptyList();
		}
		if (navigationProcessor instanceof SimpleNavigation) {
			return Collections.emptyList();
		}
		ConditionalNavigation conditionalNavigation = (ConditionalNavigation) navigationProcessor;
		return conditionalNavigation.getConditions().keySet().stream()
				   .map(conditions::get)
				   .filter(Objects::nonNull)
				   .sorted(Comparator.comparing(Condition::getProcessingOrder))
				   .collect(Collectors.toList());
	}

	public List<ActionProcessor> getActionProcessors() {
		return conditionalActions.getOrDefault(DEFAULT_CONDITION_NAME, Collections.emptyList());
	}

	public List<ActionProcessor> getActionProcessors(String conditionName) {
		return conditionalActions.getOrDefault(conditionName, Collections.emptyList());
	}

	public boolean isRecaptchaV2() { return recaptchaVersion == RecaptchaVersion.V2; }

	public boolean isListCustomResource() { return listCustomResource != null; }

	public static Builder builder(String viewName, PageType pageType) {
		return new Builder(viewName, pageType);
	}

	public static class Builder {

		private final String viewName;

		private final PageType pageType;

		private Controller postTo = Controller.NONE;

		private String customResource = "";

		private NavigationProcessor success;

		private NavigationProcessor failure;

		private boolean redirectOnError = true;

		private NavigationProcessor error;

		private final Map<String, Condition> conditions = new HashMap<>();

		private List<PageField<?>> fields = new ArrayList<>();

		private final ServicesConfig servicesConfig = new ServicesConfig();

		private final Map<String, List<ActionProcessor>> conditionalActions = new HashMap<>();

		private String missingInputView = "error";

		private boolean useSessionStorage = true;

		private RecaptchaVersion recaptchaVersion;

		private String recaptchaSecret = "";

		private String recaptchaKey = "";

		private String recaptchaAction;

		private double recaptchaScore;

		private boolean forwardQueryParameters = false;

		private final Set<String> forwardParameters = new TreeSet<>();

		private final Map<String, String> queryParameterMapping = new HashMap<>();

		private ListCustomResourceConfig listCustomResource;

		private final List<LoadActionConfig> loadActions = new ArrayList<>();

		private final List<String> numbersAsString = new ArrayList<>();

		private String profileFilterName;

		private final Map<String, String> profileFilterParameters = new HashMap<>();

		private Builder(String viewName, PageType pageType) {
			this.viewName = viewName;
			this.pageType = pageType;
		}

		public Builder controller(Controller controller) {
			this.postTo = controller;
			return this;
		}

		public Builder customResource(String customResource) {
			this.customResource = customResource;
			return this;
		}

		public Builder numberAsString(String name) {
			this.numbersAsString.add(name);
			return this;
		}

		public Builder successNavigation(String success) {
			this.success = new SimpleNavigation.Builder().viewName(success).build();
			return this;
		}

		public Builder successNavigation(NavigationProcessor success) {
			this.success = success;
			return this;
		}

		public Builder failureNavigation(String failure) {
			this.failure = new SimpleNavigation.Builder().viewName(failure).build();
			return this;
		}

		public Builder failureNavigation(NavigationProcessor failure) {
			this.failure = failure;
			return this;
		}

		public Builder noRedirectOnError() {
			this.redirectOnError = false;
			this.error = null;
			return this;
		}

		public Builder errorNavigation(String error) {
			this.error = new SimpleNavigation.Builder().viewName(error).build();
			this.redirectOnError = true;
			return this;
		}

		public Builder errorNavigation(NavigationProcessor error) {
			this.error = error;
			this.redirectOnError = true;
			return this;
		}

		public Builder condition(Condition condition) {
			this.conditions.put(condition.getName(), condition);
			return this;
		}

		public Builder defaultCondition() {
			Condition alwaysTrue = new AlwaysTrueCondition(DEFAULT_CONDITION_NAME, Integer.MAX_VALUE);
			this.conditions.put(alwaysTrue.getName(), alwaysTrue);
			return this;
		}

		public Builder fields(List<PageField<?>> fields) {
			this.fields = fields;
			return this;
		}

		public Builder addField(PageField<?> field) {
			this.fields.add(field);
			return this;
		}

		public Builder hiddenService(HiddenService hiddenService) {
			this.servicesConfig.addHiddenService(hiddenService);
			return this;
		}

		public Builder serviceField(ServiceField<?> serviceField) {
			this.servicesConfig.addServiceField(serviceField);
			return this;
		}

		public Builder serviceProfileField(ServiceProfileField<?> serviceProfileField) {
			this.servicesConfig.addServiceProfileField(serviceProfileField);
			return this;
		}

		public Builder defaultActions(List<ActionProcessor> actions) {
			if (!conditions.containsKey(DEFAULT_CONDITION_NAME)) {
				this.defaultCondition();
			}
			return this.conditionalAction(DEFAULT_CONDITION_NAME, actions);
		}

		public Builder conditionalAction(String conditionName, List<ActionProcessor> actions) {
			if (!conditions.containsKey(conditionName)) {
				throw new IllegalArgumentException(String.format("Condition name '%s' not found!", conditionName));
			}
			conditionalActions.put(conditionName, actions);
			return this;
		}

		public Builder missingInputView(String viewName) {
			this.missingInputView = viewName;
			return this;
		}

		public Builder disableSessionStorage() {
			this.useSessionStorage = false;
			return this;
		}

		public Builder recaptchaVersion(String version) {
			return this.recaptchaVersion(RecaptchaVersion.of(version));
		}

		public Builder recaptchaVersion(RecaptchaVersion version) {
			this.recaptchaVersion = version;
			return this;
		}

		public Builder recaptchaSecret(String secret) {
			this.recaptchaSecret = secret;
			return this;
		}

		public Builder recaptchaKey(String key) {
			this.recaptchaKey = key;
			return this;
		}

		public Builder recaptchaScore(double score) {
			this.recaptchaScore = score;
			return this;
		}

		public Builder recaptchaAction(String action) {
			this.recaptchaAction = action;
			return this;
		}

		public Builder forwardQueryParameters() {
			this.forwardQueryParameters = true;
			return this;
		}

		public Builder forwardQueryParameters(boolean forwardQueryParameters) {
			this.forwardQueryParameters = forwardQueryParameters;
			return this;
		}

		public Builder forwardParameter(String name) {
			this.forwardParameters.add(name);
			return this;
		}

		public Builder mapQueryParameter(String queryParameter, String filterParameter) {
			this.queryParameterMapping.put(queryParameter, filterParameter);
			return this;
		}

		public Builder listCustomResource(ListCustomResourceConfig config) {
			this.listCustomResource = config;
			return this;
		}

		public Builder loadAction(LoadActionConfig config) {
			this.loadActions.add(config);
			return this;
		}

		public Builder profileFilterName(String profileFilterName) {
			this.profileFilterName = profileFilterName;
			return this;
		}

		public Builder profileFilterParameter(String fieldName, String parameterName) {
			this.profileFilterParameters.put(fieldName, parameterName);
			return this;
		}

		public AppPage build() {
			return new AppPage(this);
		}

		// TODO: in V2 add validation that LoadActionConfig#order is unique

	}
}
