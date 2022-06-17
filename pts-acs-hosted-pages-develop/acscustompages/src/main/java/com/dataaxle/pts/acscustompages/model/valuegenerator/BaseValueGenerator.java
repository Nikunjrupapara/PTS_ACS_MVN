package com.dataaxle.pts.acscustompages.model.valuegenerator;

import com.dataaxle.pts.acscustompages.model.AdobeDataType;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.converter.AdobeConverter;

import java.io.Serializable;

public abstract class BaseValueGenerator<R> implements ValueGenerator<R>, Serializable {

	private static final long serialVersionUID = 4680714527418730841L;

	protected final String acsFieldName;

	protected final AdobeDataType acsDataType;

	protected final ActsUpon actsUpon;

	protected boolean ignoreWhenEmpty;

	@Deprecated
	protected BaseValueGenerator(String acsFieldName, AdobeDataType acsDataType, ActsUpon actsUpon) {
		this.acsFieldName = acsFieldName;
		this.acsDataType = acsDataType;
		this.actsUpon = actsUpon;
		this.ignoreWhenEmpty = false;
	}

	protected BaseValueGenerator(Builder<?> builder) {
		this.acsDataType = builder.acsDataType;
		this.acsFieldName = builder.acsFieldName;
		this.actsUpon = builder.actsUpon;
		this.ignoreWhenEmpty = builder.ignoreWhenEmpty;
	}

	@Override
	public R getValue(CustomResourceResponse source) {
		return null;
	}

	@Override
	public String getAcsFieldName() {
		return acsFieldName;
	}

	@Override
	public AdobeDataType getAcsDataType() {
		return acsDataType;
	}

	@Override
	public ActsUpon getActsUpon() {
		return actsUpon;
	}

	@Override
	public boolean isIgnoreWhenEmpty() {
		return ignoreWhenEmpty;
	}

	@SuppressWarnings("unchecked")
	protected R mapToAcs(String value) {
		AdobeConverter<?> ac = AdobeConverter.getInstance(acsDataType);
		return (R)ac.convert(value);
	}

	@SuppressWarnings("unchecked")
	protected R mapToAcs(Object value) {
		AdobeConverter<?> ac = AdobeConverter.getInstance(acsDataType);
		return (R)ac.convert(value);
	}


}
