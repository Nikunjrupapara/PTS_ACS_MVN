package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.exception.AppPageNotFoundByTypeException;
import com.dataaxle.pts.acscustompages.exception.AppPageNotFoundException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ViewType;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.form.QueryParameters;
import com.dataaxle.pts.acscustompages.model.navigation.NavigationProcessor;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;
import com.dataaxle.pts.acscustompages.utils.UtilityFunctions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class DefaultViewNameService implements ViewNameService {
	@Override
	public String deriveViewName(HttpServletRequest request) {
		String servletContextPath = request.getServletPath();
		return servletContextPath.replaceFirst("/", "").replaceFirst("profile", "companies");
	}

	@Override
	public String deriveViewName(HttpServletRequest request, String target) {
		String servletContextPath = request.getServletPath();
		return generateViewPath(servletContextPath, target);
	}

	@Override
	public String deriveViewName(AppDetails appDetails) {
		return String.format("companies/%s/%s", appDetails.getCompany(), appDetails.getContextPath()).replaceAll("//", "/");
	}

	@Override
	public String deriveViewName(AppDetails appDetails, String target) {
		String name = deriveViewName(appDetails);
		return String.format("%s%s", name.substring(0, name.lastIndexOf("/") + 1), target);
	}

	@Override
	public String deriveViewName(CustomPagesRequest customPagesRequest, ViewType viewType) {
		return deriveViewName(customPagesRequest, viewType, false);
	}

	@Override
	public String deriveViewName(CustomPagesRequest customPagesRequest, ViewType viewType, boolean redirect) {
		switch (viewType) {
			case CURRENT_PAGE:
				return deriveThisPage(customPagesRequest);
			case ENTRY_PAGE:
				return deriveEntryPage(customPagesRequest, redirect);
			case ERROR:
				return deriveErrorView(customPagesRequest, redirect);
			case FAILURE:
				return deriveFailureView(customPagesRequest, redirect);
			case SUCCESS:
				return deriveSuccessView(customPagesRequest, redirect);
		}
		return deriveErrorView(customPagesRequest, redirect);
	}

	@Override
	public String deriveThisPage(CustomPagesRequest customPagesRequest) {
		return generateViewPath(customPagesRequest, customPagesRequest.getCurrentPageName());
	}

	@Override
	public String deriveEntryPage(CustomPagesRequest customPagesRequest) {
		return deriveEntryPage(customPagesRequest, false);
	}

	@Override
	public String deriveMissingInputView(CustomPagesRequest customPagesRequest) {
		AppPage currentPage = customPagesRequest.getCurrentPage();
		if (currentPage.isRedirectOnMissingInput()) {
			try {
				AppPage missingInput = customPagesRequest.getMissingInputPage();
				return generateViewPath(customPagesRequest, missingInput.getViewName(), missingInput.getPostTo(), true);
			} catch (AppPageNotFoundException e) {
				LOG.debug("missingInputView not found for page {}", currentPage.getViewName());
				return "/error";
			}
		}
		return deriveThisPage(customPagesRequest);
	}

	@Override
	public String deriveEntryPage(CustomPagesRequest customPagesRequest, boolean redirect) {
		AppDetails appDetails = customPagesRequest.getAppDetails();
		AppPage appPage = appDetails.getEntryPoint();
		return addRedirect(generateViewPath(customPagesRequest, appPage.getViewName(), buildQueryString(customPagesRequest)), redirect);
	}

	@Override
	public String deriveSuccessView(CustomPagesRequest customPagesRequest) {
		return deriveSuccessView(customPagesRequest, false);
	}

	@Override
	public String deriveSuccessView(CustomPagesRequest customPagesRequest, boolean redirect) {
		AppPage success = customPagesRequest.getSuccessPage();
		return generateViewPath(customPagesRequest, success.getViewName(), success.getPostTo(), redirect);
	}

	@Override
	public String deriveSuccessView(CustomPagesRequest customPagesRequest, List<ProcessingAction> processingActions, boolean redirect) {
		AppPage success = customPagesRequest.getSuccessPage(processingActions);
		return generateViewPath(customPagesRequest, success.getViewName(), success.getPostTo(), redirect);
	}

	@Override
	public String deriveSuccessView(CustomPagesRequest customPagesRequest, DynamicFormBean formInput) {
		AppPage success = customPagesRequest.getSuccessPage(formInput);
		NavigationProcessor successNavigation = customPagesRequest.getCurrentPage().getSuccess();
		return generateViewPath(customPagesRequest, success.getViewName(), success.getPostTo(), successNavigation.useRedirect());
	}

	@Override
	public String deriveSuccessView(CustomPagesRequest customPagesRequest, ProfileResponse profileResponse, DynamicFormBean formInput) {
		AppPage success = customPagesRequest.getSuccessPage(formInput, profileResponse);
		NavigationProcessor successNavigation = customPagesRequest.getCurrentPage().getSuccess();
		return generateViewPath(customPagesRequest, success.getViewName(), success.getPostTo(), successNavigation.useRedirect());
	}

	@Override
	public String deriveSuccessView(CustomPagesRequest customPagesRequest, DynamicFormBean formInput, CustomResourceResponse customResourceResponse) {
		AppPage success = customPagesRequest.getSuccessPage(formInput, customResourceResponse);
		NavigationProcessor successNavigation = customPagesRequest.getCurrentPage().getSuccess();
		return generateViewPath(customPagesRequest, success.getViewName(), success.getPostTo(), successNavigation.useRedirect());
	}

	@Override
	public String deriveFailureView(CustomPagesRequest customPagesRequest) {
		AppPage failure = customPagesRequest.getFailurePage();
		NavigationProcessor failureNavigation = customPagesRequest.getCurrentPage().getFailure();
		return generateViewPath(customPagesRequest, failure.getViewName(), failure.getPostTo(), failureNavigation.useRedirect());
	}

	@Override
	@Deprecated
	public String deriveFailureView(CustomPagesRequest customPagesRequest, boolean redirect) {
		AppPage failure = customPagesRequest.getFailurePage();
		return generateViewPath(customPagesRequest, failure.getViewName(), failure.getPostTo(), redirect);
	}

	@Override
	public String deriveFailureView(CustomPagesRequest customPagesRequest, DynamicFormBean formInput) {
		AppPage failure = customPagesRequest.getFailurePage(formInput);
		NavigationProcessor failureNavigation = customPagesRequest.getCurrentPage().getFailure();
		return generateViewPath(customPagesRequest, failure.getViewName(), failure.getPostTo(), failureNavigation.useRedirect());
	}

	@Override
	public String deriveFailureView(CustomPagesRequest customPagesRequest, ProfileResponse profileResponse, DynamicFormBean formInput) {
		AppPage failure = customPagesRequest.getFailurePage(formInput, profileResponse);
		NavigationProcessor failureNavigation = customPagesRequest.getCurrentPage().getFailure();
		return generateViewPath(customPagesRequest, failure.getViewName(), failure.getPostTo(), failureNavigation.useRedirect());
	}

	@Override
	public String deriveFailureView(CustomPagesRequest customPagesRequest, DynamicFormBean formInput, CustomResourceResponse customResourceResponse) {
		AppPage failure = customPagesRequest.getFailurePage(formInput, customResourceResponse);
		NavigationProcessor failureNavigation = customPagesRequest.getCurrentPage().getFailure();
		return generateViewPath(customPagesRequest, failure.getViewName(), failure.getPostTo(), failureNavigation.useRedirect());
	}

	@Override
	public String deriveErrorView(CustomPagesRequest customPagesRequest) {
		AppPage currentPage = customPagesRequest.getCurrentPage();
		if (currentPage.isRedirectOnError()) {
			try {
				AppPage errorPage = getErrorPage(customPagesRequest);
				return generateViewPath(customPagesRequest, errorPage.getViewName(), errorPage.getPostTo(), true);
			} catch (AppPageNotFoundByTypeException e) {
				LOG.debug("No error page found for page {}", customPagesRequest.getCurrentPage());
				return "/error";
			}
		}
		return deriveThisPage(customPagesRequest);
	}

	@Override
	public String deriveErrorView(CustomPagesRequest customPagesRequest, boolean redirect) {
		AppPage errorPage = getErrorPage(customPagesRequest);
		return generateViewPath(customPagesRequest, errorPage.getViewName(), errorPage.getPostTo(), redirect);
	}

	private String generateViewPath(CustomPagesRequest customPagesRequest, String viewName) {
		return String.format("/companies/%s/%s/%s", customPagesRequest.getCompany(),
			customPagesRequest.getContextPath(), viewName);
	}

	private String generateViewPath(CustomPagesRequest customPagesRequest, String viewName, String queryParams) {
		return String.format("/companies/%s/%s/%s%s", customPagesRequest.getCompany(),
			customPagesRequest.getContextPath(), viewName, queryParams);
	}

	private String generateViewPath(CustomPagesRequest customPagesRequest, String viewName, Controller postTo, boolean redirect) {
		String queryParams = buildQueryString(customPagesRequest);
		if (postTo == Controller.NONE) {
			return addRedirect(generateViewPath(customPagesRequest, viewName, queryParams), redirect);
		}
		return addRedirect(String.format("%s/%s/%s%s", postTo.getPath(), customPagesRequest.getContextPath(),
			viewName, queryParams), redirect);
	}

	private AppPage getErrorPage(CustomPagesRequest customPagesRequest) {
		return customPagesRequest.getErrorPage();
	}

	private String generateViewPath(String servletContextPath, String target) {
		List<String> contextList = Arrays.asList(servletContextPath.split("/"));
		List<String> targetContextPathList = new ArrayList<>(contextList.subList(1, contextList.size() - 1));
		targetContextPathList.add(target);
		return String.join("/", targetContextPathList).replaceFirst("profile", "companies");
	}

	private String addRedirect(String view, boolean redirect) {
		if (redirect) {
			return String.format("redirect:%s", view);
		}
		return view;
	}

	private String buildQueryString(CustomPagesRequest customPagesRequest) {
		AppPage thisPage = customPagesRequest.getCurrentPage();
		if (!thisPage.isForwardQueryParameters()) {
			return "";
		}
		QueryParameters queryParameters = customPagesRequest.getQueryParams();
		Set<String> parameterNames = CollectionUtils.isEmpty(thisPage.getForwardParameters()) ?
										  queryParameters.getParameterNames() :
										  thisPage.getForwardParameters();
		if (parameterNames.isEmpty()) {
			return "";
		}
		String queryStr = queryParameters.buildQueryString(parameterNames);
		if (StringUtils.hasLength(queryStr)) {
			return "?" + queryStr;
		}
		return "";
	}
}
