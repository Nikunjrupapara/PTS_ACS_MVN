package com.dataaxle.pts.acscustompages.controller;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequestResult;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.FindCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileRequestResult;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.RequestResult;
import com.dataaxle.pts.acscustompages.model.ServiceField;
import com.dataaxle.pts.acscustompages.model.ServiceProfileField;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.form.QueryParameters;
import com.dataaxle.pts.acscustompages.model.loadaction.LoadActionConfig;
import com.dataaxle.pts.acscustompages.model.loadaction.ValueProvider;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.dataaxle.pts.acscustompages.service.loadaction.factory.LoadActionProcessorFactory;
import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import javax.servlet.http.HttpSession;

public class ControllerUtils {

	public static final String APP_ID = "appId";

	public static final String CUSTOM_RESOURCE_RESPONSE_WRAPPER = "customResourceResponseWrapper";

	public static final String DYNAMIC_FORM_BEAN = "dynamicFormBean";

	public static final String PROFILE_RESPONSE = "profileResponse";

	public static final String CUSTOM_PAGES_REQUEST = "customPagesRequest";

	public static final String EXCEPTION_MSG = "exMsg";

	public static final String EXCEPTION_TRACE = "exTrace";

	public static final String LIST_CUSTOM_RESOURCE_REQUEST = "listCustomResourceRequest";

	public static final String PROFILE_RESPONSE_WRAPPER = "profileResponseWrapper";

	public static final String MULTIPLE_PATH_SEGMENTS = "/**";

	public static final String CONFIRMATION_PAGE_PATH = "/p/page/confirm";

	public static final String CSP_PATH = "/p/page/csp";

	public static final String CREATE_CUSTOM_RESOURCE_PATH = "/p/customResource/create";

	public static final String CREATE_PROFILE_PATH = "/p/profile/create";

	public static final String CUSTOM_RESOURCE_CONFIRMATION_PATH = "/p/customResource/confirm";

	public static final String ERROR_PAGE_PATH = "/p/page/error";

	public static final String GET_CUSTOM_RESOURCE_BY_ID_PATH = "/p/customResource/search";

	public static final String GET_CUSTOM_RESOURCE_BY_LINK_PATH = "/p/customResource/secure";

	public static final String GET_PROFILE_CUSTOMERID_PATH = "/p/profile/search";

	public static final String GET_PROFILE_FILTER_PATH = "/p/profile/find";

	public static final String GET_PROFILE_SECURELINK_PATH = "/p/profile/secure";

	public static final String LIST_CUSTOM_RESOURCE_PATH = "/p/customResource/list";

	public static final String POST_PROFILE_PATH = "/p/profile/post";

	public static final String PROFILE_CONFIRM_PATH = "/p/profile/confirm";

	public static final String TRIGGER_EMAIL_PATH = "/p/profile/email";

	public static final String UPDATE_PROFILE_PATH = "/p/profile/update";

	public static final String UPDATE_SERVICES_PATH = "/p/services/update";

	public static final String NO_PATH = "";

	public static final String GENERIC_PAGE_PATH = "/p/page/display";

	// initialise a form with no previous input
	public static void initialiseForm(CustomPagesRequest customPagesRequest, DynamicFormBean thisPage) {
		customPagesRequest.getFieldsOnPage()
			.forEach(pageField -> thisPage.initialiseField(pageField.getName(), pageField.getDefaultValue()));
		thisPage.setProfileRequestResult(new ProfileRequestResult(new ProfileRequest("")));
	}

	// initialise a form with no previous input but that uses Query Parameters
	public static void initialiseForm(CustomPagesRequest customPagesRequest, DynamicFormBean thisPage,
									  QueryParameters queryParameters, RequestResult requestResult) {
		customPagesRequest.getFieldsOnPage()
			.forEach(pageField -> {
				String fieldName = pageField.getName();
				if (queryParameters.hasParameter(fieldName)) {
					thisPage.initialiseField(fieldName, queryParameters.getParameterAsString(fieldName));
				} else {
					thisPage.initialiseField(fieldName, pageField.getDefaultValue());
				}
			});
		thisPage.setRequestResult(requestResult);
	}

