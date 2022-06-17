package com.dataaxle.pts.acscustompages.controller;

import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.CUSTOM_PAGES_REQUEST;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.DYNAMIC_FORM_BEAN;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.EXCEPTION_MSG;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.EXCEPTION_TRACE;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.PROFILE_RESPONSE_WRAPPER;

import com.dataaxle.pts.acscustompages.authentication.CurrentApp;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResults;
import com.dataaxle.pts.acscustompages.service.ProfileCompositeService;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.dataaxle.pts.acscustompages.service.validation.ValidationService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping(ControllerUtils.UPDATE_SERVICES_PATH)
public class UpdateServicesController {

	private final ViewNameService viewNameService;

	private final ValidationService validationService;

	private final ProfileCompositeService profileCompositeService;

	public UpdateServicesController(ViewNameService viewNameService, ValidationService validationService,
									ProfileCompositeService profileCompositeService) {
		this.viewNameService = viewNameService;
		this.validationService = validationService;
		this.profileCompositeService = profileCompositeService;
	}

	@GetMapping(value = ControllerUtils.MULTIPLE_PATH_SEGMENTS)
	public ModelAndView displayPage(@CurrentApp CustomPagesRequest customPagesRequest,
									@ModelAttribute(PROFILE_RESPONSE_WRAPPER)ProfileResponseWrapper profileResponseWrapper,
									final HttpSession session, final RedirectAttributes redirectAttributes) {
		ModelAndView result = new ModelAndView();
		Map<String, Object> inputObjects = Map.of(PROFILE_RESPONSE_WRAPPER, profileResponseWrapper);
		ControllerUtils.InputObjectStatus inputStatus = ControllerUtils.checkInput(customPagesRequest, session, viewNameService,
			inputObjects);
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
		profileResponseWrapper = (ProfileResponseWrapper) inputStatus.getObject(PROFILE_RESPONSE_WRAPPER);
		DynamicFormBean dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);
		ControllerUtils.logFormBean(LOG, "displayPage", dynamicFormBean);
		result.addObject(DYNAMIC_FORM_BEAN, dynamicFormBean);
		result.setViewName(viewNameService.deriveThisPage(customPagesRequest));
		LOG.debug("result: {}", result);
		ControllerUtils.addToSession(customPagesRequest, session, Map.of(PROFILE_RESPONSE_WRAPPER, profileResponseWrapper));
		return result;
	}

	@PostMapping(value = ControllerUtils.MULTIPLE_PATH_SEGMENTS)
	public ModelAndView submit(@CurrentApp CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean,
							   final RedirectAttributes redirectAttributes) {
		ModelAndView result = new ModelAndView();
		ControllerUtils.logFormBean(LOG, "formInputBean", formInputBean);
		List<PageField<?>> fieldsOnPage = customPagesRequest.getFieldsOnPage();

		ValidationResults validationResults = validationService.validate(fieldsOnPage, formInputBean.getItems());
		if (validationResults.hasErrors()) {
			DynamicFormBean validationBean = new DynamicFormBean(customPagesRequest);
			validationBean.setValidationResults(validationResults);
			ControllerUtils.mergeData(customPagesRequest, formInputBean, validationBean);
			result.addObject(DYNAMIC_FORM_BEAN, validationBean);
			result.setViewName(viewNameService.deriveThisPage(customPagesRequest));
			LOG.debug("result: {}", result);
			return result;
		}

		ProfileRequest profileRequest = ControllerUtils.prepareRequest(customPagesRequest, formInputBean);
		try {
			ProfileResponseWrapper profileResponseWrapper = profileCompositeService.process(customPagesRequest.getAppDetails(), profileRequest);
			if (!profileResponseWrapper.isAllSuccess()) {
				String msg = profileResponseWrapper.getProfileRequestResult().getErrorMessage();
				LOG.error("An error occurred! {}", msg);
				ControllerUtils.errorView(viewNameService, customPagesRequest, Map.of(
					DYNAMIC_FORM_BEAN, formInputBean,
					CUSTOM_PAGES_REQUEST, customPagesRequest,
					EXCEPTION_MSG, msg
				), redirectAttributes, result);
				LOG.debug("result: {}", result);
				return result;
			}
			redirectAttributes.addFlashAttribute(PROFILE_RESPONSE_WRAPPER, profileResponseWrapper);
			result.setViewName(viewNameService.deriveSuccessView(customPagesRequest, profileResponseWrapper.getProfileResponse(), formInputBean));
			return result;
		} catch (Exception e) {
			LOG.error("An error occurred!", e);
			DynamicFormBean formOutputBean = new DynamicFormBean(customPagesRequest);
			formOutputBean.setError(true);
			ControllerUtils.mergeData(customPagesRequest, formInputBean, formOutputBean);
			Map<String,Object> params =  Map.of(
					DYNAMIC_FORM_BEAN, formOutputBean,
					CUSTOM_PAGES_REQUEST, customPagesRequest
			);
			ControllerUtils.errorView(viewNameService, customPagesRequest, e, params, redirectAttributes, result);
			LOG.debug("result: {}", result);
			return result;
		}
	}
}
