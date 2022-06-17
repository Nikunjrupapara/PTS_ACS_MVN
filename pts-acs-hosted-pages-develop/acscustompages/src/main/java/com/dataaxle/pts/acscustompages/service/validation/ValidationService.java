package com.dataaxle.pts.acscustompages.service.validation;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResults;

import java.util.List;
import java.util.Map;

public interface ValidationService {

	String GLOBAL_ERROR = "global";

	ValidationResults validate(List<PageField<?>> fields, Map<String, Object> fieldValues);

	ValidationResults validate(CustomPagesRequest customPagesRequest, DynamicFormBean dynamicFormBean);
}
