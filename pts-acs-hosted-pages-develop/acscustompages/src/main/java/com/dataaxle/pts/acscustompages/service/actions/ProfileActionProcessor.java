package com.dataaxle.pts.acscustompages.service.actions;

import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
public class ProfileActionProcessor implements ActionProcessor, Serializable {

	private static final long serialVersionUID = 3045514174560079905L;

	Map<String, ValueGenerator<?>> valueGenerators;

	public ProfileActionProcessor(List<ValueGenerator<?>> valueGenerators) {
		this.valueGenerators = valueGenerators.stream()
								   .collect(Collectors.toMap(ValueGenerator::getAcsFieldName, valueGenerator -> valueGenerator));
	}

	@Override
	public void process(AppPage page, DynamicFormBean formInput, ProfileRequest profileRequest) {
		valueGenerators.forEach((fieldName, valueGenerator) -> {
			Object value = null;
			switch (valueGenerator.getActsUpon()) {
				case CONSTANT:
					value = valueGenerator.getValue();
					break;
				case FORM_DATA:
					value = valueGenerator.getValue(formInput);
					break;
				case PROFILE:
					break;
			}
			addToRequest(valueGenerator, profileRequest, fieldName, value);
		});
	}

	@Override
	public void process(AppPage page, DynamicFormBean formInput, ProfileResponse profileResponse, ProfileRequest profileRequest) {
		valueGenerators.forEach((fieldName, valueGenerator) -> {
			Object value = null;
			switch (valueGenerator.getActsUpon()) {
				case CONSTANT:
					value = valueGenerator.getValue();
					break;
				case FORM_DATA:
					value = valueGenerator.getValue(formInput);
					break;
				case PROFILE:
					value = valueGenerator.getValue(profileResponse);
					break;
			}
			addToRequest(valueGenerator, profileRequest, fieldName, value);
		});
	}

	private void addToRequest(ValueGenerator<?> valueGenerator, ProfileRequest profileRequest, String fieldName, Object value) {
		if (valueGenerator.isIgnoreWhenEmpty()) {
			if (value != null) {
				if (StringUtils.hasText(value.toString())) {
					profileRequest.addProfileField(fieldName, value);
				}
			}
		} else {
			profileRequest.addProfileField(fieldName, value);
		}
	}

	@Override
	public void process(AppPage appPage, DynamicFormBean formInput, CustomResourceResponse customResourceResponse, ProfileRequest profileRequest) {
		// do nothing
		// TODO: remove this method when restructured code is merged into develop
	}

	@Override
	public void process(AppPage appPage, CustomResourceRequest customResourceRequest, CustomResourceResponse customResourceResponse, DynamicFormBean formInputBean) {
		// do nothing
	}
}
