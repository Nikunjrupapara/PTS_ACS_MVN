package com.dataaxle.pts.acscustompages.controller;

import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.CUSTOM_PAGES_REQUEST;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.CUSTOM_RESOURCE_RESPONSE_WRAPPER;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.DYNAMIC_FORM_BEAN;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.EXCEPTION_MSG;

import com.dataaxle.pts.acscustompages.authentication.CurrentApp;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResults;
import com.dataaxle.pts.acscustompages.service.CustomResourceService;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import com.dataaxle.pts.acscustompages.service.validation.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class CustomResourceController {

	private final ViewNameService viewNameService;

	private final ValidationService validationService;

	private final CustomResourceService customResourceService;

	public CustomResourceController(ViewNameService viewNameService, ValidationService validationService,
									CustomResourceService customResourceService) {
		this.viewNameService = viewNameService;
		this.validationService = validationService;
		this.customResourceService = customResourceService;
	}

	@GetMapping(value = ControllerUtils.CREATE_CUSTOM_RESOURCE_PATH + ControllerUtils.MULTIPLE_PATH_SEGMENTS)
	public ModelAndView display(@CurrentApp CustomPagesRequest customPagesRequest,
								@ModelAttribute(DYNAMIC_FORM_BEAN) DynamicFormBean fromPrevPage,
								@ModelAttribute(CUSTOM_RESOURCE_RESPONSE_WRAPPER)
								CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper,
								final HttpSession httpSession, final RedirectAttributes redirectAttributes) {
		ModelAndView result = new ModelAndView();
		Map<String, Object> inputObjects = Map.of(
			CUSTOM_RESOURCE_RESPONSE_WRAPPER, customResourceResponseWrapper,
			DYNAMIC_FORM_BEAN, fromPrevPage
		);
		ControllerUtils.InputObjectStatus inputStatus = ControllerUtils.checkInput(customPagesRequest, httpSession,
			viewNameService, inputObjects);
		LOG.debug("inputStatus: {}", inputStatus);
		if (inputStatus.isInputMissing()) {
			DynamicFormBean output = new DynamicFormBean(customPagesRequest);
			output.setError(true);
			if (!inputStatus.isRedirect()) {
				result.addObject(DYNAMIC_FORM_BEAN, output);
			} else {
				redirectAttributes.addFlashAttribute(DYNAMIC_FORM_BEAN, output);
			}
			result.setViewName(inputStatus.getView());
			LOG.debug("result: {}", result);
			return result;
		}
		customResourceResponseWrapper = (CustomResourceResponseWrapper<GetCustomResourceResponse>) inputStatus.getObject(CUSTOM_RESOURCE_RESPONSE_WRAPPER);
		fromPrevPage = (DynamicFormBean) inputStatus.getObject(DYNAMIC_FORM_BEAN);
		DynamicFormBean thisPageFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, thisPageFormBean, customResourceResponseWrapper, fromPrevPage);
		result.addObject(DYNAMIC_FORM_BEAN, thisPageFormBean);
		result.setViewName(viewNameService.deriveThisPage(customPagesRequest));
		ControllerUtils.logFields(LOG, DYNAMIC_FORM_BEAN, thisPageFormBean.getItems());
		LOG.debug("result: {}", result);
		return result;
	}

	@PostMapping(value = {ControllerUtils.CREATE_CUSTOM_RESOURCE_PATH + ControllerUtils.MULTIPLE_PATH_SEGMENTS})
	public ModelAndView submit(@CurrentApp CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean,
							   final HttpSession session, final RedirectAttributes redirectAttributes) {
		ModelAndView result = new ModelAndView();
		Map<String, Object> formInput = formInputBean.getItems();
		ControllerUtils.logFields(LOG, "formInput", formInput);
		List<PageField<?>> fieldsOnPage = customPagesRequest.getFieldsOnPage();

		DynamicFormBean validationResult = new DynamicFormBean(customPagesRequest);
		ValidationResults validationResults = validationService.validate(fieldsOnPage, formInput);
		if (validationResults.hasErrors()) {
			validationResult.setValidationResults(validationResults);
			ControllerUtils.mergeData(customPagesRequest, formInputBean, validationResult);
			result.addObject(DYNAMIC_FORM_BEAN, validationResult);
			result.setViewName(viewNameService.deriveThisPage(customPagesRequest));
			LOG.debug("result: {}", result);
			return result;
		}

		CustomResourceRequest customResourceRequest = ControllerUtils.prepareCustomResourceRequest(customPagesRequest,
			formInputBean);

		try {
			LOG.debug("{}", customResourceRequest);
			CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper =
				customResourceService.create(customPagesRequest.getAppDetails(), customResourceRequest);
			if (!customResourceResponseWrapper.isSuccess()) {
				String msg = customResourceResponseWrapper.getErrorMessage();
				LOG.error("An error occurred! {}", msg);
				ControllerUtils.errorView(viewNameService, customPagesRequest, Map.of(
					DYNAMIC_FORM_BEAN, formInputBean,
					CUSTOM_PAGES_REQUEST, customPagesRequest,
					EXCEPTION_MSG, msg
				), redirectAttributes, result);
				LOG.debug("result: {}", result);
				return result;
			}
			redirectAttributes.addFlashAttribute(CUSTOM_RESOURCE_RESPONSE_WRAPPER, customResourceResponseWrapper);
			redirectAttributes.addFlashAttribute(DYNAMIC_FORM_BEAN, formInputBean);
			String viewName = viewNameService.deriveSuccessView(customPagesRequest, formInputBean,
				customResourceResponseWrapper.getCustomResourceResponse());
			result.setViewName(viewName);
			LOG.debug("result: {}", result);
			ControllerUtils.addToSession(customPagesRequest, session,
				Map.of(
					CUSTOM_RESOURCE_RESPONSE_WRAPPER, customResourceResponseWrapper,
					DYNAMIC_FORM_BEAN, formInputBean
				));
			return result;
		} catch (Exception e) {
			LOG.error("An error occurred", e);
			DynamicFormBean formOutputBean = new DynamicFormBean(customPagesRequest);
			formOutputBean.setError(true);
			ControllerUtils.mergeData(customPagesRequest, formInputBean, formOutputBean);
			Map<String,Object> params =  Map.of(
				DYNAMIC_FORM_BEAN, formOutputBean,
				CUSTOM_PAGES_REQUEST, customPagesRequest
			);
			ControllerUtils.errorView(viewNameService, customPagesRequest, e, params, redirectAttributes, result);
			LOG.debug("result: {}", result);
			ControllerUtils.addToSession(customPagesRequest, session, params);
			return result;
		}
	}

	@GetMapping(value = ControllerUtils.CUSTOM_RESOURCE_CONFIRMATION_PATH + ControllerUtils.MULTIPLE_PATH_SEGMENTS)
	public ModelAndView displayConfirmation(@CurrentApp CustomPagesRequest customPagesRequest,
											@ModelAttribute(DYNAMIC_FORM_BEAN) DynamicFormBean fromPrevPage,
											@ModelAttribute(CUSTOM_RESOURCE_RESPONSE_WRAPPER)
												CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper,
											final HttpSession httpSession, final RedirectAttributes redirectAttributes) {
		ModelAndView result = new ModelAndView();
		Map<String, Object> inputObjects = Map.of(
			CUSTOM_RESOURCE_RESPONSE_WRAPPER, customResourceResponseWrapper,
			DYNAMIC_FORM_BEAN, fromPrevPage
		);
		ControllerUtils.InputObjectStatus inputStatus = ControllerUtils.checkInput(customPagesRequest, httpSession,
			viewNameService, inputObjects);
		LOG.debug("inputStatus: {}", inputStatus);
		if (inputStatus.isInputMissing()) {
			DynamicFormBean output = new DynamicFormBean(customPagesRequest);
			output.setError(true);
			if (!inputStatus.isRedirect()) {
				result.addObject(DYNAMIC_FORM_BEAN, output);
			} else {
				redirectAttributes.addFlashAttribute(DYNAMIC_FORM_BEAN, output);
			}
			result.setViewName(inputStatus.getView());
			LOG.debug("result: {}", result);
			return result;
		}
		customResourceResponseWrapper = (CustomResourceResponseWrapper<GetCustomResourceResponse>) inputStatus.getObject(CUSTOM_RESOURCE_RESPONSE_WRAPPER);
		fromPrevPage = (DynamicFormBean) inputStatus.getObject(DYNAMIC_FORM_BEAN);
		DynamicFormBean thisPageFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, thisPageFormBean, customResourceResponseWrapper, fromPrevPage);
		result.addObject(DYNAMIC_FORM_BEAN, thisPageFormBean);
		result.setViewName(viewNameService.deriveThisPage(customPagesRequest));
		ControllerUtils.logFields(LOG, DYNAMIC_FORM_BEAN, thisPageFormBean.getItems());
		LOG.debug("result: {}", result);
		return result;
	}
}
