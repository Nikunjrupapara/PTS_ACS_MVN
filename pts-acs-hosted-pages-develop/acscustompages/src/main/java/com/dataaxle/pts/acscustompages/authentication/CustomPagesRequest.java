package com.dataaxle.pts.acscustompages.authentication;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.MDC_REQUEST_ID;
import static com.google.common.base.Preconditions.checkArgument;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.exception.AppPageNotFoundException;
import com.dataaxle.pts.acscustompages.exception.CustomPagesRequestException;
import com.dataaxle.pts.acscustompages.exception.NoMatchingConditionFoundException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.Domain;
import com.dataaxle.pts.acscustompages.model.Environment;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.HiddenService;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ServiceField;
import com.dataaxle.pts.acscustompages.model.ServiceProfileField;
import com.dataaxle.pts.acscustompages.model.ViewType;
import com.dataaxle.pts.acscustompages.model.conditions.Condition;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.form.QueryParameters;
import com.dataaxle.pts.acscustompages.service.actions.ActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Value
public class CustomPagesRequest implements Serializable {

	private static final long serialVersionUID = -4851722499985561995L;

	AppDetails appDetails;

	Domain domain;

	String currentPageName;

	QueryParameters queryParams;

	String requestId;

	Environment environment;

	String ipAddress;

	String recaptchaResponse;

	String barcodeUrl;


	public String getRecaptchaKey(){
		AppPage appPage = appDetails.getPage(currentPageName);
		return appPage.getRecaptchaKey();
	}

	public boolean hasQueryParameter(String name) { return queryParams.hasParameter(name); }

	public List<String> getQueryParameter(String name) {
		if (hasQueryParameter(name)) {
			return queryParams.getParameter(name);
		}
		return Collections.emptyList();
	}

	public String getQueryParameterFirstValue(String name) { return queryParams.getParameterFirstValue(name); }

	@Deprecated
	public CustomPagesRequest(AppDetails appDetails, Domain domain, String servletContextPath) {
		this.appDetails = appDetails;
		this.domain = domain;
		this.currentPageName = deriveCurrentPage(servletContextPath);
		this.requestId = deriveRequestId();
		this.environment = Environment.getInstance(domain);
		this.ipAddress = "";
		this.recaptchaResponse = "";
		this.queryParams = QueryParameters.of(servletContextPath);
		this.barcodeUrl = "";
	}

	private CustomPagesRequest(Builder builder) {
		this.appDetails = builder.appDetails;
		this.domain = builder.domain;
		this.currentPageName = deriveCurrentPage(builder.servletContextPath);
		this.queryParams = QueryParameters.of(builder.servletContextPath);
		this.requestId = deriveRequestId();
		this.environment = Environment.getInstance(domain);
		this.ipAddress = builder.ipAddress;
		this.recaptchaResponse = builder.recaptchaResponse;
		this.barcodeUrl = builder.barcodeUrl;
	}

	private String deriveCurrentPage(String contextPath) {
		String[] contextPathElements = contextPath.split("/");
		String currentPage = contextPathElements[contextPathElements.length - 1];
		if (currentPage.contains("?")) {
			int qMarkIndex = currentPage.indexOf("?");
			currentPage = currentPage.substring(0, qMarkIndex);
		}
		return currentPage;
	}

	private String deriveRequestId() {
		return MDC.get(MDC_REQUEST_ID) != null ? MDC.get(MDC_REQUEST_ID) : UUID.randomUUID().toString();
	}

	public void checkPageName() {
		try {
			getAppPage(currentPageName);
		} catch (AppPageNotFoundException e) {
			throw new CustomPagesRequestException(this, e);
		}
	}

	public String getCompany() {
		return appDetails.getCompany();
	}

	public String getDomainName() { return domain.getName(); }

	public String getBrand() { return domain.getBrand(); }

	public String getContextPath() {
		return appDetails.getContextPath();
	}

	public String getFullPath() {
		return String.format("%s/%s", appDetails.getContextPath(), currentPageName);
	}