	// initialise a form with data from a previous page which did not return a profile, e.g. an unsuccessful
	// profile search by customerId
	public static void initialiseForm(CustomPagesRequest customPagesRequest, DynamicFormBean fromPrevPage, DynamicFormBean thisPage) {
		if (fromPrevPage != null) {
			Map<String, Object> prevPageData = fromPrevPage.getItems();
			customPagesRequest.getFieldsOnPage().forEach(pageField -> {
				String fieldName = pageField.getName();
				Object value = prevPageData.containsKey(fieldName) ? prevPageData.get(fieldName) : pageField.getDefaultValue();
				thisPage.initialiseField(fieldName, value);
			});
			thisPage.setError(fromPrevPage.isError());
		}
	}
	// initialise a form with the results of a Profile request
	public static void initialiseForm(CustomPagesRequest customPagesRequest, ProfileResponseWrapper profileResponseWrapper,
									  DynamicFormBean dynamicFormBean) {
		initialiseForm(customPagesRequest, dynamicFormBean, profileResponseWrapper, null);
	}

	// initialise a form with the results of a Profile request and data from a previous page
	public static void initialiseForm(CustomPagesRequest customPagesRequest, DynamicFormBean thisPageFormBean,
									  ProfileResponseWrapper profileResponseWrapper, DynamicFormBean fromPrevPage) {
		boolean hasPrevPageData = fromPrevPage != null;
		if (profileResponseWrapper.hasProfile()) {
			ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
			customPagesRequest.getFieldsOnPage().forEach(pageField -> {
				String fieldName = pageField.getName();
				Profile profile = profileResponse.getProfile();
				// Set the field value using the incoming profile, previous page data and query parameters.
				// Profile data takes priority, then previous page data then query parameters
				// If none contains a value for the field then use its configured default value
				Object value = null;
				boolean valueSet = false;
				if (profile.hasField(fieldName)) {
					value = profile.getField(fieldName).orElse("");
					valueSet = true;
				}
				if (!valueSet) {
					if (hasPrevPageData) {
						if (fromPrevPage.hasItem(fieldName)) {
							value = fromPrevPage.getItem(fieldName);
							valueSet = true;
						}
					}
				}
				if (!valueSet) {
					if (customPagesRequest.hasQueryParameter(fieldName)) {
						value = customPagesRequest.getQueryParameterFirstValue(fieldName);
						valueSet = true;
					}
				}
				if (!valueSet) {
					value = pageField.getDefaultValue();
				}
				thisPageFormBean.initialiseField(fieldName, value);
			});
			Set<String> servicesOnForm = new HashSet<>();
			List<ServiceField<?>> serviceFields = customPagesRequest.getPageServiceFields();
			List<ServiceProfileField<?>> serviceProfileFields = customPagesRequest.getPageServiceProfileFields();
			serviceFields.forEach(serviceField -> servicesOnForm.addAll(serviceField.getServiceNames()));
			serviceProfileFields.forEach(serviceProfileField -> servicesOnForm.addAll(serviceProfileField.getServiceNames()));
			servicesOnForm.forEach(name -> thisPageFormBean.initialiseService(name, profileResponse.hasCurrentService(name)));

			serviceFields.stream()
				.filter(ServiceField::isSingleService)
				.forEach(serviceField -> {
					String serviceName = serviceField.getServiceName();
					boolean hasService = profileResponse.hasCurrentService(serviceName);
					String fieldValue = hasService ? serviceField.getFormOnValue() : serviceField.getFormOffValue();
					thisPageFormBean.initialiseField(serviceField.getFieldName(), fieldValue);
				});
			serviceFields.stream()
				.filter(Predicate.not(ServiceField::isSingleService))
				.forEach(serviceField -> thisPageFormBean.initialiseField(serviceField.getFieldName(), serviceField.getFormOffValue()));
			serviceProfileFields.stream()
				.filter(ServiceField::isSingleService)
				.forEach(serviceField -> {
					String serviceName = serviceField.getServiceName();
					String fieldName = serviceField.getFieldName();
					boolean hasService = profileResponse.hasCurrentService(serviceName);
					// For a ServiceProfileField we always use the current service subscription status rather than the
					// associated profile attribute to set the value displayed on the form.  The values will be synchronised
					// when the form is submitted and the Microservices API request processed.
					String fieldValue = hasService ? serviceField.getFormOnValue() : serviceField.getFormOffValue();
					thisPageFormBean.initialiseField(fieldName, fieldValue);
				});
			serviceProfileFields.stream()
				.filter(Predicate.not(ServiceField::isSingleService))
				.forEach(serviceProfileField -> {
					Object acsValue = profileResponse.getField(serviceProfileField.getProfileName());
					String fieldValue = serviceProfileField.getFormValue(acsValue);
					thisPageFormBean.initialiseField(serviceProfileField.getFieldName(), fieldValue);
				});
		}
		thisPageFormBean.setProfileRequestResult(profileResponseWrapper.getProfileRequestResult());
	}

