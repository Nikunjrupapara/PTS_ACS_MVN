package com.dataaxle.pts.acscustompages.controller;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.exception.AppDetailsNotFoundException;
import com.dataaxle.pts.acscustompages.exception.AppPageNotFoundByTypeException;
import com.dataaxle.pts.acscustompages.exception.AppPageNotFoundException;
import com.dataaxle.pts.acscustompages.exception.CustomPagesAuthenticationException;
import com.dataaxle.pts.acscustompages.exception.CustomPagesRequestException;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class PtsErrorController implements ErrorController {

	private final ViewNameService viewNameService;

	@Autowired
	public PtsErrorController(ViewNameService viewNameService) {
		this.viewNameService = viewNameService;
	}

	@RequestMapping("/error")
	public ModelAndView handleError(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		String requestId = (String)httpSession.getAttribute(CustomPagesConstants.MDC_REQUEST_ID);
		boolean hasRequestId = StringUtils.hasText(requestId);
		if (hasRequestId) {
			MDC.put(CustomPagesConstants.MDC_REQUEST_ID, requestId);
		}
		Exception exception = (Exception)request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		ModelAndView result;
		if (exception != null) {
			LOG.error("Handling exception {}, message: {}", exception.getClass().getCanonicalName(), exception.getMessage());
			result = getModelAndView(exception);
		} else {
			result = new ModelAndView("error");
		}
		if (hasRequestId) {
			result.addObject(CustomPagesConstants.MDC_REQUEST_ID, requestId);
		}
		LOG.debug("Returning result: {}", result);
		MDC.clear();
		return result;
	}

	private ModelAndView getModelAndView(Exception e) {
		ModelAndView result = new ModelAndView();
		result.addObject("e", e);
		if (e instanceof AppDetailsNotFoundException) {
			result.setViewName("notfound");
			return result;
		}
		if (e instanceof CustomPagesAuthenticationException) {
			CustomPagesRequest customPagesRequest = ((CustomPagesAuthenticationException) e).getCustomPagesRequest();
			String viewName = viewNameService.deriveErrorView(customPagesRequest);
			if (StringUtils.hasText(viewName)) {
				result.setViewName(viewName);
			} else {
				result.setViewName("unauthorized");
			}
			return result;
		}
		if (e instanceof CustomPagesRequestException) {
			CustomPagesRequest customPagesRequest = ((CustomPagesRequestException) e).getCustomPagesRequest();
			Throwable cause = e.getCause();
			// overwrite the exception in the model with the cause
			result.addObject("e", cause);
			if (cause instanceof AppPageNotFoundException || cause instanceof AppDetailsNotFoundException) {
				result.setViewName("notfound");
			} else {
				try {
					result.setViewName(viewNameService.deriveErrorView(customPagesRequest));
				} catch (AppPageNotFoundByTypeException ex) {
					result.setViewName("error");
				}
			}
			return result;
		}
		LOG.warn("No handler found for class {}", e.getClass().getCanonicalName());
		result.setViewName("error");
		return result;
	}
}
