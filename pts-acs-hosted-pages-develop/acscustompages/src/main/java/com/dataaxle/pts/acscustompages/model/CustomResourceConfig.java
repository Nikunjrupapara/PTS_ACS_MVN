package com.dataaxle.pts.acscustompages.model;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class CustomResourceConfig implements Serializable {

	private static final long serialVersionUID = 479101516884677660L;

	String name;

	List<CustomResourceFieldConfig> fields;
}