	// initialise a form with the results from a GetCustomResourceRequest
	public static void initialiseForm(CustomPagesRequest customPagesRequest, DynamicFormBean thisPageFormBean,
									  CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper,
									  DynamicFormBean fromPrevPage) {
		boolean hasPrevPageData = fromPrevPage != null;
		customPagesRequest.getFieldsOnPage().forEach(pageField -> {
			GetCustomResourceResponse response;
			CustomResourceRecord record = new CustomResourceRecord();
			if (customResourceResponseWrapper.hasResponse()) {
				response = customResourceResponseWrapper.getCustomResourceResponse();
				record = response.getRecord();
			}
			String fieldName = pageField.getName();
			// Set the field value using the incoming record, previous page data and query parameters.
			// Record data takes priority, then previous page data then query parameters
			// If none contains a value for the field then use its configured default value
			Object value = null;
			boolean valueSet = false;
			if (record.hasField(fieldName)) {
				value = record.getValue(fieldName).orElse("");
				valueSet = true;
			}
			if (!valueSet) {
				if (hasPrevPageData) {
					if (fromPrevPage.hasItem(fieldName)) {
						value = fromPrevPage.getItem(fieldName);
						valueSet = true;
					}
				}
			}
			if (!valueSet) {
				if (customPagesRequest.hasQueryParameter(fieldName)) {
					value = customPagesRequest.getQueryParameterFirstValue(fieldName);
					valueSet = true;
				}
			}
			if (!valueSet) {
				value = pageField.getDefaultValue();
			}
			thisPageFormBean.initialiseField(fieldName, value);
		});
		thisPageFormBean.setRequestResult(customResourceResponseWrapper.getCustomResourceRequestResult());
		ControllerUtils.performLoadActions(customPagesRequest, thisPageFormBean, customResourceResponseWrapper.getRecord(),
			customPagesRequest.getQueryParams());
	}

	// initialise a form with the results from a ListCustomResourceRequest
	public static void initialiseForm(CustomPagesRequest customPagesRequest, DynamicFormBean thisPageFormBean,
									  ListCustomResourceResponse customResourceResponse, DynamicFormBean fromPrevPage,
									  CustomResourceRequestResult customResourceRequestResult) {
		boolean hasPrevPageData = fromPrevPage != null;
		customPagesRequest.getFieldsOnPage().forEach(pageField -> {
			String fieldName = pageField.getName();
			Object value = null;
			boolean valueSet = false;
			if (hasPrevPageData) {
				if (fromPrevPage.hasItem(fieldName)) {
					value = fromPrevPage.getItem(fieldName);
					valueSet = true;
				}
			}
			if (!valueSet) {
				if (customPagesRequest.hasQueryParameter(fieldName)) {
					value = customPagesRequest.getQueryParameterFirstValue(fieldName);
					valueSet = true;
				}
			}
			if (!valueSet) {
				value = pageField.getDefaultValue();
			}
			thisPageFormBean.initialiseField(fieldName, value);
		});
		thisPageFormBean.setRequestResult(customResourceRequestResult);
	}

	public static void initialiseFields(List<PageField<?>> fieldsOnPage, Map<String, Object> source, DynamicFormBean dynamicFormBean) {
		fieldsOnPage.forEach(pageField -> {
			String fieldName = pageField.getName();
			Object value = source.containsKey(fieldName) ? source.get(fieldName) : pageField.getDefaultValue();
			dynamicFormBean.initialiseField(fieldName, value);
		});
	}

