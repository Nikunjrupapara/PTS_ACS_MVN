package com.dataaxle.pts.acscustompages.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomResourceResponseWrapper<T extends CustomResourceResponse> implements Serializable {

	private static final long serialVersionUID = -7177391304937275483L;

	CustomResourceRequestResult customResourceRequestResult;

	T customResourceResponse;

	public CustomResourceResponseWrapper() {

	}

	public CustomResourceResponseWrapper(GetCustomResourceRequest getCustomResourceRequest,
										 T customResourceResponse) {
		this.customResourceRequestResult = new CustomResourceRequestResult(getCustomResourceRequest);
		this.customResourceResponse = customResourceResponse;
	}

	public CustomResourceResponseWrapper(ListCustomResourceRequest listCustomResourceRequest,
										 T customResourceResponse) {
		this.customResourceRequestResult = new CustomResourceRequestResult(listCustomResourceRequest);
		this.customResourceResponse = customResourceResponse;

	}

	public CustomResourceResponseWrapper(GetCustomResourceRequest getCustomResourceRequest, boolean lookup, boolean found) {
		this.customResourceRequestResult = new CustomResourceRequestResult(getCustomResourceRequest, lookup, found);
	}

	public CustomResourceResponseWrapper(GetCustomResourceRequest getCustomResourceRequest, boolean lookup, boolean found,
										 boolean error) {
		this.customResourceRequestResult = new CustomResourceRequestResult(getCustomResourceRequest, lookup, found, error);
	}

	public CustomResourceResponseWrapper(CustomResourceRequest customResourceRequest, T customResourceResponse) {
		this.customResourceRequestResult = new CustomResourceRequestResult(customResourceRequest);
		this.customResourceResponse = customResourceResponse;
	}

	public boolean hasResult() { return customResourceRequestResult != null; }

	public boolean hasResponse() {
		return customResourceResponse != null;
	}

	public boolean isSuccess() {
		return customResourceRequestResult.isAllSuccess();
	}

	public String getErrorMessage() { return customResourceRequestResult.getErrorMessage(); }

	public void setCustomResourceSuccess() { customResourceRequestResult.setCustomResourcesSuccess(true); }

	public CustomResourceRecord getRecord() {
		if (customResourceResponse != null) {
			return customResourceResponse.getRecord();
		}
		return new CustomResourceRecord();
	}
}
