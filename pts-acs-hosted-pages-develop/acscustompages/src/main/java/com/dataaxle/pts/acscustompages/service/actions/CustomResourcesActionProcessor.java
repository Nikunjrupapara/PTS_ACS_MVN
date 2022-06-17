package com.dataaxle.pts.acscustompages.service.actions;

import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceConfig;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class CustomResourcesActionProcessor implements ActionProcessor, Serializable {

	private static final long serialVersionUID = 8015325949903272973L;

	List<CustomResourceConfig> customResourceConfigs;

	@Override
	public void process(AppPage page, DynamicFormBean formInput, ProfileRequest profileRequest) {
		customResourceConfigs.forEach((config -> {
			CustomResourceRequest customResourceRequest = new CustomResourceRequest(config.getName());
			CustomResourceRecord record = new CustomResourceRecord();
			config.getFields().forEach(field -> {
				ValueGenerator<?> valueGenerator = field.getValueGenerator();
				switch (valueGenerator.getActsUpon()) {
					case CONSTANT:
						record.addValue(field.getFieldName(), valueGenerator.getValue());
						break;
					case FORM_DATA:
						record.addValue(field.getFieldName(), valueGenerator.getValue(formInput));
						break;
					case PROFILE:
						break;
				}
			});
			customResourceRequest.addRecord(record);
			profileRequest.addCustomResourceRequest(customResourceRequest);
		}));

	}

	@Override
	public void process(AppPage page, DynamicFormBean formInput, ProfileResponse profileResponse, ProfileRequest profileRequest) {
		customResourceConfigs.forEach((config -> {
			CustomResourceRequest customResourceRequest = new CustomResourceRequest(config.getName());
			CustomResourceRecord record = new CustomResourceRecord();
			config.getFields().forEach(field -> {
				ValueGenerator<?> valueGenerator = field.getValueGenerator();
				switch (valueGenerator.getActsUpon()) {
					case CONSTANT:
						record.addValue(field.getFieldName(), valueGenerator.getValue());
						break;
					case FORM_DATA:
						record.addValue(field.getFieldName(), valueGenerator.getValue(formInput));
						break;
					case PROFILE:
						record.addValue(field.getFieldName(), valueGenerator.getValue(profileResponse));
						break;
				}
			});
			customResourceRequest.addRecord(record);
			profileRequest.addCustomResourceRequest(customResourceRequest);
		}));
	}

	@Override
	public void process(AppPage appPage, DynamicFormBean formInput, CustomResourceResponse customResourceResponse, ProfileRequest profileRequest) {
		customResourceConfigs.forEach((config -> {
			CustomResourceRequest customResourceRequest = new CustomResourceRequest(config.getName());
			CustomResourceRecord record = new CustomResourceRecord();
			config.getFields().forEach(field -> {
				ValueGenerator<?> valueGenerator = field.getValueGenerator();
				switch (valueGenerator.getActsUpon()) {
					case CONSTANT:
						record.addValue(field.getFieldName(), valueGenerator.getValue());
						break;
					case CUSTOM_RESOURCE:
						record.addValue(field.getFieldName(), valueGenerator.getValue(customResourceResponse));
						break;
					case FORM_DATA:
						record.addValue(field.getFieldName(), valueGenerator.getValue(formInput));
						break;
				}
			});
			customResourceRequest.addRecord(record);
			profileRequest.addCustomResourceRequest(customResourceRequest);
		}));
	}

	@Override
	public void process(AppPage appPage, CustomResourceRequest customResourceRequest, CustomResourceResponse customResourceResponse, DynamicFormBean formInputBean) {
		customResourceConfigs.forEach((config -> {
			CustomResourceRecord record = new CustomResourceRecord();
			config.getFields().forEach(field -> {
				ValueGenerator<?> valueGenerator = field.getValueGenerator();
				switch (valueGenerator.getActsUpon()) {
					case CONSTANT:
						record.addValue(field.getFieldName(), valueGenerator.getValue());
						break;
					case CUSTOM_RESOURCE:
						record.addValue(field.getFieldName(), valueGenerator.getValue(customResourceResponse));
						break;
					case FORM_DATA:
						record.addValue(field.getFieldName(), valueGenerator.getValue(formInputBean));
						break;
				}
			});
			customResourceRequest.addRecord(record);
		}));
	}
}
