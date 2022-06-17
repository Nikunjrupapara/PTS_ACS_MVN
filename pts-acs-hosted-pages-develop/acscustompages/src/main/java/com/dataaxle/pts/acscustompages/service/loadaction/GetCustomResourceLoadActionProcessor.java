package com.dataaxle.pts.acscustompages.service.loadaction;

import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.FindCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.loadaction.GetCustomResourceLoadActionConfig;
import com.dataaxle.pts.acscustompages.model.loadaction.LoadActionConfig;
import com.dataaxle.pts.acscustompages.model.loadaction.ValueProvider;
import com.dataaxle.pts.acscustompages.service.CustomResourceService;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Value
@AllArgsConstructor
@Slf4j
public class GetCustomResourceLoadActionProcessor implements LoadActionProcessor<GetCustomResourceResponse> {

	CustomResourceService customResourceService;

	GetCustomResourceLoadActionConfig config;

	@Override
	public FindCustomResourceRequest prepareRequest(ValueProvider... sources) {
		Map<String, String> parameters = new HashMap<>();
		config.getParameters().forEach((name, param) -> {
			ValueProvider valueProvider = ValueProvider.getValueProvider(param.getSource(), sources);
			parameters.put(name, valueProvider.provideValue(param.getName()));
		});
		return GetCustomResourceRequest.builder()
			.customResource(config.getCustomResource()).filter(config.getFilterName()).addParameters(parameters)
			.numberAsString(config.getNumbersAsString())
			.build();
	}

	@Override
	public CustomResourceResponseWrapper<GetCustomResourceResponse> execute(AppDetails appDetails,
																			FindCustomResourceRequest request) {
		GetCustomResourceRequest getCustomResourceRequest = (GetCustomResourceRequest) request;
		try {
			return customResourceService.get(appDetails, getCustomResourceRequest);
		} catch (ResourceNotFoundException e) {
			if (config.getDataNotFoundStrategy() == LoadActionConfig.DataNotFoundStrategy.RETHROW) {
				throw e;
			}
			LOG.warn("Request returned no data: {}", getCustomResourceRequest, e);
			return new CustomResourceResponseWrapper<>(getCustomResourceRequest, true, false);
		}
	}

	@Override
	public void mapToPage(CustomResourceResponseWrapper<?> customResourceResponseWrapper, DynamicFormBean thisPageFormBean) {
		if (customResourceResponseWrapper.isSuccess()) {
			GetCustomResourceResponse response = (GetCustomResourceResponse) customResourceResponseWrapper.getCustomResourceResponse();
			config.getFieldMap().forEach((target, source) ->
											 thisPageFormBean.initialiseField(target, response.getField(source)));
		}
	}
}
