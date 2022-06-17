package com.dataaxle.pts.acscustompages.stubs;

import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.ADT_BOOLEAN;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.ADT_DATETIME;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.ADT_INT64;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.ADT_STRING;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceConfig;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.conditions.Condition;
import com.dataaxle.pts.acscustompages.model.conditions.FieldHasValueCondition;
import com.dataaxle.pts.acscustompages.model.loadaction.GetCustomResourceLoadActionConfig;
import com.dataaxle.pts.acscustompages.model.loadaction.LoadActionConfig;
import com.dataaxle.pts.acscustompages.model.loadaction.ParameterConfig;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.validation.ValidatePhoneNumber;
import com.dataaxle.pts.acscustompages.model.validation.ValidateString;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ConstantValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CurrentDateTimeValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.ActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.CustomResourcesActionProcessor;

import java.util.Collections;
import java.util.List;

public class StubbedAppDetailsRepositoryBoscovsFhf extends StubbedAppDetailBase {

	final static String CONFIRM_NM = "confirm";
	final static String ERROR_NM = "error";
	final static String LOOKUP_NM = "lookup";
	final static String STEP1_NM = "step1";
	final static String STEP2_NM = "step2";
	final static String STEP3_NM = "step3";

	final static PageField<?> ZIPCODE_FLD = buildField("zipcode", false, false, "",
		new ValidateString("zipcode", false, null, 5, "([0-9]{:max})?"));

