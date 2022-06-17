package com.dataaxle.pts.acscustompages.controller;

import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.CUSTOM_PAGES_REQUEST;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.DYNAMIC_FORM_BEAN;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.EXCEPTION_MSG;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.EXCEPTION_TRACE;
import static com.dataaxle.pts.acscustompages.controller.ControllerUtils.PROFILE_RESPONSE_WRAPPER;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.LOOKUP_PARAMETER;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.SECURITY_PARAMETER;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.VIEW_PARAMETER;

import com.dataaxle.pts.acscustompages.authentication.CurrentApp;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.service.GetProfileBySecureLinkService;
import com.dataaxle.pts.acscustompages.service.ProfileCompositeService;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = ControllerUtils.GET_PROFILE_SECURELINK_PATH)
public class GetProfileSecureLinkMvc {

    private final GetProfileBySecureLinkService getProfileBySecureLinkService;

    private final ProfileCompositeService profileCompositeService;

    private final ViewNameService viewNameService;

    @Autowired
    public GetProfileSecureLinkMvc(GetProfileBySecureLinkService getProfileBySecureLinkService,
                                   ProfileCompositeService profileCompositeService, ViewNameService viewNameService) {
        this.getProfileBySecureLinkService = getProfileBySecureLinkService;
        this.profileCompositeService = profileCompositeService;
        this.viewNameService = viewNameService;
    }

    @GetMapping(value = ControllerUtils.MULTIPLE_PATH_SEGMENTS)
    public ModelAndView getProfile(@CurrentApp CustomPagesRequest customPagesRequest,
                                   @RequestParam(name=LOOKUP_PARAMETER, required = false) String lookupValue,
                                   @RequestParam(name=SECURITY_PARAMETER, required = false) String securityValue,
                                   @RequestParam(name = VIEW_PARAMETER, required = false) String view,
                                   final RedirectAttributes redirectAttributes) {
        if (lookupValue == null) {
            lookupValue = "";
        }
        if (securityValue == null) {
            securityValue = "";
        }
        LOG.debug("{}={}, {}={}", LOOKUP_PARAMETER, lookupValue, SECURITY_PARAMETER, securityValue);
        ModelAndView result = new ModelAndView();
        AppDetails appDetails = customPagesRequest.getAppDetails();
        DynamicFormBean dynamicFormBean = new DynamicFormBean(customPagesRequest);
        GetProfileRequest getProfileRequest = new GetProfileRequest();
        try {
            getProfileRequest.addParameter(LOOKUP_PARAMETER, lookupValue);
            getProfileRequest.addParameter(SECURITY_PARAMETER, securityValue);
            ProfileResponseWrapper profileResponseWrapper =
                getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, view);

            ProfileResponse foundProfile = profileResponseWrapper.getProfileResponse();
            ProfileRequest profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, foundProfile);
            if (profileRequest.hasActions()) {
                try {
                    profileResponseWrapper = profileCompositeService.process(customPagesRequest.getAppDetails(), profileRequest);
                    if (!profileResponseWrapper.isAllSuccess()) {
                        String msg = profileResponseWrapper.getProfileRequestResult().getErrorMessage();
                        LOG.error("An error occurred! {}", msg);
                        ControllerUtils.errorView(viewNameService, customPagesRequest, Map.of(
                            CUSTOM_PAGES_REQUEST, customPagesRequest,
                            EXCEPTION_MSG, msg
                        ), redirectAttributes, result);
                        LOG.debug("result: {}", result);
                        return result;
                    }
                } catch (Exception ex) {
                    LOG.error("An error occurred!", ex);
                    Map<String,Object> params =  Map.of(
                            CUSTOM_PAGES_REQUEST, customPagesRequest
                    );
                    ControllerUtils.errorView(viewNameService, customPagesRequest,ex, params, redirectAttributes, result);
                    LOG.debug("result: {}", result);
                    return result;
                }
            }
            ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);
            String viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponseWrapper.getProfileResponse(),
                dynamicFormBean);
            if (viewName.startsWith("redirect:")) {
                redirectAttributes.addFlashAttribute(PROFILE_RESPONSE_WRAPPER, profileResponseWrapper);
                redirectAttributes.addFlashAttribute(DYNAMIC_FORM_BEAN, dynamicFormBean);
            } else {
                result.addObject(PROFILE_RESPONSE_WRAPPER, profileResponseWrapper);
                result.addObject(DYNAMIC_FORM_BEAN, dynamicFormBean);
            }
            result.setViewName(viewName);
            LOG.debug("result: {}", result);
            return result;
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute(PROFILE_RESPONSE_WRAPPER,
                new ProfileResponseWrapper(getProfileRequest, true, false));
            result.setViewName(viewNameService.deriveFailureView(customPagesRequest));
            LOG.debug("result: {}", result);
            return result;
        } catch (ServerErrorException e) {
            LOG.error(e.getMessage(), e);
            Map<String,Object> params =  Map.of(
                    CUSTOM_PAGES_REQUEST, customPagesRequest
            );
            ControllerUtils.errorView(viewNameService, customPagesRequest, e, params, redirectAttributes, result);
            LOG.debug("result: {}", result);
            return result;
        }
    }
}
