package com.dataaxle.pts.acscustompages.service.loadaction;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.FindCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.loadaction.ValueProvider;

/**
 * This interface defines the contract for a class that implements a Load Action for an AppPage.
 * @param <T>
 */
public interface LoadActionProcessor<T extends CustomResourceResponse> {

	/**
	 * This method prepares the request to submit to the Microservices API based upon the configuration provided to the
	 * LoadActionProcessor when it was constructed
	 * @param sources
	 * @return
	 */
	FindCustomResourceRequest prepareRequest(ValueProvider... sources);

	/**
	 * This method executes the request
	 * @param appDetails
	 * @param request
	 * @return
	 */
	CustomResourceResponseWrapper<T> execute(AppDetails appDetails, FindCustomResourceRequest request);

	/**
	 * This method maps the response to the request to the fields on the page being loaded
	 * @param customResourceResponseWrapper
	 * @param thisPageFormBean
	 */
	void mapToPage(CustomResourceResponseWrapper<?> customResourceResponseWrapper, DynamicFormBean thisPageFormBean);

	interface Builder {
		Builder build();
	}
}
