package com.dataaxle.pts.acscustompages.model;

/**
 * Interface for requests to get a single record or list of records from a custom resource
 */
public interface FindCustomResourceRequest extends BaseCustomResourceRequest {
	String buildUri();

}