	public boolean isAuthenticated() {
		return appDetails.isAuthenticated().equals(AppDetails.AuthenticationStatus.AUTHENTICATED);
	}

	public AppDetails.AuthenticationStatus getAuthenticationStatus() {
		return appDetails.isAuthenticated();
	}

	public List<PageField<?>> getFieldsOnPage() {
		return getCurrentPage().getFields();
	}

	public List<PageField<?>> getFieldsOnPage(String pageName) {
		return appDetails.getPage(pageName).getFields();
	}

	public List<String> getFieldsToSubmit() {
		return getCurrentPage().getFieldsToSubmit();
	}

	public AppPage getCurrentPage() {
		return appDetails.getPage(currentPageName);
	}

	public Controller getController() { return getCurrentPage().getPostTo(); }

	public AppPage getAppPage(String name) { return appDetails.getPage(name); }

	public String getSuccessPageName() {
		return getSuccessPage().getViewName();
	}

	public AppPage getSuccessPage() { return appDetails.getPage(getCurrentPage().getSuccess().deriveViewName(null)); }

	@Deprecated
	public AppPage getSuccessPage(List<ProcessingAction> processingActions) {
		return appDetails.getPage(getCurrentPage().getSuccess().deriveViewName(""));
	}

	public AppPage getSuccessPage(DynamicFormBean dynamicFormBean) {
		String conditionName = evaluateConditions(dynamicFormBean);
		return appDetails.getPage(getCurrentPage().getSuccess().deriveViewName(conditionName));
	}

	public AppPage getSuccessPage(DynamicFormBean dynamicFormBean, ProfileResponse profileResponse) {
		String conditionName = evaluateConditions(profileResponse, dynamicFormBean, ViewType.SUCCESS);
		return appDetails.getPage(getCurrentPage().getSuccess().deriveViewName(conditionName));
	}

	public AppPage getSuccessPage(DynamicFormBean dynamicFormBean, CustomResourceResponse customResourceResponse) {
		String conditionName = evaluateConditions(customResourceResponse, dynamicFormBean, ViewType.SUCCESS);
		return appDetails.getPage(getCurrentPage().getSuccess().deriveViewName(conditionName));
	}

	public String getFailurePageName() {
		return getFailurePage().getViewName();
	}

	public AppPage getFailurePage() { return appDetails.getPage(getCurrentPage().getFailure().deriveViewName(null)); }

	public AppPage getFailurePage(DynamicFormBean dynamicFormBean) {
		String conditionName = evaluateConditions(dynamicFormBean);
		return appDetails.getPage(getCurrentPage().getFailure().deriveViewName(conditionName));
	}

	public AppPage getFailurePage(DynamicFormBean dynamicFormBean, ProfileResponse profileResponse) {
		String conditionName = evaluateConditions(profileResponse, dynamicFormBean, ViewType.FAILURE);
		return appDetails.getPage(getCurrentPage().getFailure().deriveViewName(conditionName));
	}

	public AppPage getFailurePage(DynamicFormBean dynamicFormBean, CustomResourceResponse customResourceResponse) {
		String conditionName = evaluateConditions(customResourceResponse, dynamicFormBean, ViewType.FAILURE);
		return appDetails.getPage(getCurrentPage().getFailure().deriveViewName(conditionName));
	}

	public AppPage getErrorPage() { return appDetails.getErrorPage(); }

	public AppPage getMissingInputPage() { return appDetails.getPage(getCurrentPage().getMissingInputView()); }

	public List<PageField<?>> getFieldsOnFailurePage() {
		return getFieldsOnPage(getCurrentPage().getFailure().deriveViewName(null));
	}

	public List<HiddenService> getPageServices() {
		return getCurrentPage().getServicesConfig().getHiddenServices();
	}

	public List<ServiceField<?>> getPageServiceFields() {
		return getCurrentPage().getServicesConfig().getServiceFields();
	}

	public List<ServiceProfileField<?>> getPageServiceProfileFields() {
		return getCurrentPage().getServicesConfig().getServiceProfileFields();
	}

