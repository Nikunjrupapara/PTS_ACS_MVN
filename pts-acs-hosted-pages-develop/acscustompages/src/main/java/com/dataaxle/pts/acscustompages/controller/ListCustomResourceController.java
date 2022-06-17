package com.dataaxle.pts.acscustompages.controller;

import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.CUSTOM_PAGES_REQUEST;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.DYNAMIC_FORM_BEAN;

import com.dataaxle.pts.acscustompages.authentication.CurrentApp;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequestResult;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceConfig;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.form.ListDetails;
import com.dataaxle.pts.acscustompages.model.form.QueryParameters;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResults;
import com.dataaxle.pts.acscustompages.service.ListCustomResourceService;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import com.dataaxle.pts.acscustompages.service.validation.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = ControllerUtils.LIST_CUSTOM_RESOURCE_PATH)
public class ListCustomResourceController {

	private final ViewNameService viewNameService;

	private final ValidationService validationService;

	private final ListCustomResourceService listCustomResourceService;

	public ListCustomResourceController(ViewNameService viewNameService, ValidationService validationService, ListCustomResourceService listCustomResourceService) {
		this.viewNameService = viewNameService;
		this.validationService = validationService;
		this.listCustomResourceService = listCustomResourceService;
	}

	@GetMapping(value = ControllerUtils.MULTIPLE_PATH_SEGMENTS)
	public ModelAndView display(@CurrentApp CustomPagesRequest customPagesRequest,
								final RedirectAttributes redirectAttributes) {
		ModelAndView result = new ModelAndView();
		DynamicFormBean thisPageFormBean = new DynamicFormBean(customPagesRequest);
		QueryParameters queryParameters = customPagesRequest.getQueryParams();
		queryParameters.getParameters().forEach((key, value) -> LOG.info("name: {}, value: {}", key, value));
		Map<String, Object> inputParameters = new HashMap<>();
		queryParameters
			.getParameterNames()
			.forEach(parameter -> inputParameters.put(parameter, queryParameters.getParameterFirstValue(parameter)));
		List<PageField<?>> fieldsOnPage = customPagesRequest.getFieldsOnPage();

		DynamicFormBean validationResult = new DynamicFormBean(customPagesRequest);
		ValidationResults validationResults = validationService.validate(fieldsOnPage, inputParameters);
		if (validationResults.hasErrors()) {
			validationResult.setValidationResults(validationResults);
			ControllerUtils.mergeData(customPagesRequest, queryParameters, validationResult);
			result.addObject(DYNAMIC_FORM_BEAN, validationResult);
			result.setViewName(viewNameService.deriveThisPage(customPagesRequest));
			LOG.debug("result: {}", result);
			return result;
		}

		AppPage appPage = customPagesRequest.getCurrentPage();
		ListCustomResourceConfig config = appPage.getListCustomResource();
		ListCustomResourceRequest.Builder builder = ListCustomResourceRequest.builder(config);
		config.getParameterNames().forEach(parameterName -> {
			if (queryParameters.hasParameter(parameterName)) {
				builder.addParameter(parameterName, queryParameters.getParameterAsString(parameterName));
			} else {
				LOG.warn("AppDetails {} page {}/{} expected query parameter {} but it was not present",
					customPagesRequest.getCompany(), customPagesRequest.getContextPath(), appPage.getViewName(),
					parameterName);
			}
		});
		/*
		 Pagination works as follows:
		 The HTML page should send a query parameter named 'page', which contains the value of the page to be
		 displayed.  For example, when displaying a list for the first time the value should be submitted as 1.
		 If page 3 is displayed then the Prev button (to display page 2) should provide a value of 2 and the Next button
		 (to display page 4) should provide a value of 4.  The ListCustomResourceRequest class will use the page
		 value provided in conjunction with the configured page size to calculate the page value to send to the
		 Microservices API.
		 */
		int pageNumber = Integer.parseInt(queryParameters.getParameterFirstValue("page"));
		builder.pageNumber(pageNumber);
		ListCustomResourceRequest listCustomResourceRequest = builder.build();
		LOG.debug("{}", listCustomResourceRequest);
		try {
			CustomResourceResponseWrapper<ListCustomResourceResponse> customResourceResponseWrapper =
				listCustomResourceService.listRecords(customPagesRequest.getAppDetails(), listCustomResourceRequest);
			String viewName;
			if (customResourceResponseWrapper.isSuccess()) {
				ListCustomResourceResponse listCustomResourceResponse = customResourceResponseWrapper.getCustomResourceResponse();
				CustomResourceRequestResult requestResult = customResourceResponseWrapper.getCustomResourceRequestResult();
				ControllerUtils.initialiseForm(customPagesRequest, thisPageFormBean, queryParameters, requestResult);
				ListDetails<CustomResourceRecord> listDetails = ListDetails.of(appPage.getListCustomResource(),
					listCustomResourceResponse);
				thisPageFormBean.setListDetails(listDetails);
				result.addObject(DYNAMIC_FORM_BEAN, thisPageFormBean);
				viewName = viewNameService.deriveThisPage(customPagesRequest);
			} else {
				viewName = viewNameService.deriveErrorView(customPagesRequest);
			}
			result.setViewName(viewName);
			LOG.debug("result: {}", result);
			return result;
		} catch (Exception e) {
			LOG.error("An error occurred!", e);
			Map<String, Object> params = Map.of(
				DYNAMIC_FORM_BEAN, thisPageFormBean,
				CUSTOM_PAGES_REQUEST, customPagesRequest
			);
			ControllerUtils.errorView(viewNameService, customPagesRequest, e, params, redirectAttributes, result);
			LOG.debug("result: {}", result);
			return result;
		}
	}
}
