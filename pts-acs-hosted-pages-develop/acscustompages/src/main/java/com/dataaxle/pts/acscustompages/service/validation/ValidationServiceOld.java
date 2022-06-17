package com.dataaxle.pts.acscustompages.service.validation;

import com.dataaxle.pts.acscustompages.model.form.AbstractFormBean;
import org.springframework.validation.Errors;

import java.util.Map;

public interface ValidationServiceOld<T extends AbstractFormBean<?>> {
	void validate(T formBean, Errors errors);

	void validate(Map<String, Object> fields, Errors errors);
}
