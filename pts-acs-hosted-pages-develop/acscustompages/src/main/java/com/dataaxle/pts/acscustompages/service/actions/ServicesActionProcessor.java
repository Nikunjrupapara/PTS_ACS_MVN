package com.dataaxle.pts.acscustompages.service.actions;

import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Value
@NoArgsConstructor
public class ServicesActionProcessor implements ActionProcessor, Serializable {

	private static final long serialVersionUID = 6521798867891282771L;

	@Override
	public void process(AppPage page, DynamicFormBean dynamicFormBean, ProfileRequest profileRequest) {
		page.getServicesConfig().getHiddenServices().forEach(hiddenService ->
			profileRequest.addServiceAction(hiddenService.getName(), hiddenService.getServiceAction())
		);
		page.getServicesConfig().getServiceFields().forEach(serviceField -> {
			String fieldName = serviceField.getFieldName();
			String fieldValue = dynamicFormBean.getItem(fieldName);
			boolean hasValue = StringUtils.hasText(fieldValue);
			if (serviceField.isIgnoreWhenEmpty()) {
				if (hasValue) {
					profileRequest.addServiceAction(serviceField.getServiceActions(fieldValue));
				}
			} else {
				profileRequest.addServiceAction(serviceField.getServiceActions(fieldValue));
			}
		});
		page.getServicesConfig().getServiceProfileFields().forEach(serviceProfileField -> {
			String fieldName = serviceProfileField.getFieldName();
			String fieldValue = dynamicFormBean.getItem(fieldName);
			profileRequest.addServiceAction(serviceProfileField.getServiceActions(fieldValue));
			profileRequest.addProfileField(serviceProfileField.getProfileName(), serviceProfileField.getAcsValue(fieldValue));
		});
	}

	@Override
	public void process(AppPage page, DynamicFormBean formInput, ProfileResponse profileResponse, ProfileRequest profileRequest) {
		page.getServicesConfig().getHiddenServices().forEach(hiddenService ->
													   profileRequest.addServiceAction(hiddenService.getName(), hiddenService.getServiceAction())
		);
		page.getServicesConfig().getServiceFields().forEach(serviceField -> {
			String fieldName = serviceField.getFieldName();
			String fieldValue = formInput.getItem(fieldName);
			boolean hasValue = StringUtils.hasText(fieldValue);
			if (serviceField.isIgnoreWhenEmpty()) {
				if (hasValue) {
					profileRequest.addServiceAction(serviceField.getServiceActions(fieldValue));
				}
			} else {
				profileRequest.addServiceAction(serviceField.getServiceActions(fieldValue));
			}
		});
		page.getServicesConfig().getServiceProfileFields().forEach(serviceProfileField -> {
			String fieldName = serviceProfileField.getFieldName();
			String fieldValue = formInput.getItem(fieldName);
			profileRequest.addServiceAction(serviceProfileField.getServiceActions(fieldValue));
			profileRequest.addProfileField(fieldName, serviceProfileField.getAcsValue(fieldValue));
		});
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