	public static void performLoadActions(CustomPagesRequest customPagesRequest, DynamicFormBean thisPageFormBean,
										  ValueProvider... sources) {
		customPagesRequest.getCurrentPage().getLoadActions().stream()
			.sorted(Comparator.comparing(LoadActionConfig::getOrder))
			.map(LoadActionProcessorFactory::of)
			.filter(Objects::nonNull)
			.forEach(processor -> {
				FindCustomResourceRequest request = processor.prepareRequest(sources);
				CustomResourceResponseWrapper<?> responseWrapper = processor.execute(customPagesRequest.getAppDetails(),
					request);
				processor.mapToPage(responseWrapper, thisPageFormBean);
			});
	}

	public static void mergeData(CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean, DynamicFormBean outputBean) {
		customPagesRequest.getFieldsOnPage().forEach(pageField -> {
			String fieldName = pageField.getName();
			Object value = formInputBean.getItems().containsKey(fieldName) ? formInputBean.getItems().get(fieldName) :
							   pageField.getDefaultValue();
			outputBean.initialiseField(fieldName, value);
		});
		Map<String, Boolean> services = outputBean.getServices();
		services.putAll(formInputBean.getServices());
		outputBean.setServices(services);
		RequestResult requestResult;
		if (formInputBean.getRequestResult() != null) {
			requestResult = formInputBean.getRequestResult();
		} else {
			requestResult = new ProfileRequestResult(new ProfileRequest(""));
		}
		outputBean.setRequestResult(requestResult);
	}

	// Merge overload for a page that uses query parameters
	public static void mergeData(CustomPagesRequest customPagesRequest, QueryParameters queryParameters, DynamicFormBean outputBean) {
		customPagesRequest.getFieldsOnPage().forEach(pageField -> {
			String fieldName = pageField.getName();
			Object value = queryParameters.hasParameter(fieldName) ? queryParameters.getParameterAsString(fieldName) :
							   pageField.getDefaultValue();
			outputBean.initialiseField(fieldName, value);
		});
		/*Map<String, Boolean> services = outputBean.getServices();
		services.putAll(formInputBean.getServices());
		outputBean.setServices(services);*/
		/*RequestResult requestResult;
		if (formInputBean.getRequestResult() != null) {
			requestResult = formInputBean.getRequestResult();
		} else {
			requestResult = ;
		}*/
		outputBean.setRequestResult(new ProfileRequestResult(new ProfileRequest("")));
	}

	public static GetCustomResourceRequest getCustomResourceRequest(CustomPagesRequest customPagesRequest) {
		AppPage currentPage = customPagesRequest.getCurrentPage();
		GetCustomResourceRequest getCustomResourceRequest = GetCustomResourceRequest.builder()
			.customResource(currentPage.getCustomResource()).numberAsString(currentPage.getNumbersAsString()).build();
		QueryParameters queryParameters = customPagesRequest.getQueryParams();
		currentPage.getQueryParameterMapping()
			.forEach((key, value) ->
						 getCustomResourceRequest.addParameter(value, queryParameters.getParameterFirstValue(key)));
		return getCustomResourceRequest;
	}

	// called by a Controller which is not initialised with a profile.
	public static ProfileRequest prepareRequest(CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean) {
		AppPage currentPage = customPagesRequest.getCurrentPage();
		ProfileRequest profileRequest = new ProfileRequest(formInputBean.getCustomerUniqueId());
		customPagesRequest.getProcessingActions(formInputBean)
			.stream()
			.filter(Objects::nonNull)
			.forEach(actionProcessor -> actionProcessor.process(currentPage, formInputBean, profileRequest));
		return profileRequest;
	}

	// called by a controller which is initialised with a profile.
	public static ProfileRequest prepareRequest(CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean,
												ProfileResponse profileResponse) {
		AppPage currentPage = customPagesRequest.getCurrentPage();
		ProfileRequest profileRequest = new ProfileRequest(profileResponse == null ? "" : profileResponse.getUniqueId());
		customPagesRequest.getProcessingActions(profileResponse, formInputBean)
			.stream()
			.filter(Objects::nonNull)
			.forEach(actionProcessor -> actionProcessor.process(currentPage, formInputBean, profileResponse, profileRequest));
		return profileRequest;
	}

