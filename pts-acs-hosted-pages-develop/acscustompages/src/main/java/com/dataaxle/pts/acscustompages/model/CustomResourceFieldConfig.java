package com.dataaxle.pts.acscustompages.model;

import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import lombok.Value;

import java.io.Serializable;

@Value
public class CustomResourceFieldConfig implements Serializable {

	private static final long serialVersionUID = 3146958694567925350L;

	String fieldName;

	ValueGenerator<?> valueGenerator;

	public CustomResourceFieldConfig(ValueGenerator<?> valueGenerator) {
		this.fieldName = valueGenerator.getAcsFieldName();
		this.valueGenerator = valueGenerator;
	}
}
