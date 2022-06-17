package com.dataaxle.pts.acscustompages.controller;

import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.CUSTOM_PAGES_REQUEST;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.DYNAMIC_FORM_BEAN;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.EXCEPTION_MSG;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.PROFILE_RESPONSE_WRAPPER;

import com.dataaxle.pts.acscustompages.authentication.CurrentApp;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResults;
import com.dataaxle.pts.acscustompages.service.GetCustomResourceByIdService;
import com.dataaxle.pts.acscustompages.service.ProfileCompositeService;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.dataaxle.pts.acscustompages.service.validation.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(value = ControllerUtils.GET_CUSTOM_RESOURCE_BY_ID_PATH)
public class GetCustomResourceById {

	private final ViewNameService viewNameService;

	private final GetCustomResourceByIdService getCustomResourceByIdService;

	private final ProfileCompositeService profileCompositeService;

	private final ValidationService validationService;

	@Autowired
	public GetCustomResourceById(ViewNameService viewNameService, GetCustomResourceByIdService getCustomResourceByIdService,
								 ProfileCompositeService profileCompositeService, ValidationService validationService) {
		this.viewNameService = viewNameService;
		this.getCustomResourceByIdService = getCustomResourceByIdService;
		this.profileCompositeService = profileCompositeService;
		this.validationService = validationService;
	}

	@GetMapping(value = ControllerUtils.MULTIPLE_PATH_SEGMENTS)
	public ModelAndView display(@CurrentApp CustomPagesRequest customPagesRequest,
								@ModelAttribute(DYNAMIC_FORM_BEAN) DynamicFormBean fromPrevPage,
								@ModelAttribute(PROFILE_RESPONSE_WRAPPER) ProfileResponseWrapper profileResponseWrapper) {
		ModelAndView result = new ModelAndView();
		DynamicFormBean thisPageFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, thisPageFormBean, profileResponseWrapper, fromPrevPage);
		result.addObject(DYNAMIC_FORM_BEAN, thisPageFormBean);
		result.setViewName(viewNameService.deriveThisPage(customPagesRequest));
		LOG.debug("result: {}", result);
		return result;
	}

	@PostMapping(value = ControllerUtils.MULTIPLE_PATH_SEGMENTS)
	public ModelAndView submit(@CurrentApp CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean,
							final RedirectAttributes redirectAttributes) {
		ModelAndView result = new ModelAndView();
		Map<String, Object> formInput = formInputBean.getItems();
		List<PageField<?>> fieldsOnPage = customPagesRequest.getFieldsOnPage();
		ControllerUtils.logFields(LOG, "formInputBean", formInput);

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

		List<String> fieldsToSubmit = customPagesRequest.getFieldsToSubmit();

		GetCustomResourceRequest getCustomResourceRequest =
			new GetCustomResourceRequest(customPagesRequest.getCurrentPage().getCustomResource());
		formInputBean.getItems().entrySet().stream()
			.filter(entry -> fieldsToSubmit.contains(entry.getKey()))
			.forEach(entry -> {
				String fieldName = entry.getKey();
				String fieldValue = (String) entry.getValue();
				LOG.debug("searchItem: fieldName: {}, fieldValue: {}", fieldName, fieldValue);
				getCustomResourceRequest.addParameter(fieldName, fieldValue);
			});

		try {
			// As we have a CustomResourceResponseWrapper, pass it to the next page to use as required
			CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper =
				getCustomResourceByIdService.getCustomResource(customPagesRequest.getAppDetails(), getCustomResourceRequest);
			CustomResourceResponse customResourceResponse = customResourceResponseWrapper.getCustomResourceResponse();
			ProfileRequest profileRequest = ControllerUtils.prepareRequest(customPagesRequest, formInputBean,
				customResourceResponse);
			ProfileResponseWrapper profileResponseWrapper = null;
			if (profileRequest.hasActions()) {
				try {
					profileResponseWrapper = profileCompositeService.process(customPagesRequest.getAppDetails(), profileRequest);
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
				} catch (Exception ex) {
					LOG.error("An error occurred!", ex);
					Map<String,Object> params =  Map.of(
							DYNAMIC_FORM_BEAN, formInputBean,
							CUSTOM_PAGES_REQUEST, customPagesRequest
					);
					ControllerUtils.errorView(viewNameService, customPagesRequest, ex, params, redirectAttributes, result);
					LOG.debug("result: {}", result);
					return result;
				}
			}
			redirectAttributes.addFlashAttribute(PROFILE_RESPONSE_WRAPPER, profileResponseWrapper);
			result.setViewName(viewNameService.deriveSuccessView(customPagesRequest, formInputBean,
				customResourceResponseWrapper.getCustomResourceResponse()));
			LOG.debug("result: {}", result);
			return result;
		} catch (ResourceNotFoundException e) {
			ProfileRequest profileRequest = ControllerUtils.prepareRequest(customPagesRequest, formInputBean, (CustomResourceResponse) null);
			ProfileResponseWrapper profileResponseWrapper = null;
			ProfileResponse profileResponse = null;
			if (profileRequest.hasActions()) {
				try {
					profileResponseWrapper = profileCompositeService.process(customPagesRequest.getAppDetails(), profileRequest);
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
					profileResponse = profileResponseWrapper.getProfileResponse();
				} catch (Exception ex) {
					LOG.error("An error occurred!", ex);
					Map<String,Object> params =  Map.of(
							DYNAMIC_FORM_BEAN, formInputBean,
							CUSTOM_PAGES_REQUEST, customPagesRequest
					);
					ControllerUtils.errorView(viewNameService, customPagesRequest, ex, params, redirectAttributes, result);
					LOG.debug("result: {}", result);
					return result;
				}
			}
			// map ProfileResponse attributes to DynamicFormBean for fields to display on next page
			List<PageField<?>> fieldsOnTargetPage = customPagesRequest.getFieldsOnFailurePage();
			DynamicFormBean nextPageFields = new DynamicFormBean(customPagesRequest);
			ControllerUtils.initialiseFields(fieldsOnTargetPage, formInputBean.getItems(), nextPageFields);
			ControllerUtils.logFields(LOG, "nextPageFields", nextPageFields.getItems());
			redirectAttributes.addFlashAttribute(DYNAMIC_FORM_BEAN, nextPageFields);
			redirectAttributes.addFlashAttribute(PROFILE_RESPONSE_WRAPPER, Objects.requireNonNullElseGet(profileResponseWrapper,
				() -> new CustomResourceResponseWrapper(getCustomResourceRequest, true, false)));
			result.setViewName(viewNameService.deriveFailureView(customPagesRequest, profileResponse, formInputBean));
			LOG.debug("result: {}", result);
			return result;
		} catch (Exception e) {
			LOG.error("An error occurred!", e);
			Map<String,Object> params =  Map.of(
					DYNAMIC_FORM_BEAN, formInputBean,
					CUSTOM_PAGES_REQUEST, customPagesRequest
			);
			ControllerUtils.errorView(viewNameService, customPagesRequest,e, params, redirectAttributes, result);
			LOG.debug("result: {}", result);
			return result;
		}
	}

}