	public static ProfileRequest prepareRequest(CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean,
													   CustomResourceResponse customResourceResponse) {
		AppPage currentPage = customPagesRequest.getCurrentPage();
		ProfileRequest profileRequest = new ProfileRequest(formInputBean.getCustomerUniqueId());
		customPagesRequest.getProcessingActions(customResourceResponse, formInputBean)
			.stream()
			.filter(Objects::nonNull)
			.forEach(actionProcessor -> actionProcessor.process(currentPage, formInputBean, customResourceResponse, profileRequest));
		return profileRequest;
	}

	public static CustomResourceRequest prepareCustomResourceRequest(CustomPagesRequest customPagesRequest,
																	 DynamicFormBean formInputBean) {
		AppPage currentPage = customPagesRequest.getCurrentPage();
		CustomResourceRequest request = CustomResourceRequest.builder()
			.customResource(currentPage.getCustomResource())
			.action(ProcessingAction.CREATE_CUSTOM_RESOURCE)
			.numberAsString(currentPage.getNumbersAsString())
			.build();
		customPagesRequest.getProcessingActions(formInputBean)
			.stream()
			.filter(Objects::nonNull)
			.forEach(actionProcessor -> actionProcessor.process(currentPage, request, null, formInputBean));
		return request;
	}

	public static CustomResourceRequest updateCustomResourceRequest(CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean,
																	CustomResourceResponse customResourceResponse) {
		AppPage currentPage = customPagesRequest.getCurrentPage();
		CustomResourceRequest request = CustomResourceRequest.builder()
			.customResource(currentPage.getCustomResource())
			.action(ProcessingAction.UPDATE_CUSTOM_RESOURCE)
			.numberAsString(currentPage.getNumbersAsString())
			.build();
		customPagesRequest.getProcessingActions(customResourceResponse, formInputBean)
			.stream()
			.filter(Objects::nonNull)
			.forEach(actionProcessor -> actionProcessor.process(currentPage, request, customResourceResponse, formInputBean));
		return request;
	}

	public static void errorView(ViewNameService viewNameService, CustomPagesRequest customPagesRequest,
								 Map<String, Object> model, RedirectAttributes redirectAttributes, ModelAndView result) {
		String viewName = viewNameService.deriveErrorView(customPagesRequest);
		if (isRedirect(viewName)) {
			model.forEach(redirectAttributes::addFlashAttribute);
		} else {
			result.addAllObjects(model);
		}
		result.setViewName(viewName);
	}

	public static void errorView(ViewNameService viewNameService, CustomPagesRequest customPagesRequest, Exception ex,
								 Map<String, Object> model, RedirectAttributes redirectAttributes, ModelAndView result) {
		String viewName = viewNameService.deriveErrorView(customPagesRequest);
		String errorMsg = "";
		String stackTrace = "";
		if(ex!=null){
			if(ex.getMessage()!=null)errorMsg=ex.getMessage();
			stackTrace = Throwables.getStackTraceAsString(ex);
			if(StringUtils.isEmpty(stackTrace))stackTrace="";
		}
		if (isRedirect(viewName)) {
			model.forEach(redirectAttributes::addFlashAttribute);
			redirectAttributes.addFlashAttribute(EXCEPTION_MSG,errorMsg);
			redirectAttributes.addFlashAttribute(EXCEPTION_TRACE,stackTrace);
		} else {
			result.addAllObjects(model);
			result.addObject(EXCEPTION_MSG,errorMsg);
			result.addObject(EXCEPTION_TRACE,stackTrace);
		}

		result.setViewName(viewName);
	}

	public static boolean isRedirect(String view) {
		return view.startsWith("redirect:/");
	}

	public static void logFields(Logger LOG, String sourceName, Map<String, Object> data) {
		if (LOG.isDebugEnabled()) {
			data.forEach((key, value) -> LOG.debug("fields: {}: key: {}, value: {}", sourceName, key, value));

		}
	}