	public static AppDetails fhfRegistration() {

		final AppPage step1 = AppPage.builder(STEP1_NM, PageType.PAGE).controller(Controller.GENERIC_PAGE)
			.defaultCondition().defaultActions(Collections.emptyList())
			.successNavigation(STEP2_NM).failureNavigation(STEP1_NM).build();

		final String CUS_NP_ORGANIZATIONS_NM = "cusNpOrganizations";

		final AppPage step2 = AppPage.builder(STEP2_NM, PageType.ENTRY_POINT)
			.controller(Controller.LIST_CUSTOM_RESOURCE)
			.successNavigation(STEP1_NM).failureNavigation(STEP1_NM).errorNavigation(ERROR_NM)
			.addField(ZIPCODE_FLD)
			// used in pagination of Organization list
			.addField(buildField("page", true, false, 1, null))
			.defaultCondition().defaultActions(Collections.emptyList())
			.listCustomResource(ListCustomResourceConfig.builder()
				.customResource(CUS_NP_ORGANIZATIONS_NM).filterName("byZipcode").listFieldName("organizations")
				.parameterName("zipcode").executionPoint(ListCustomResourceConfig.ExecutionPoint.ON_SUBMIT)
				.pagination().pageSize(20).sortParameter("orgName").numberAsString("zipcode").build())
			.build();

		final AppPage lookup = AppPage.builder(LOOKUP_NM, PageType.PAGE)
			.controller(Controller.GET_CUSTOM_RESOURCE_BY_LINK).successNavigation(STEP3_NM).failureNavigation(STEP3_NM)
			.customResource(CUS_NP_ORGANIZATIONS_NM).numberAsString("zipcode")
			.mapQueryParameter("id", "npid")
			.forwardQueryParameters().forwardParameter("id").forwardParameter("zip")
			.build();

		// Names of fields from cusNpOrganizations used on step3 page
		final String ORG_NAME = "orgName";
		final String ORG_ADDRESS_1 = "address1";
		final String ORG_ADDRESS_2 = "address2";
		final String ORG_CITY = "city";
		final String ORG_STATECODE = "stateCode";
		final String ORG_ZIPCODE = "zipcode";

		// Names for form fields
		final String ORG_ID = "npID";
		final String FIRST_NAME_FLD = "firstName";
		final String LAST_NAME_FLD = "lastName";
		final String EMAIL_FLD = "email";
		final String CONFIRM_EMAIL_FLD = "confirmEmail";
		final String PHONE_FLD = "phone";
		final String CONFIRM_PHONE_FLD = "confirmPhone";
		final String PASS_COUNT_FLD = "passCount";
		final String PICKUP_STORE_FLD = "pickupStore";
		final String STORE_NAME_FLD = "storeName";

		final String ZIPCODE_REGEX = "^[0-9]{:min,:max}$";
		final String NAME_REGEX = "^[\\p{Graph} ]{:min,:max}$";
		final String ADDRESS_REGEX = "^[\\p{Graph} ]{:min,:max}$";

		// Field names for cusRegistration custom resource
		final String REG_CREATED = "CREATED";
		final String REG_EMAIL = "EMAIL";
		final String REG_FIRST_NAME = "FIRST_NAME";
		final String REG_LAST_NAME = "LAST_NAME";
		final String REG_NP_ADDR1 = "NP_ADDR1";
		final String REG_NP_ADDR2 = "NP_ADDR2";
		final String REG_NP_CITY_NM = "NP_CITY_NM";
		final String REG_NP_ID = "NP_ID";
		final String REG_NP_ORG_NM = "NP_ORG_NM";
		final String REG_NP_STATE_CD = "NP_STATE_CD";
		final String REG_NP_ZIP_CD = "NP_ZIP_CD";
		final String REG_PASS_COUNT = "PASS_COUNT";
		final String REG_PHONE = "PHONE";
		final String REG_PICKUP_STORE = "PICKUP_STOREID";
		final String REG_SENT = "SENT";

		final String HAS_ORG_COND_NM = "hasOrg";

		final Condition hasOrg = new FieldHasValueCondition(HAS_ORG_COND_NM, 0, Condition.ActsUpon.FORM_DATA,
			new FormFieldValueGenerator.Builder().fieldName(ORG_ID).acsFieldName(ORG_ID).adobeDataType(ADT_STRING).build());

		final ValueGenerator<?> VG_CREATED = new CurrentDateTimeValueGenerator.Builder().acsFieldName(REG_CREATED)
			.adobeDataType(ADT_DATETIME).zoneId("UTC").build();

		final ValueGenerator<?> VG_EMAIL = new FormFieldValueGenerator.Builder().acsFieldName(REG_EMAIL)
			.adobeDataType(ADT_STRING).fieldName(EMAIL_FLD).build();

		final ValueGenerator<?> VG_FIRST_NAME = new FormFieldValueGenerator.Builder().acsFieldName(REG_FIRST_NAME)
			.adobeDataType(ADT_STRING).fieldName(FIRST_NAME_FLD).build();

		final ValueGenerator<?> VG_LAST_NAME = new FormFieldValueGenerator.Builder().acsFieldName(REG_LAST_NAME)
			.adobeDataType(ADT_STRING).fieldName(LAST_NAME_FLD).build();

		final ValueGenerator<?> VG_NP_ADDR1 = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_ADDR1)
			.adobeDataType(ADT_STRING).fieldName(ORG_ADDRESS_1).build();

