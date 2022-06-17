package com.dataaxle.pts.acscustompages.controller;

import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.CUSTOM_PAGES_REQUEST;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.CUSTOM_RESOURCE_RESPONSE_WRAPPER;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.DYNAMIC_FORM_BEAN;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.EXCEPTION_MSG;

import com.dataaxle.pts.acscustompages.authentication.CurrentApp;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.service.CustomResourceService;
import com.dataaxle.pts.acscustompages.service.GetCustomResourceByIdService;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping(value = ControllerUtils.GET_CUSTOM_RESOURCE_BY_LINK_PATH)
public class GetCustomResourceBySecureLink {

	private final ViewNameService viewNameService;

	private final GetCustomResourceByIdService getCustomResourceByIdService;

	private final CustomResourceService customResourceService;

	@Autowired
	public GetCustomResourceBySecureLink(ViewNameService viewNameService, GetCustomResourceByIdService getCustomResourceByIdService,
										 CustomResourceService customResourceService) {
		this.viewNameService = viewNameService;
		this.getCustomResourceByIdService = getCustomResourceByIdService;
		this.customResourceService = customResourceService;
	}

	@GetMapping(value = ControllerUtils.MULTIPLE_PATH_SEGMENTS)
	public ModelAndView display(@CurrentApp CustomPagesRequest customPagesRequest,
								@RequestParam Map<String, String> allQueryParameters,
								final RedirectAttributes redirectAttributes, final HttpSession session) {
		ModelAndView result = new ModelAndView();
		DynamicFormBean thisPageFormBean = new DynamicFormBean(customPagesRequest);
		LOG.debug("queryParameters: {}", allQueryParameters);
		AppDetails appDetails = customPagesRequest.getAppDetails();

		GetCustomResourceRequest getRequest = ControllerUtils.getCustomResourceRequest(customPagesRequest);
		try {
			CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper =
				getCustomResourceByIdService.getCustomResource(appDetails, getRequest);

			CustomResourceResponse foundRecord = customResourceResponseWrapper.getCustomResourceResponse();
			CustomResourceRequest customResourceRequest = ControllerUtils.updateCustomResourceRequest(customPagesRequest,
				thisPageFormBean, foundRecord);
			if (customResourceRequest.hasActions()) {
				try  {
					customResourceResponseWrapper = customResourceService.update(appDetails, customResourceRequest);
					if (!customResourceResponseWrapper.isSuccess()) {
						String message = customResourceResponseWrapper.getErrorMessage();
						LOG.error("An error occurred: {}", message);
						ControllerUtils.errorView(viewNameService, customPagesRequest, Map.of(
							CUSTOM_PAGES_REQUEST, customPagesRequest,
							EXCEPTION_MSG, message
						), redirectAttributes, result);
						LOG.debug("result: {}", result);
						return result;
					}
				} catch (Exception e) {
					LOG.error("An error occurred!", e);
					ControllerUtils.errorView(viewNameService, customPagesRequest, e, Map.of(
						CUSTOM_PAGES_REQUEST, customPagesRequest
					), redirectAttributes, result);
					LOG.debug("result: {}", result);
					return result;
				}
			}

			ControllerUtils.initialiseForm(customPagesRequest, thisPageFormBean, customResourceResponseWrapper, null);
			String viewName = viewNameService.deriveSuccessView(customPagesRequest, thisPageFormBean,
				customResourceResponseWrapper.getCustomResourceResponse());
			if (ControllerUtils.isRedirect(viewName)) {
				redirectAttributes.addFlashAttribute(CUSTOM_RESOURCE_RESPONSE_WRAPPER, customResourceResponseWrapper);
				redirectAttributes.addFlashAttribute(DYNAMIC_FORM_BEAN, thisPageFormBean);
			} else {
				result.addObject(CUSTOM_RESOURCE_RESPONSE_WRAPPER, customResourceResponseWrapper);
				result.addObject(DYNAMIC_FORM_BEAN, thisPageFormBean);
			}
			result.setViewName(viewName);
			LOG.debug("result: {}", result);
			ControllerUtils.addToSession(customPagesRequest, session, Map.of(
				CUSTOM_RESOURCE_RESPONSE_WRAPPER, customResourceResponseWrapper,
				DYNAMIC_FORM_BEAN, thisPageFormBean
			));
			return result;
		} catch (ResourceNotFoundException e) {
			CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper =
				new CustomResourceResponseWrapper<>(getRequest, true, false);
			ControllerUtils.initialiseForm(customPagesRequest, thisPageFormBean, customResourceResponseWrapper, null);
			String viewName = viewNameService.deriveFailureView(customPagesRequest, thisPageFormBean,
				customResourceResponseWrapper.getCustomResourceResponse());
			if (ControllerUtils.isRedirect(viewName)) {
				redirectAttributes.addFlashAttribute(CUSTOM_RESOURCE_RESPONSE_WRAPPER, customResourceResponseWrapper);
				redirectAttributes.addFlashAttribute(DYNAMIC_FORM_BEAN, thisPageFormBean);
			} else {
				result.addObject(CUSTOM_RESOURCE_RESPONSE_WRAPPER, customResourceResponseWrapper);
				result.addObject(DYNAMIC_FORM_BEAN, thisPageFormBean);
			}
			result.setViewName(viewName);
			LOG.debug("result: {}", result);
			return result;
		} catch (ServerErrorException e) {
			LOG.error(e.getMessage(), e);
			CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper =
				new CustomResourceResponseWrapper<>(getRequest, true, false, true);
			ControllerUtils.initialiseForm(customPagesRequest, thisPageFormBean, customResourceResponseWrapper, null);
			ControllerUtils.errorView(viewNameService, customPagesRequest, e, Map.of(
				CUSTOM_PAGES_REQUEST, customPagesRequest,
				DYNAMIC_FORM_BEAN, thisPageFormBean
			), redirectAttributes, result);
			LOG.debug("result: {}", result);
			return result;
		}
	}
}