	public static void logFormBean(Logger LOG, String sourceName, DynamicFormBean dynamicFormBean) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("brand: {}", dynamicFormBean.getBrand());
			dynamicFormBean.getItems()
				.forEach((key, value) -> LOG.debug("{}: fields: key: {}, value: {}", sourceName, key, value));
			dynamicFormBean.getServices()
				.forEach((key, value) -> LOG.debug("{}: services: name: {}, subscribed: {}", sourceName, key, value));
		}
	}

	public static void addToSession(CustomPagesRequest customPagesRequest, HttpSession session, Map<String, Object> items) {
		if (customPagesRequest.useSessionStorage()) {
			session.setAttribute(APP_ID, customPagesRequest.getAppDetails().getAppId());
			items.forEach(session::setAttribute);
		}
	}

	public static InputObjectStatus checkInput(CustomPagesRequest customPagesRequest, HttpSession session,
									ViewNameService viewNameService, Map<String, Object> itemsIn) {
		// Check if the expected input is present (not null) in the itemsIn map
		boolean inputFound =  itemsIn.entrySet().stream()
								  .map(ControllerUtils::checkObject)
								  .reduce(Boolean::logicalAnd)
								  .orElse(false);
		if (inputFound) {
			return InputObjectStatus.valid(itemsIn);
		}
		List<String> itemNames = new ArrayList<>(itemsIn.keySet());
		Map<String, Object> itemsOut = new HashMap<>();
		if (customPagesRequest.useSessionStorage()) {
			itemNames.forEach(name -> itemsOut.put(name, null));
			AppDetails.AppDetailsId sessionAppId = (AppDetails.AppDetailsId) session.getAttribute(APP_ID);
			// compare the appId from the current request with that stored in the session
			if (null != sessionAppId && !sessionAppId.equals(customPagesRequest.getAppDetails().getAppId())) {
				// if the appIds do not match remove stored data from session as it is no longer valid
				List.of(APP_ID, DYNAMIC_FORM_BEAN, PROFILE_RESPONSE_WRAPPER).forEach(session::removeAttribute);
			}
			// now see if any of the required input is stored in the session and, if so, add it to the output map
			itemNames.forEach(name -> {
				if (null != session.getAttribute(name)) {
					itemsOut.put(name, session.getAttribute(name));
				}
			});
		}
		// Now check if the required data is present in the outputMap
		inputFound =  itemsOut.entrySet().stream()
						  .map(ControllerUtils::checkObject)
						  .reduce(Boolean::logicalAnd)
						  .orElse(false);
		if (inputFound) {
			return InputObjectStatus.valid(itemsOut);
		}
		// The required input is not present so we will display the missingInputView
		String viewName = viewNameService.deriveMissingInputView(customPagesRequest);
		return InputObjectStatus.invalid(viewName, itemsOut);
	}

	private static boolean checkObject(Map.Entry<String, Object> entry) {
		String name = entry.getKey();
		Object value = entry.getValue();
		if (value == null) {
			return false;
		}
		// ProfileResponseWrapper wraps a ProfileRequestResult, which is what we are really interested in,
		// hence the convoluted check here.  Likewise for CustomResourceResponseWrapper.
		if (name.equals(PROFILE_RESPONSE_WRAPPER)) {
			ProfileResponseWrapper pwr = (ProfileResponseWrapper) value;
			return pwr.getProfileRequestResult() != null;
		}
		if (name.equals(CUSTOM_RESOURCE_RESPONSE_WRAPPER)) {
			CustomResourceResponseWrapper<?> crwr = (CustomResourceResponseWrapper<?>) value;
			return crwr.getCustomResourceRequestResult() != null;
		}
		return true;
	}

	private ControllerUtils() {

	}

	@Value
	@AllArgsConstructor
	public static class InputObjectStatus {
		String view;

		boolean redirect;

		boolean inputMissing;

		Map<String, Object> inputObjects = new HashMap<>();

		void addInputObject(Map<String, Object> inputObjects) {
			this.inputObjects.putAll(inputObjects);
		}

		public Object getObject(String name) {
			return inputObjects.getOrDefault(name, null);
		}

		static InputObjectStatus valid(Map<String, Object> inputObjects) {
			InputObjectStatus result = new InputObjectStatus("", false, false);
			result.addInputObject(inputObjects);
			return result;
		}

		static InputObjectStatus invalid(String viewName, Map<String, Object> inputObjects) {
			InputObjectStatus result = new InputObjectStatus(viewName, ControllerUtils.isRedirect(viewName), true);
			result.addInputObject(inputObjects);
			return result;
		}
	}
}
