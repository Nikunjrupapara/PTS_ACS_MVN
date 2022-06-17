package com.dataaxle.pts.acscustompages.service.actions;

import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;

public interface ActionProcessor {

	void process(AppPage page, DynamicFormBean formInput, ProfileRequest profileRequest);

	void process(AppPage appPage, DynamicFormBean formInput, ProfileResponse profileResponse, ProfileRequest profileRequest);

	void process(AppPage appPage, DynamicFormBean formInput, CustomResourceResponse customResourceResponse, ProfileRequest profileRequest);

	/*
	TODO: consider the following refactoring possibility
	This method probably should take an argument of CustomResourceRequests instead of CustomResourceRequest.  This is because
	it would be more consistent with the composition of ProfileRequest and therefore make it easier to define a Request
	interface and have that implemented by ProfileRequest (maybe needs to be renamed to CompositeRequest?).
	I did not go down this path just now as there is no corresponding Microservices Resource to process a request with a
	CustomResourceRequests body so it was more pragmatic to use CustomResourceRequest in the short term.
	 */
	void process(AppPage appPage, CustomResourceRequest customResourceRequest, CustomResourceResponse customResourceResponse,
				 DynamicFormBean formInputBean);

	abstract class Builder<T extends ActionProcessor.Builder<T>> {
		abstract ActionProcessor build();

		protected abstract T self();
	}
}
