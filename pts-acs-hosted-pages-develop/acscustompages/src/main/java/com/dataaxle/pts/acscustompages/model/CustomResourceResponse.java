package com.dataaxle.pts.acscustompages.model;

public interface CustomResourceResponse {

	default Object getField(String fieldName) { return ""; }

	CustomResourceRecord getRecord();

	CustomResourceRecord getRecord(int index);
}
