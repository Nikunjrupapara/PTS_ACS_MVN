package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ViewType;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface ViewNameService {

	@Deprecated
	String deriveViewName(HttpServletRequest request);

	@Deprecated
	String deriveViewName(HttpServletRequest request, String target);

	@Deprecated
	String deriveViewName(AppDetails appDetails);

	@Deprecated
	String deriveViewName(AppDetails appDetails, String target);

	String deriveViewName(CustomPagesRequest customPagesRequest, ViewType viewType);

	@Deprecated
	String deriveViewName(CustomPagesRequest customPagesRequest, ViewType viewType, boolean redirect);

	String deriveThisPage(CustomPagesRequest customPagesRequest);

	String deriveEntryPage(CustomPagesRequest customPagesRequest);

	@Deprecated
	String deriveEntryPage(CustomPagesRequest customPagesRequest, boolean redirect);

	String deriveMissingInputView(CustomPagesRequest customPagesRequest);

	String deriveSuccessView(CustomPagesRequest customPagesRequest);

	@Deprecated
	String deriveSuccessView(CustomPagesRequest customPagesRequest, boolean redirect);

	@Deprecated
	String deriveSuccessView(CustomPagesRequest customPagesRequest, List<ProcessingAction> processingActions, boolean redirect);

	String deriveSuccessView(CustomPagesRequest customPagesRequest, DynamicFormBean formInput);

	String deriveSuccessView(CustomPagesRequest customPagesRequest, ProfileResponse profileResponse, DynamicFormBean formInput);

	String deriveSuccessView(CustomPagesRequest customPagesRequest, DynamicFormBean formInput, CustomResourceResponse customResourceResponse);

	String deriveFailureView(CustomPagesRequest customPagesRequest);

	@Deprecated
	String deriveFailureView(CustomPagesRequest customPagesRequest, boolean redirect);

	String deriveFailureView(CustomPagesRequest customPagesRequest, DynamicFormBean formInput);

	String deriveFailureView(CustomPagesRequest customPagesRequest, ProfileResponse profileResponse, DynamicFormBean formInput);

	String deriveFailureView(CustomPagesRequest customPagesRequest, DynamicFormBean formInput, CustomResourceResponse customResourceResponse);

	String deriveErrorView(CustomPagesRequest customPagesRequest);

	@Deprecated
	String deriveErrorView(CustomPagesRequest customPagesRequest, boolean redirect);

}
