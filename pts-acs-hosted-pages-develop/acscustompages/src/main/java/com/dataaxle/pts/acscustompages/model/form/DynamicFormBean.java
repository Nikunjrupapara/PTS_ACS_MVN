package com.dataaxle.pts.acscustompages.model.form;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_ID_HASH;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.Environment;
import com.dataaxle.pts.acscustompages.model.RequestResult;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResults;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor(force = true)
public class DynamicFormBean implements Serializable {

	private static final long serialVersionUID = -3630431170852933502L;

	final Controller controller;

	final String contextPath;

	final String appPath;

	final Environment environment;

	String brand;

	final String barcodeUrl;

	boolean error = false;

	Map<String, Object> items = new HashMap<>();

	Map<String, Boolean> services = new HashMap<>();

	ValidationResults validationResults = new ValidationResults();

	// TODO: This is retained for backwards compatibility with some forms which reference this property directly.
	//   These should be updated to reference requestResult instead so this field can be deleted.
	@Deprecated
	RequestResult profileRequestResult;

	RequestResult requestResult;

	String recaptchaKey;

	QueryParameters queryParameters;

	ListDetails<?> listDetails = ListDetails.emptyList();

	public DynamicFormBean(CustomPagesRequest customPagesRequest) {
		this.controller = customPagesRequest.getController();
		this.contextPath = customPagesRequest.getFullPath();
		this.brand = customPagesRequest.getBrand();
		this.appPath = customPagesRequest.getContextPath();
		this.environment = customPagesRequest.getEnvironment();
		this.recaptchaKey = customPagesRequest.getRecaptchaKey();
		this.queryParameters = customPagesRequest.getQueryParams();
		this.barcodeUrl = customPagesRequest.getBarcodeUrl();
	}

	public String getRecaptchaKey(){
		return recaptchaKey==null?"NO KEY":recaptchaKey;
	}

	public void initialiseField(String key) {
		items.put(key, "");
	}

	public void initialiseField(String key, Object value) {
		items.put(key, value);
	}

	public String getCustomerUniqueId() {
		return (String)items.getOrDefault(CUS_CUSTOMER_ID_HASH, "");
	}

	public void setRequestResult(RequestResult requestResult) {
		this.profileRequestResult = requestResult;
		this.requestResult = requestResult;
	}

	public void setProfileRequestResult(RequestResult requestResult) {
		this.profileRequestResult = requestResult;
		this.requestResult = requestResult;
	}

	public RequestResult getProfileRequestResult() { return requestResult; }

	public boolean hasErrors() { return validationResults.hasErrors(); }

	public boolean isError(String name) {
		return validationResults.isError(name);
	}

	public String errorCode(String name) {
		if (validationResults.isError(name)) {
			return validationResults.getResult(name).getErrorCode().getCode();
		}
		return "";
	}

	public String errorMessage(String name) {
		if (validationResults.isError(name)) {
			return validationResults.getResult(name).getErrorCode().getMessageCode();
		}
		return "";
	}

	public void initialiseService(String name, boolean subscribed) {
		services.put(name, subscribed);
	}

	public boolean isSubscribedToService(String name) {
		if (services.containsKey(name)) {
			return services.get(name);
		}
		return false;
	}

	public boolean hasItem(String fieldName) {
		return items.containsKey(fieldName);
	}

	public String getItem(String fieldName) {
		if (items.containsKey(fieldName)) {
			return (String) items.get(fieldName);
		}
		return "";
	}

	public String getEnvironmentName() {
		return environment.getName();
	}

	public boolean hasQueryParam(String name) { return queryParameters.hasParameter(name); }

	public List<String> getQueryParamValue(String name) {
		if (queryParameters.hasParameter(name)) {
			return queryParameters.getParameter(name);
		}
		return Collections.emptyList();
	}

	public String getQueryParamFirstValue(String name) {
		List<String> values = getQueryParamValue(name);
		if (values.isEmpty()) {
			return "";
		}
		return values.get(0);
	}

	public String buildQueryString() {
		String queryStr = queryParameters.buildQueryString();
		return StringUtils.hasLength(queryStr) ? "?" + queryStr : "";
	}

	public String buildSubmitUrl(boolean includeQueryString) {
		if (controller == null || controller.equals(Controller.NONE)) {
			return "";
		}
		return controller.getPath() + "/" + (contextPath) +
				   (includeQueryString ? buildQueryString() : "");
	}

	public String buildSubmitUrl() {
		return buildSubmitUrl(true);
	}

	public boolean hasRequestResult() { return requestResult != null; }
}
