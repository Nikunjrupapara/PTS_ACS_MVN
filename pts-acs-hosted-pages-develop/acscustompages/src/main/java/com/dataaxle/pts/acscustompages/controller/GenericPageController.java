package com.dataaxle.pts.acscustompages.controller;

import com.dataaxle.pts.acscustompages.authentication.CurrentApp;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.CONFIRMATION_PAGE_PATH;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.DYNAMIC_FORM_BEAN;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.ERROR_PAGE_PATH;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.GENERIC_PAGE_PATH;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.MULTIPLE_PATH_SEGMENTS;

@Slf4j
@Controller
public class GenericPageController {

	private final ViewNameService viewNameService;

	@Autowired
	public GenericPageController(ViewNameService viewNameService) {
		this.viewNameService = viewNameService;
	}

	@GetMapping(value = CONFIRMATION_PAGE_PATH + MULTIPLE_PATH_SEGMENTS)
	public ModelAndView displayConfirmationPage(@CurrentApp CustomPagesRequest customPagesRequest,
								@ModelAttribute(DYNAMIC_FORM_BEAN)DynamicFormBean fromPrevPage) {
		ModelAndView result = new ModelAndView();
		DynamicFormBean dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, fromPrevPage, dynamicFormBean);
		result.addObject(DYNAMIC_FORM_BEAN, dynamicFormBean);
		result.setViewName(viewNameService.deriveThisPage(customPagesRequest));
		LOG.debug("result: {}", result);
		return result;
	}

	@GetMapping(value = ERROR_PAGE_PATH + MULTIPLE_PATH_SEGMENTS)
	public ModelAndView displayErrorPage(@CurrentApp CustomPagesRequest customPagesRequest,
										 @ModelAttribute(DYNAMIC_FORM_BEAN)DynamicFormBean fromPrevPage) {
		ModelAndView result = new ModelAndView();
		DynamicFormBean dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, fromPrevPage, dynamicFormBean);
		result.addObject(DYNAMIC_FORM_BEAN, dynamicFormBean);
		result.setViewName(viewNameService.deriveThisPage(customPagesRequest));
		LOG.debug("result: {}", result);
		return result;
	}

	@GetMapping(value = GENERIC_PAGE_PATH + MULTIPLE_PATH_SEGMENTS)
	public ModelAndView displayGenericPage(@CurrentApp CustomPagesRequest customPagesRequest,
												@ModelAttribute(DYNAMIC_FORM_BEAN)DynamicFormBean fromPrevPage) {
		ModelAndView result = new ModelAndView();
		DynamicFormBean dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, fromPrevPage, dynamicFormBean);
		result.addObject(DYNAMIC_FORM_BEAN, dynamicFormBean);
		result.setViewName(viewNameService.deriveThisPage(customPagesRequest));
		LOG.debug("result: {}", result);
		return result;
	}
}
