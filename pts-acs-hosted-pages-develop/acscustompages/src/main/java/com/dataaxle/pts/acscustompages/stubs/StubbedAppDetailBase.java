package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.model.CustomResourceConfig;
import com.dataaxle.pts.acscustompages.model.CustomResourceFieldConfig;
import com.dataaxle.pts.acscustompages.model.HiddenService;
import com.dataaxle.pts.acscustompages.model.MultiServiceField;
import com.dataaxle.pts.acscustompages.model.MultiServiceProfileField;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.ServiceField;
import com.dataaxle.pts.acscustompages.model.ServiceProfileField;
import com.dataaxle.pts.acscustompages.model.SingleServiceField;
import com.dataaxle.pts.acscustompages.model.SingleServiceProfileField;
import com.dataaxle.pts.acscustompages.model.form.ValueType;
import com.dataaxle.pts.acscustompages.model.validation.ValidationRule;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;

import java.util.List;

public abstract class StubbedAppDetailBase {

	protected static PageField<String> buildField(String name, boolean hidden, boolean submit, String defaultValue, ValidationRule validationRule) {
		return new PageField<>(name, hidden, submit, defaultValue, validationRule, ValueType.STRING);
	}

	protected static PageField<Boolean> buildField(String name, boolean hidden, boolean submit, boolean defaultValue, ValidationRule validationRule) {
		return new PageField<>(name, hidden, submit, defaultValue, validationRule, ValueType.BOOLEAN);
	}

	protected static PageField<Integer> buildField(String name, boolean hidden, boolean submit, Integer defaultValue, ValidationRule validationRule) {
		return new PageField<>(name, hidden, submit, defaultValue, validationRule, ValueType.NUMBER);
	}

	protected static HiddenService buildService(String name, ServiceAction serviceAction) {
		return new HiddenService(name, serviceAction);
	}

	protected static ServiceField<String> buildServiceField(String fieldName, String serviceName, String formOnValue, String formOffValue) {
		return new SingleServiceField<>(fieldName, formOnValue, formOffValue, serviceName);
	}

	protected static ServiceField<String> buildServiceField(String fieldName, String serviceName, String formOnValue, String formOffValue,
															boolean ignoreWhenEmpty) {
		return new SingleServiceField<>(fieldName, formOnValue, formOffValue, serviceName, ignoreWhenEmpty);
	}

	protected static ServiceField<String> buildServiceField(String fieldName, String serviceName, String formOnValue, String formOffValue,
															boolean ignoreWhenEmpty, boolean reverseAction) {
		return new SingleServiceField<>(fieldName, formOnValue, formOffValue, serviceName, ignoreWhenEmpty, reverseAction);
	}

	protected static ServiceField<String> buildServiceField(String fieldName, String formOnValue, String formOffValue,
														  List<String> add, List<String> remove) {
		return new MultiServiceField<>(fieldName, formOnValue, formOffValue, add, remove);
	}

	protected static ServiceProfileField<String> buildServiceField(String fieldName, String formOnValue, String formOffValue,
																 String profileName, String acsOnValue, String acsOffValue,
																 String serviceName) {
		return new SingleServiceProfileField<>(fieldName, formOnValue, formOffValue, profileName, acsOnValue, acsOffValue, serviceName);
	}

	protected static ServiceProfileField<Boolean> buildServiceField(String fieldName, String formOnValue, String formOffValue,
																  String profileName, Boolean acsOnValue, Boolean acsOffValue,
																  String serviceName) {
		return new SingleServiceProfileField<>(fieldName, formOnValue, formOffValue, profileName, acsOnValue, acsOffValue, serviceName);
	}

	protected static ServiceProfileField<Short> buildServiceField(String fieldName, String formOnValue, String formOffValue,
																String profileName, Short acsOnValue, Short acsOffValue,
																List<String> add, List<String> remove) {
		return new MultiServiceProfileField<>(fieldName, formOnValue, formOffValue, profileName, acsOnValue, acsOffValue, add, remove);
	}

	protected static CustomResourceConfig buildCustomResourceConfig(String name, List<CustomResourceFieldConfig> fields) {
		return new CustomResourceConfig(name, fields);
	}

	protected static CustomResourceFieldConfig buildCustomResourceField(ValueGenerator<?> valueGenerator) {
		return new CustomResourceFieldConfig(valueGenerator);
	}
}