	public String getProfileFilterName() { return getCurrentPage().getProfileFilterName(); }

	public String evaluateConditions(DynamicFormBean formInput) {
		AppPage currentPage = getCurrentPage();
		List<Condition> conditions = currentPage.getConditions();
		if (conditions.isEmpty()) {
			return "";
		}

		for (Condition condition : conditions) {
			boolean matched = false;
			switch (condition.getActsUpon()) {
				case FORM_DATA:
					matched = condition.evaluate(formInput);
					break;
				case NONE:
					matched = condition.evaluate();
					break;
				default:
					break;
			}
			if (matched) {
				LOG.info("Matched condition: {}", condition.getName());
				return condition.getName();
			}
		}

		throw new NoMatchingConditionFoundException(appDetails.getAppId(), currentPage.getViewName());
	}

	public String evaluateConditions(ProfileResponse profileResponse, DynamicFormBean formInput) {
		AppPage currentPage = getCurrentPage();
		List<Condition> conditions = currentPage.getConditions();
		if (conditions.isEmpty()) {
			return "";
		}

		for (Condition condition : conditions) {
			boolean matched = false;
			switch (condition.getActsUpon()) {
				case FORM_DATA:
					matched = condition.evaluate(formInput);
					break;
				case MULTIPLE:
					matched = condition.evaluate(profileResponse, formInput);
					break;
				case NONE:
					matched = condition.evaluate();
					break;
				case PROFILE:
					matched = condition.evaluate(profileResponse);
					break;
				default:
					break;
			}
			if (matched) {
				LOG.info("Matched condition: {}", condition.getName());
				return condition.getName();
			}
		}

		throw new NoMatchingConditionFoundException(appDetails.getAppId(), currentPage.getViewName());
	}

	public String evaluateConditions(ProfileResponse profileResponse, DynamicFormBean formInput, ViewType viewType) {
		AppPage currentPage = getCurrentPage();
		List<Condition> conditions = currentPage.getConditions(viewType);
		if (conditions.isEmpty()) {
			return "";
		}

		for (Condition condition : conditions) {
			boolean matched = false;
			switch (condition.getActsUpon()) {
				case FORM_DATA:
					matched = condition.evaluate(formInput);
					break;
				case MULTIPLE:
					matched = condition.evaluate(profileResponse, formInput);
					break;
				case NONE:
					matched = condition.evaluate();
					break;
				case PROFILE:
					matched = condition.evaluate(profileResponse);
					break;
				default:
					break;
			}
			if (matched) {
				LOG.info("Matched condition: {}", condition.getName());
				return condition.getName();
			}
		}
		return "";
	}

	public String evaluateConditions(CustomResourceResponse customResourceResponse, DynamicFormBean formInput) {
		AppPage currentPage = getCurrentPage();
		List<Condition> conditions = currentPage.getConditions();
		if (conditions.isEmpty()) {
			return "";
		}

		for (Condition condition : conditions) {
			boolean matched = false;
			switch (condition.getActsUpon()) {
				case CUSTOM_RESOURCE:
					matched = condition.evaluate(customResourceResponse);
					break;
				case FORM_DATA:
					matched = condition.evaluate(formInput);
					break;
				case NONE:
					matched = condition.evaluate();
					break;
				default:
					break;
			}
			LOG.info("Evaluated condition: {}, matched : {}", condition.getName(), matched);
			if (matched) {
				return condition.getName();
			}
		}

		throw new NoMatchingConditionFoundException(appDetails.getAppId(), currentPage.getViewName());
	}

	public String evaluateConditions(CustomResourceResponse customResourceResponse, DynamicFormBean formInput, ViewType viewType) {
		AppPage currentPage = getCurrentPage();
		List<Condition> conditions = currentPage.getConditions(viewType);
		if (conditions.isEmpty()) {
			return "";
		}
		for (Condition condition : conditions) {
			boolean matched = false;
			switch (condition.getActsUpon()) {
				case CUSTOM_RESOURCE:
					matched = condition.evaluate(customResourceResponse);
					break;
				case FORM_DATA:
					matched = condition.evaluate(formInput);
					break;
				case NONE:
					matched = condition.evaluate();
					break;
				case PROFILE:
					break;
				default:
					break;
			}
			if (matched) {
				LOG.info("Matched condition: {}", condition.getName());
				return condition.getName();
			}
		}

		return "";
	}

