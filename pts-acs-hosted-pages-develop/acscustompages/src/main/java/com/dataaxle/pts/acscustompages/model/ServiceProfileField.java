package com.dataaxle.pts.acscustompages.model;

public interface ServiceProfileField<T> extends ServiceField<T>  {

	String getProfileName();

	T getAcsValue(String fieldValue);
}