		final ValueGenerator<?> VG_NP_ADDR2 = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_ADDR2)
			.adobeDataType(ADT_STRING).fieldName(ORG_ADDRESS_2).build();

		final ValueGenerator<?> VG_NP_CITY_NM = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_CITY_NM)
			.adobeDataType(ADT_STRING).fieldName(ORG_CITY).build();

		final ValueGenerator<?> VG_NP_ID = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_ID)
			.adobeDataType(ADT_INT64).fieldName(ORG_ID).build();

		final ValueGenerator<?> VG_NP_ORG_NM = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_ORG_NM)
			.adobeDataType(ADT_STRING).fieldName(ORG_NAME).build();

		final ValueGenerator<?> VG_NP_STATE_CD = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_STATE_CD)
			.adobeDataType(ADT_STRING).fieldName(ORG_STATECODE).build();

		final ValueGenerator<?> VG_NP_ZIP_CD = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_ZIP_CD)
			.adobeDataType(ADT_STRING).fieldName(ORG_ZIPCODE).build();

		final ValueGenerator<?> VG_PASS_COUNT = new FormFieldValueGenerator.Builder().acsFieldName(REG_PASS_COUNT)
			.adobeDataType(ADT_INT64).fieldName(PASS_COUNT_FLD).build();

		final ValueGenerator<?> VG_PHONE = new FormFieldValueGenerator.Builder().acsFieldName(REG_PHONE)
			.adobeDataType(ADT_STRING).fieldName(PHONE_FLD).build();

		final ValueGenerator<?> VG_PICKUP_STOREID = new FormFieldValueGenerator.Builder().acsFieldName(REG_PICKUP_STORE)
			.adobeDataType(ADT_INT64).fieldName(PICKUP_STORE_FLD).build();

		final ValueGenerator<?> VG_SENT = new ConstantValueGenerator.Builder<>().acsFieldName(REG_SENT)
			.adobeDataType(ADT_BOOLEAN).value(false).build();

		final String CUS_CLOSEST_STORE_NM = "cusClosestStore";
		final String CUS_REGISTRATION_NM = "cusRegistration";

		// All fields except NP_ID, which will not be available
		final List<ActionProcessor> noOrgActions = List.of(
			new CustomResourcesActionProcessor(List.of(buildCustomResourceConfig(CUS_REGISTRATION_NM, List.of(
				buildCustomResourceField(VG_CREATED), buildCustomResourceField(VG_EMAIL),
				buildCustomResourceField(VG_FIRST_NAME), buildCustomResourceField(VG_LAST_NAME),
				buildCustomResourceField(VG_NP_ADDR1), buildCustomResourceField(VG_NP_ADDR2),
				buildCustomResourceField(VG_NP_CITY_NM), buildCustomResourceField(VG_NP_ORG_NM),
				buildCustomResourceField(VG_NP_STATE_CD), buildCustomResourceField(VG_NP_ZIP_CD),
				buildCustomResourceField(VG_PASS_COUNT), buildCustomResourceField(VG_PHONE),
				buildCustomResourceField(VG_PICKUP_STOREID), buildCustomResourceField(VG_SENT)
			))))
		);

		// All fields on cusRegistration resource
		final List<ActionProcessor> hasOrgActions = List.of(
			new CustomResourcesActionProcessor(List.of(buildCustomResourceConfig(CUS_REGISTRATION_NM, List.of(
				buildCustomResourceField(VG_CREATED), buildCustomResourceField(VG_EMAIL),
				buildCustomResourceField(VG_FIRST_NAME), buildCustomResourceField(VG_LAST_NAME),
				buildCustomResourceField(VG_NP_ADDR1), buildCustomResourceField(VG_NP_ADDR2),
				buildCustomResourceField(VG_NP_CITY_NM), buildCustomResourceField(VG_NP_ID),
				buildCustomResourceField(VG_NP_ORG_NM), buildCustomResourceField(VG_NP_STATE_CD),
				buildCustomResourceField(VG_NP_ZIP_CD), buildCustomResourceField(VG_PASS_COUNT),
				buildCustomResourceField(VG_PHONE), buildCustomResourceField(VG_PICKUP_STOREID),
				buildCustomResourceField(VG_SENT)
			))))
		);

		final AppPage step3 = AppPage.builder(STEP3_NM, PageType.PAGE)
			.controller(Controller.CREATE_CUSTOM_RESOURCE)
			.successNavigation(CONFIRM_NM)
			.errorNavigation(ERROR_NM)
			.customResource(CUS_REGISTRATION_NM).numberAsString("NP_ZIP_CD")
			.loadAction(GetCustomResourceLoadActionConfig.builder().customResource(CUS_CLOSEST_STORE_NM)
				.filterName("byZipcode").fieldMapping("pickupStore", "storeNumber")
							.numberAsString("zipcode")
				.parameter("zipcode", ParameterConfig.builder()
					.source(LoadActionConfig.ParameterSource.QUERY_PARAMETER).name("zip").build())
				.build())
			.missingInputView(STEP1_NM)
			//.forwardQueryParameters().forwardParameter("id").forwardParameter("zip")
			.addField(buildField(ORG_ID, true, false, "", null))
			.addField(buildField(ORG_NAME, false, false, "",
				new ValidateString(ORG_NAME, true, 2, 255, NAME_REGEX)))
			.addField(buildField(ORG_ADDRESS_1, false, false, "",
				new ValidateString(ORG_ADDRESS_1, true, 5, 255, ADDRESS_REGEX)))
			.addField(buildField(ORG_ADDRESS_2, false, false, "",
				new ValidateString(ORG_ADDRESS_2, false, 0, 255, ADDRESS_REGEX)))
			.addField(buildField(ORG_CITY, false, false, "",
				new ValidateString(ORG_CITY, true, 2, 50, NAME_REGEX)))
			.addField(buildField(ORG_STATECODE, false, false, "",
				new ValidateString(ORG_STATECODE, true, 2, 2, "^[A-Z]{:min,:max}$")))
			.addField(buildField(ORG_ZIPCODE, false, false, "",
				new ValidateString(ORG_ZIPCODE, true, 5, 5, ZIPCODE_REGEX)))
			.addField(buildField(FIRST_NAME_FLD, false, false, "",
				new ValidateString(FIRST_NAME_FLD, true, 2, 50, NAME_REGEX)))
			.addField(buildField(LAST_NAME_FLD, false, false, "",
				new ValidateString(LAST_NAME_FLD, true, 2, 50, NAME_REGEX)))
			.addField(buildField(EMAIL_FLD, false, false, "",
				new ValidateEmail(EMAIL_FLD, false, true, true, 0, 255,
					CONFIRM_EMAIL_FLD)))
			.addField(buildField(CONFIRM_EMAIL_FLD, false, false, "", null))
			.addField(buildField(PHONE_FLD, false, false, "",
				new ValidatePhoneNumber(PHONE_FLD, true, true, CONFIRM_PHONE_FLD, List.of("US"))))
			.addField(buildField(CONFIRM_PHONE_FLD, false, false, "", null))
			.addField(buildField(PASS_COUNT_FLD, false, false, "",
				new ValidateString(PASS_COUNT_FLD, true, 3, 3, "^[1-7]{1}[0]{2}")))
			.addField(buildField(PICKUP_STORE_FLD, false, false, "",
				new ValidateString(PICKUP_STORE_FLD, true, 1, 5, "^[0-9]{:min,:max}$")))
			.addField(buildField(STORE_NAME_FLD, false, false, "", null))
			.defaultCondition().defaultActions(noOrgActions)
			.condition(hasOrg).conditionalAction(HAS_ORG_COND_NM, hasOrgActions)
			.build();

		final AppPage confirm = AppPage.builder(CONFIRM_NM, PageType.PAGE)
			.controller(Controller.CUSTOM_RESOURCE_CONFIRMATION_PAGE)
			.missingInputView(STEP1_NM)
			.addField(buildField(REG_NP_ORG_NM, false, false, "", null))
			.addField(buildField(REG_NP_ADDR1, false, false, "", null))
			.addField(buildField(REG_NP_CITY_NM, false, false, "", null))
			.addField(buildField(REG_NP_STATE_CD, false, false, "", null))
			.addField(buildField(REG_NP_ZIP_CD, false, false, "", null))
			.addField(buildField(REG_FIRST_NAME, false, false, "", null))
			.addField(buildField(REG_LAST_NAME, false, false, "", null))
			.addField(buildField(REG_EMAIL, false, false, "", null))
			.addField(buildField(REG_PHONE, false, false, "", null))
			.addField(buildField(REG_PASS_COUNT, false, false, "", null))
			.addField(buildField(STORE_NAME_FLD, false, false, "", null))
			.addField(buildField(REG_PICKUP_STORE, false, false, "", null))
			.build();

		final AppPage error = AppPage.builder(ERROR_NM, PageType.ERROR)
			.controller(Controller.ERROR_PAGE)
			.build();

		return AppDetails.builder("boscovs", "fhf-registration")
			.addPage(step1)
			.addPage(step2)
			.addPage(lookup)
			.addPage(step3)
			.addPage(confirm)
			.addPage(error)
			.enable()
			.build();
	}

	public static AppDetails fhfRegistered() {

		final AppPage step1 = AppPage.builder(STEP1_NM, PageType.PAGE).controller(Controller.GENERIC_PAGE)
			.defaultCondition().defaultActions(Collections.emptyList())
			.successNavigation(STEP2_NM).failureNavigation(STEP1_NM).build();

		final String CUS_ALREADY_REGISTERED_NM = "cusAlreadyRegistered";

		final AppPage step2 = AppPage.builder(STEP2_NM, PageType.ENTRY_POINT)
			.controller(Controller.LIST_CUSTOM_RESOURCE)
			.successNavigation(STEP1_NM).failureNavigation(STEP1_NM).errorNavigation(ERROR_NM)
			.addField(ZIPCODE_FLD)
			// used in pagination of Organization list
			.addField(buildField("page", true, false, 1, null))
			.defaultCondition().defaultActions(Collections.emptyList())
			.listCustomResource(ListCustomResourceConfig.builder()
				.customResource(CUS_ALREADY_REGISTERED_NM).filterName("byZipcode").listFieldName("organizations")
				.parameterName("zipcode").executionPoint(ListCustomResourceConfig.ExecutionPoint.ON_SUBMIT)
				.pagination().pageSize(20).sortParameter("NP_ORG_NM").numberAsString("NP_ZIP_CD").build())
			.build();

		final AppPage lookup = AppPage.builder(LOOKUP_NM, PageType.PAGE)
			.controller(Controller.GET_CUSTOM_RESOURCE_BY_LINK).successNavigation(STEP3_NM).failureNavigation(STEP3_NM)
			.customResource(CUS_ALREADY_REGISTERED_NM).numberAsString("NP_ZIP_CD")
			.mapQueryParameter("id", "npid")
			.forwardQueryParameters().forwardParameter("id").forwardParameter("zip")
			.build();

		// Names of fields from cusAlreadyRegistered used on step3 page
		final String ORG_NAME = "NP_ORG_NM";
		final String ORG_ADDRESS_1 = "NP_ADDR1";
		final String ORG_ADDRESS_2 = "NP_ADDR2";
		final String ORG_CITY = "NP_CITY_NM";
		final String ORG_STATECODE = "NP_STATE_CD";
		final String ORG_ZIPCODE = "NP_ZIP_CD";

		// Names for form fields
		final String ORG_ID = "NP_ID";
		final String FIRST_NAME_FLD = "FIRST_NAME";
		final String LAST_NAME_FLD = "LAST_NAME";
		final String EMAIL_FLD = "EMAIL";
		final String PHONE_FLD = "PHONE";
		final String PASS_COUNT_FLD = "passCount";
		final String PICKUP_STORE_FLD = "PICKUP_STOREID";
		final String STORE_NAME_FLD = "storeName";

		// Field names for cusRegistration custom resource
		final String REG_CREATED = "CREATED";
		final String REG_EMAIL = "EMAIL";
		final String REG_FIRST_NAME = "FIRST_NAME";
		final String REG_LAST_NAME = "LAST_NAME";
		final String REG_NP_ADDR1 = "NP_ADDR1";
		final String REG_NP_ADDR2 = "NP_ADDR2";
		final String REG_NP_CITY_NM = "NP_CITY_NM";
		final String REG_NP_ID = "NP_ID";
		final String REG_NP_ORG_NM = "NP_ORG_NM";
		final String REG_NP_STATE_CD = "NP_STATE_CD";
		final String REG_NP_ZIP_CD = "NP_ZIP_CD";
		final String REG_PASS_COUNT = "PASS_COUNT";
		final String REG_PHONE = "PHONE";
		final String REG_PICKUP_STORE = "PICKUP_STOREID";
		final String REG_SENT = "SENT";

		final ValueGenerator<?> VG_CREATED = new CurrentDateTimeValueGenerator.Builder().acsFieldName(REG_CREATED)
			.adobeDataType(ADT_DATETIME).zoneId("UTC").build();

		final ValueGenerator<?> VG_EMAIL = new FormFieldValueGenerator.Builder().acsFieldName(REG_EMAIL)
			.adobeDataType(ADT_STRING).fieldName(EMAIL_FLD).build();

		final ValueGenerator<?> VG_FIRST_NAME = new FormFieldValueGenerator.Builder().acsFieldName(REG_FIRST_NAME)
			.adobeDataType(ADT_STRING).fieldName(FIRST_NAME_FLD).build();

		final ValueGenerator<?> VG_LAST_NAME = new FormFieldValueGenerator.Builder().acsFieldName(REG_LAST_NAME)
			.adobeDataType(ADT_STRING).fieldName(LAST_NAME_FLD).build();

		final ValueGenerator<?> VG_NP_ADDR1 = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_ADDR1)
			.adobeDataType(ADT_STRING).fieldName(ORG_ADDRESS_1).build();

		final ValueGenerator<?> VG_NP_ADDR2 = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_ADDR2)
			.adobeDataType(ADT_STRING).fieldName(ORG_ADDRESS_2).build();

		final ValueGenerator<?> VG_NP_CITY_NM = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_CITY_NM)
			.adobeDataType(ADT_STRING).fieldName(ORG_CITY).build();

		final ValueGenerator<?> VG_NP_ID = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_ID)
			.adobeDataType(ADT_INT64).fieldName(ORG_ID).build();

		final ValueGenerator<?> VG_NP_ORG_NM = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_ORG_NM)
			.adobeDataType(ADT_STRING).fieldName(ORG_NAME).build();

		final ValueGenerator<?> VG_NP_STATE_CD = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_STATE_CD)
			.adobeDataType(ADT_STRING).fieldName(ORG_STATECODE).build();

		final ValueGenerator<?> VG_NP_ZIP_CD = new FormFieldValueGenerator.Builder().acsFieldName(REG_NP_ZIP_CD)
			.adobeDataType(ADT_STRING).fieldName(ORG_ZIPCODE).build();

		final ValueGenerator<?> VG_PASS_COUNT = new FormFieldValueGenerator.Builder().acsFieldName(REG_PASS_COUNT)
			.adobeDataType(ADT_INT64).fieldName(PASS_COUNT_FLD).build();

		final ValueGenerator<?> VG_PHONE = new FormFieldValueGenerator.Builder().acsFieldName(REG_PHONE)
			.adobeDataType(ADT_STRING).fieldName(PHONE_FLD).build();

		final ValueGenerator<?> VG_PICKUP_STOREID = new FormFieldValueGenerator.Builder().acsFieldName(REG_PICKUP_STORE)
			.adobeDataType(ADT_INT64).fieldName(PICKUP_STORE_FLD).build();

		final ValueGenerator<?> VG_SENT = new ConstantValueGenerator.Builder<>().acsFieldName(REG_SENT)
			.adobeDataType(ADT_BOOLEAN).value(false).build();

		final String CUS_RENEWALS_NM = "cusRenewals";

		// All fields on cusRenewals resource
		final List<ActionProcessor> defaultActions = List.of(
			new CustomResourcesActionProcessor(List.of(buildCustomResourceConfig(CUS_RENEWALS_NM, List.of(
				buildCustomResourceField(VG_CREATED), buildCustomResourceField(VG_EMAIL),
				buildCustomResourceField(VG_FIRST_NAME), buildCustomResourceField(VG_LAST_NAME),
				buildCustomResourceField(VG_NP_ADDR1), buildCustomResourceField(VG_NP_ADDR2),
				buildCustomResourceField(VG_NP_CITY_NM), buildCustomResourceField(VG_NP_ID),
				buildCustomResourceField(VG_NP_ORG_NM), buildCustomResourceField(VG_NP_STATE_CD),
				buildCustomResourceField(VG_NP_ZIP_CD), buildCustomResourceField(VG_PASS_COUNT),
				buildCustomResourceField(VG_PHONE), buildCustomResourceField(VG_PICKUP_STOREID),
				buildCustomResourceField(VG_SENT)
			))))
		);

		final AppPage step3 = AppPage.builder(STEP3_NM, PageType.PAGE)
			.controller(Controller.CREATE_CUSTOM_RESOURCE)
			.successNavigation(CONFIRM_NM)
			.errorNavigation(ERROR_NM)
			.customResource(CUS_RENEWALS_NM).numberAsString("NP_ZIP_CD")
			.missingInputView(STEP1_NM)
			.addField(buildField(ORG_ID, true, false, "", null))
			.addField(buildField(ORG_NAME, false, false, "", null))
			.addField(buildField(ORG_ADDRESS_1, false, false, "", null))
			.addField(buildField(ORG_ADDRESS_2, false, false, "", null))
			.addField(buildField(ORG_CITY, false, false, "", null))
			.addField(buildField(ORG_STATECODE, false, false, "", null))
			.addField(buildField(ORG_ZIPCODE, false, false, "", null))
			.addField(buildField(FIRST_NAME_FLD, false, false, "", null))
			.addField(buildField(LAST_NAME_FLD, false, false, "", null))
			.addField(buildField(EMAIL_FLD, false, false, "", null))
			.addField(buildField(PHONE_FLD, false, false, "", null))
			.addField(buildField(PASS_COUNT_FLD, false, false, "",
				new ValidateString(PASS_COUNT_FLD, true, 3, 3, "^[1-7]{1}[0]{2}")))
			.addField(buildField(PICKUP_STORE_FLD, false, false, "",
				new ValidateString(PICKUP_STORE_FLD, true, 1, 5, "^[0-9]{:min,:max}$")))
			.addField(buildField(STORE_NAME_FLD, false, false, "", null))
			.defaultCondition().defaultActions(defaultActions)
			.build();

		final AppPage confirm = AppPage.builder(CONFIRM_NM, PageType.PAGE)
			.controller(Controller.CUSTOM_RESOURCE_CONFIRMATION_PAGE)
			.missingInputView(STEP1_NM)
			.addField(buildField(REG_NP_ORG_NM, false, false, "", null))
			.addField(buildField(REG_NP_ADDR1, false, false, "", null))
			.addField(buildField(REG_NP_CITY_NM, false, false, "", null))
			.addField(buildField(REG_NP_STATE_CD, false, false, "", null))
			.addField(buildField(REG_NP_ZIP_CD, false, false, "", null))
			.addField(buildField(REG_FIRST_NAME, false, false, "", null))
			.addField(buildField(REG_LAST_NAME, false, false, "", null))
			.addField(buildField(REG_EMAIL, false, false, "", null))
			.addField(buildField(REG_PHONE, false, false, "", null))
			.addField(buildField(REG_PASS_COUNT, false, false, "", null))
			.addField(buildField(STORE_NAME_FLD, false, false, "", null))
			.addField(buildField(REG_PICKUP_STORE, false, false, "", null))
			.build();

		final AppPage error = AppPage.builder(ERROR_NM, PageType.ERROR)
			.controller(Controller.ERROR_PAGE)
			.build();

		return AppDetails.builder("boscovs", "fhf-registered")
			.addPage(step1)
			.addPage(step2)
			.addPage(lookup)
			.addPage(step3)
			.addPage(confirm)
			.addPage(error)
			.enable()
			.build();
	}
}