	public List<ActionProcessor> getProcessingActions(DynamicFormBean formInput) {
		AppPage currentPage = getCurrentPage();
		return currentPage.getActionProcessors(evaluateConditions(formInput));
	}

	public List<ActionProcessor> getProcessingActions(ProfileResponse profileResponse, DynamicFormBean formInput) {
		AppPage currentPage = getCurrentPage();
		return currentPage.getActionProcessors(evaluateConditions(profileResponse, formInput));
	}

	public List<ActionProcessor> getProcessingActions(CustomResourceResponse customResourceResponse, DynamicFormBean formInput) {
		return getCurrentPage().getActionProcessors(evaluateConditions(customResourceResponse, formInput));
	}

	public boolean redirectOnMissingInput() {
		AppPage missingInputPage = getMissingInputPage();
		AppPage currentPage = getCurrentPage();
		return !currentPage.getViewName().equalsIgnoreCase(missingInputPage.getViewName());
	}

	public void setResponseHeaders(HttpServletResponse response) {
		appDetails.getCustomHeaders().forEach((name, value) -> addHeader(response, name, value));
	}

	public String getRequestUrl() {
		AppPage appPage = appDetails.getPage(currentPageName);
		String controller = appPage.getPostTo().getPath();
		String contextPath = getContextPath();
		StringBuilder urlBuilder = new StringBuilder(
			String.format("https://%s%s/%s/%s", domain.getName(), controller, contextPath, currentPageName));
		String queryStr = queryParams.buildQueryString();
		if (StringUtils.hasText(queryStr)) {
			urlBuilder.append("?").append(queryStr);
		}

		return urlBuilder.toString();
	}

	public boolean useSessionStorage() {
		return getCurrentPage().isUseSessionStorage();
	}

	public boolean useRecaptcha() { return getCurrentPage().isUseRecaptcha(); }

	private void addHeader(HttpServletResponse response, String name, String value) {
		if (name.equals("Content-Security-Policy")) {
			String cspValue = (String) response.getHeader(name);
			if (StringUtils.isEmpty(cspValue)) {
				cspValue = "";
			}
			cspValue = String.format("%s; %s", cspValue, value);
			response.setHeader("Content-Security-Policy", cspValue);
		} else {
			response.setHeader(name, value);
		}
	}
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		AppDetails appDetails;

		Domain domain;

		String servletContextPath;

		String ipAddress = "";

		String recaptchaResponse = "";

		String barcodeUrl = "";

		public Builder appDetails(AppDetails appDetails) {
			this.appDetails = appDetails;
			return this;
		}

		public Builder domain(Domain domain) {
			this.domain = domain;
			return this;
		}

		public Builder servletContextPath(String servletContextPath) {
			this.servletContextPath = servletContextPath;
			return this;
		}

		public Builder ipAddress(String ipAddress) {
			this.ipAddress = ipAddress;
			return this;
		}

		public Builder recaptchaResponse(String recaptchaResponse) {
			this.recaptchaResponse = recaptchaResponse;
			return this;
		}

		public Builder barcodeUrl(String barcodeUrl) {
			this.barcodeUrl = barcodeUrl;
			return this;
		}

		public CustomPagesRequest build() {
			CustomPagesRequest result = new CustomPagesRequest(this);
			validate(result);
			return result;
		}


		private void validate(CustomPagesRequest result) {
			checkArgument(result.appDetails != null, "No application data provided!");
			checkArgument(result.domain != null, "No domain data provided!");
			checkArgument(StringUtils.hasText(result.currentPageName), "Current page is not set!");
			checkArgument(StringUtils.hasText(result.requestId), "Request Id is not present!");
			checkArgument(result.environment != null, "Unable to derive environment info!");
		}
	}
}
