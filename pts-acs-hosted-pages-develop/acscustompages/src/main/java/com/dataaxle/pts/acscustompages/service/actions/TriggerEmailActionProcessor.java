package com.dataaxle.pts.acscustompages.service.actions;

import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.TriggeredEmailConfig;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class TriggerEmailActionProcessor implements ActionProcessor, Serializable {

	private static final long serialVersionUID = -6589906080741056383L;

	TriggeredEmailConfig triggeredEmailConfig;

	@Override
	public void process(AppPage page, DynamicFormBean formInput, ProfileRequest profileRequest) {
		profileRequest.setTriggeredEventId(triggeredEmailConfig.getEventId(formInput.getBrand()));
		processEmail(formInput, null, null, profileRequest);
		processScheduled(formInput, null, null, profileRequest);
		processExpiration(formInput, null, null, profileRequest);
		triggeredEmailConfig.getContextFields().forEach((field, valueGenerator) -> {
			switch (valueGenerator.getActsUpon()) {
				case CONSTANT:
					profileRequest.addPersonalizationField(field, valueGenerator.getValue());
					break;
				case FORM_DATA:
					profileRequest.addPersonalizationField(field, valueGenerator.getValue(formInput));
					break;
				case PROFILE:
					break;
			}
		});
	}

	@Override
	public void process(AppPage page, DynamicFormBean formInput, ProfileResponse profileResponse, ProfileRequest profileRequest) {
		profileRequest.setTriggeredEventId(triggeredEmailConfig.getEventId(formInput.getBrand()));
		processEmail(formInput, profileResponse, null, profileRequest);
		processScheduled(formInput, profileResponse, null, profileRequest);
		processExpiration(formInput, profileResponse, null, profileRequest);
		triggeredEmailConfig.getContextFields().forEach((field, valueGenerator) -> {
			switch (valueGenerator.getActsUpon()) {
				case CONSTANT:
					profileRequest.addPersonalizationField(field, valueGenerator.getValue());
					break;
				case FORM_DATA:
					profileRequest.addPersonalizationField(field, valueGenerator.getValue(formInput));
					break;
				case PROFILE:
					profileRequest.addPersonalizationField(field, valueGenerator.getValue(profileResponse));
					break;
			}
		});
	}

	@Override
	public void process(AppPage appPage, DynamicFormBean formInput, CustomResourceResponse customResourceResponse, ProfileRequest profileRequest) {
		profileRequest.setTriggeredEventId(triggeredEmailConfig.getEventId(formInput.getBrand()));
		processEmail(formInput, null, customResourceResponse, profileRequest);
		processScheduled(formInput, null, customResourceResponse, profileRequest);
		processExpiration(formInput, null, customResourceResponse, profileRequest);
		triggeredEmailConfig.getContextFields().forEach((field, valueGenerator) -> {
			switch (valueGenerator.getActsUpon()) {
				case CONSTANT:
					profileRequest.addPersonalizationField(field, valueGenerator.getValue());
					break;
				case CUSTOM_RESOURCE:
					profileRequest.addPersonalizationField(field, valueGenerator.getValue(customResourceResponse));
				case FORM_DATA:
					profileRequest.addPersonalizationField(field, valueGenerator.getValue(formInput));
					break;
				case PROFILE:
					//profileRequest.addPersonalizationField(field, valueGenerator.getValue(profileResponse));
					break;
			}
		});
	}

	@Override
	public void process(AppPage appPage, CustomResourceRequest customResourceRequest, CustomResourceResponse customResourceResponse, DynamicFormBean formInputBean) {
		// do nothing
	}

	private void processEmail(DynamicFormBean formInput, ProfileResponse profileResponse,
							  CustomResourceResponse customResourceResponse, ProfileRequest profileRequest) {
		ValueGenerator<?> emailRecipient = triggeredEmailConfig.getEmail();
		profileRequest.setTriggeredEventRecipient((String)getValue(emailRecipient, formInput, profileResponse, customResourceResponse));
	}

	private void processScheduled(DynamicFormBean formInput, ProfileResponse profileResponse,
								  CustomResourceResponse customResourceResponse, ProfileRequest profileRequest) {
		ValueGenerator<?> scheduled = triggeredEmailConfig.getScheduled();
		if (scheduled != null) {
			profileRequest.setTriggeredEventScheduled((LocalDateTime)getValue(scheduled, formInput, profileResponse, customResourceResponse));
		}
	}

	private void processExpiration(DynamicFormBean formInput, ProfileResponse profileResponse,
								   CustomResourceResponse customResourceResponse, ProfileRequest profileRequest) {
		ValueGenerator<?> expiration = triggeredEmailConfig.getExpiration();
		if (expiration != null) {
			profileRequest.setTriggeredEventExpiration((LocalDateTime)getValue(expiration, formInput, profileResponse, customResourceResponse));
		}
	}

	private Object getValue(ValueGenerator<?> valueGenerator, DynamicFormBean formInput, ProfileResponse profileResponse,
							CustomResourceResponse customResourceResponse) {
		switch (valueGenerator.getActsUpon()) {
			case CONSTANT:
				return valueGenerator.getValue();
			case CUSTOM_RESOURCE:
				return valueGenerator.getValue(customResourceResponse);
			case FORM_DATA:
				return valueGenerator.getValue(formInput);
			case PROFILE:
				return valueGenerator.getValue(profileResponse);
		}
		return "";
	}
}
