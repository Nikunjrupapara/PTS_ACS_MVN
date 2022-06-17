package com.dataaxle.pts.acscustompages.stubs;

import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.ADT_BOOLEAN;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.ADT_STRING;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.EMAIL;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.FIRST_NAME;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.LAST_NAME;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.LOC_COUNTRY_CODE;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.LOC_STATE_CODE;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.LOC_ZIP_CODE;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUSTOMER_ID;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUSTOMER_ID_HASH;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_ID_HASH;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.PKEY;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceConfig;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.TriggeredEmailConfig;
import com.dataaxle.pts.acscustompages.model.conditions.Condition;
import com.dataaxle.pts.acscustompages.model.conditions.CustomResourceFieldValueCondition;
import com.dataaxle.pts.acscustompages.model.conditions.CustomResourceFoundCondition;
import com.dataaxle.pts.acscustompages.model.conditions.FormFieldValueCondition;
import com.dataaxle.pts.acscustompages.model.conditions.NotCondition;
import com.dataaxle.pts.acscustompages.model.conditions.ProfileFoundCondition;
import com.dataaxle.pts.acscustompages.model.navigation.ConditionalNavigation;
import com.dataaxle.pts.acscustompages.model.navigation.NavigationProcessor;
import com.dataaxle.pts.acscustompages.model.validation.ValidateBoolean;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.validation.ValidateString;
import com.dataaxle.pts.acscustompages.model.validation.ValidationRule;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ConstantValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CurrentDateTimeValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CurrentDateValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CustomResourceFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.ActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.CustomResourcesActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ProfileActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.TriggerEmailActionProcessor;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StubbedAppDetailsRepositoryResideo extends StubbedAppDetailBase {

	public static AppDetails freeFilterRegistration() {

		final String INDEX_VIEW_NM = "index";
		final String PROCESS_CODE_VIEW_NM = "processCode";
		final String CONFIRMATION_VIEW_NM = "confirmation";
		final String INVALID_CODE_VIEW_NM = "invalidCode";
		final String ERROR_VIEW_NM = "error";

		final String ZONE_CST = "US/Central";

		final String PRODUCT_FLD_NM = "product";
		final String PROMO_CODE_FLD_NM = "promo_code";
		final String RESIDENT_FLD_NM = "resident";
		final String COUNTRY_FLD_NM = "country";
		final String STATE_FLD_NM = "state";
		final String ZIP_FLD_NM = "zip";

		final String SOURCE_VALUE = "freeFilterSignup";

		final ValueGenerator<?> VG_EMAIL = new FormFieldValueGenerator.Builder()
													.acsFieldName(EMAIL)
													.adobeDataType(ADT_STRING)
													.fieldName(EMAIL)
													.build();
		final ValueGenerator<?> VG_FIRST_NAME = new FormFieldValueGenerator.Builder()
										  .acsFieldName(FIRST_NAME)
										  .adobeDataType(ADT_STRING)
										  .fieldName(FIRST_NAME)
										  .build();
		final ValueGenerator<?> VG_LAST_NAME = new FormFieldValueGenerator.Builder()
										 .acsFieldName(LAST_NAME)
										 .adobeDataType(ADT_STRING)
										 .fieldName(LAST_NAME)
										 .build();
		final ValueGenerator<?> VG_COUNTRY_PROFILE = new FormFieldValueGenerator.Builder()
										.acsFieldName(LOC_COUNTRY_CODE)
										.adobeDataType(ADT_STRING)
										.fieldName(COUNTRY_FLD_NM)
										.build();
		final ValueGenerator<?> VG_STATE_PROFILE = new FormFieldValueGenerator.Builder()
											   .acsFieldName(LOC_STATE_CODE)
											   .adobeDataType(ADT_STRING)
											   .fieldName(STATE_FLD_NM)
											   .build();
		final ValueGenerator<?> VG_ZIP_PROFILE = new FormFieldValueGenerator.Builder()
									.acsFieldName(LOC_ZIP_CODE)
									.adobeDataType(ADT_STRING)
									.fieldName(ZIP_FLD_NM)
									.build();
		final ValueGenerator<?> VG_FF_CAPTURE_DT = new CurrentDateTimeValueGenerator.Builder()
													   .acsFieldName("cusResideo_freefiltercapture_date")
													   .zoneId(ZONE_CST)
													   .build();
		final ValueGenerator<?> VG_SOURCE_PROFILE = new ConstantValueGenerator.Builder<>()
														.acsFieldName("cusSource")
														.adobeDataType(ADT_STRING)
														.value(SOURCE_VALUE)
														.build();

		final ProfileActionProcessor NEW_PROFILE = new ProfileActionProcessor(
			List.of(VG_EMAIL, VG_FIRST_NAME, VG_LAST_NAME, VG_COUNTRY_PROFILE, VG_STATE_PROFILE, VG_ZIP_PROFILE,
				VG_FF_CAPTURE_DT, VG_SOURCE_PROFILE));
		final ProfileActionProcessor EXISTING_PROFILE = new ProfileActionProcessor(
			List.of(VG_FIRST_NAME, VG_LAST_NAME, VG_COUNTRY_PROFILE, VG_STATE_PROFILE, VG_ZIP_PROFILE, VG_FF_CAPTURE_DT));

		final ValueGenerator<?> VG_CUSTOMER_ID = new FormFieldValueGenerator.Builder()
													 .acsFieldName(CUSTOMER_ID)
													 .adobeDataType(ADT_STRING)
													 .fieldName(EMAIL)
													 .build();
		final ValueGenerator<?> VG_EXPORT_STATUS = new ConstantValueGenerator.Builder<>()
													   .acsFieldName("exportStatus")
													   .adobeDataType(ADT_STRING)
													   .value("N")
													   .build();
		final ValueGenerator<?> VG_OPT_IN_ASKS = new ConstantValueGenerator.Builder<>()
													 .acsFieldName("optInAsks")
													 .adobeDataType(ADT_BOOLEAN)
													 .value(true)
													 .build();
		final ValueGenerator<?> VG_OPT_IN_EMAILS = new ConstantValueGenerator.Builder<>()
													 .acsFieldName("optInEmails")
													 .adobeDataType(ADT_BOOLEAN)
													 .value(true)
													 .build();
		final ValueGenerator<?> VG_OPT_IN_OFFERS = new ConstantValueGenerator.Builder<>()
													 .acsFieldName("optInOffers")
													 .adobeDataType(ADT_BOOLEAN)
													 .value(true)
													 .build();
		final ValueGenerator<?> VG_PRODUCT_NAME = new FormFieldValueGenerator.Builder()
													  .acsFieldName("productName")
													  .adobeDataType(ADT_STRING)
													  .fieldName(PRODUCT_FLD_NM)
													  .build();
		final ValueGenerator<?> VG_PROMO_CODE = new FormFieldValueGenerator.Builder()
													.acsFieldName(PROMO_CODE_FLD_NM)
													.adobeDataType(ADT_STRING)
													.fieldName(PROMO_CODE_FLD_NM)
													.build();
		final ValueGenerator<?> VG_SOURCE_HPL = new ConstantValueGenerator.Builder<>()
												.acsFieldName("source")
												.adobeDataType(ADT_STRING)
												.value(SOURCE_VALUE)
												.build();
		final ValueGenerator<?> VG_COUNTRY_CR = new FormFieldValueGenerator.Builder()
														 .acsFieldName("country")
														 .adobeDataType(ADT_STRING)
														 .fieldName(COUNTRY_FLD_NM)
														 .build();
		final ValueGenerator<?> VG_STATE_CR = new FormFieldValueGenerator.Builder()
													   .acsFieldName("state")
													   .adobeDataType(ADT_STRING)
													   .fieldName(STATE_FLD_NM)
													   .build();
		final ValueGenerator<?> VG_ZIP_CR = new FormFieldValueGenerator.Builder()
													 .acsFieldName("zip")
													 .adobeDataType(ADT_STRING)
													 .fieldName(ZIP_FLD_NM)
													 .build();
		final String HOSTED_PAGES_LOGS_RESOURCE_NM = "cusHosted_pages_logs";
		final CustomResourceConfig HOSTED_PAGES_LOGS_CONFIG = buildCustomResourceConfig(HOSTED_PAGES_LOGS_RESOURCE_NM,
			List.of(
				buildCustomResourceField(VG_CUSTOMER_ID),
				buildCustomResourceField(VG_EMAIL),
				buildCustomResourceField(VG_EXPORT_STATUS),
				buildCustomResourceField(VG_FIRST_NAME),
				buildCustomResourceField(VG_LAST_NAME),
				buildCustomResourceField(VG_OPT_IN_ASKS),
				buildCustomResourceField(VG_OPT_IN_EMAILS),
				buildCustomResourceField(VG_OPT_IN_OFFERS),
				buildCustomResourceField(VG_PRODUCT_NAME),
				buildCustomResourceField(VG_PROMO_CODE),
				buildCustomResourceField(VG_SOURCE_HPL),
				buildCustomResourceField(VG_STATE_CR),
				buildCustomResourceField(VG_COUNTRY_CR),
				buildCustomResourceField(VG_ZIP_CR)
			));
		final CustomResourcesActionProcessor HOSTED_PAGES_LOGS = new CustomResourcesActionProcessor(List.of(HOSTED_PAGES_LOGS_CONFIG));

		final ValidationRule VALIDATE_PROMO_CODE = new ValidateString(PROMO_CODE_FLD_NM, true, 9, 9, "^[a-z0-9]{:min,:max}$");
		final ValidationRule VALIDATE_EMAIL = new ValidateEmail(EMAIL, true, true, false, 5, 255);

		final PageField<?> FLD_PRODUCT = buildField(PRODUCT_FLD_NM, false, false, "",
			new ValidateString(PRODUCT_FLD_NM, true, null, null, ""));
		final PageField<?> FLD_PROMO_CODE_INDEX = buildField(PROMO_CODE_FLD_NM, false, false, "", VALIDATE_PROMO_CODE);
		final PageField<?> FLD_EMAIL_INDEX = buildField(EMAIL, false, true, "", VALIDATE_EMAIL);
		final PageField<?> FLD_FIRST_NAME = buildField(FIRST_NAME, false, false, "",
			new ValidateString(LAST_NAME, true, 1, 50, "[\\p{L} -'\\.]{:min,:max}"));
		final PageField<?> FLD_LAST_NAME = buildField(LAST_NAME, false, false, "",
			new ValidateString(LAST_NAME, true, 1, 50, "[\\p{L} -'\\.]{:min,:max}"));
		final PageField<?> FLD_COUNTRY = buildField(COUNTRY_FLD_NM, false, false, "",
			new ValidateString(COUNTRY_FLD_NM, true, 2, 2, "^US$"));
		final PageField<?> FLD_STATE = buildField(STATE_FLD_NM, false, false, "",
			new ValidateString(STATE_FLD_NM, true, 2, 2, "[\\p{L} -'\\.]{:min,:max}"));
		final PageField<?> FLD_ZIP = buildField(ZIP_FLD_NM, false, false, "",null);
		final PageField<?> FLD_RESIDENT = buildField(RESIDENT_FLD_NM, false, false, "",
			new ValidateBoolean(RESIDENT_FLD_NM, true));

		final String PROFILE_FOUND_CONDITION_NM = "profileFound";
		final String PROFILE_NOT_FOUND_CONDITION_NM = String.format("Not %s", PROFILE_FOUND_CONDITION_NM);
		final String INELIGIBLE_STATE_CONDITION_NM = "ineligibleState";

		final NavigationProcessor INDEX_NAVIGATION = new ConditionalNavigation.Builder()
														 .condition(INELIGIBLE_STATE_CONDITION_NM, CONFIRMATION_VIEW_NM)
														 .defaultViewName(PROCESS_CODE_VIEW_NM)
														 .build();

		final AppPage index = AppPage.builder(INDEX_VIEW_NM, PageType.ENTRY_POINT)
							.controller(Controller.GET_PROFILE_CUSTOMERID)
							.condition(new NotCondition(new ProfileFoundCondition(PROFILE_FOUND_CONDITION_NM, 0)))
							.condition(new ProfileFoundCondition(PROFILE_FOUND_CONDITION_NM, 1))
							.condition(new FormFieldValueCondition(INELIGIBLE_STATE_CONDITION_NM, 2, STATE_FLD_NM,
								Arrays.asList("AK", "HI")))
								  .successNavigation(INDEX_NAVIGATION)
								  .failureNavigation(INDEX_NAVIGATION)
								  .errorNavigation(ERROR_VIEW_NM)
							.addField(FLD_PRODUCT)
							.addField(FLD_PROMO_CODE_INDEX)
							.addField(FLD_EMAIL_INDEX)
							.addField(FLD_FIRST_NAME)
							.addField(FLD_LAST_NAME)
							.addField(FLD_COUNTRY)
							.addField(FLD_STATE)
							.addField(FLD_ZIP)
							.addField(FLD_RESIDENT)
							.hiddenService(buildService("honeywellhomepresentedoffers", ServiceAction.ADD))
							.hiddenService(buildService("honeywellhomeemails", ServiceAction.ADD))
							.hiddenService(buildService("honeywellhomeasks", ServiceAction.ADD))
							.conditionalAction(PROFILE_NOT_FOUND_CONDITION_NM, List.of(
								NEW_PROFILE,
								new ServicesActionProcessor(),
								HOSTED_PAGES_LOGS
							))
							.conditionalAction(PROFILE_FOUND_CONDITION_NM, List.of(
								EXISTING_PROFILE,
								new ServicesActionProcessor(),
								HOSTED_PAGES_LOGS
							))
							.build();

		// These are similar to the fields defined for the index page except that the submit values are toggled, so that
		// email is submitted on the index page and promo_code on the processCode page
		PageField<?> FLD_PROMO_CODE_PROCESS = buildField(PROMO_CODE_FLD_NM, false, true, "", VALIDATE_PROMO_CODE);
		PageField<?> FLD_EMAIL_PROCESS = buildField(EMAIL, false, false, "", VALIDATE_EMAIL);
		PageField<?> FLD_CUSTOMER_ID_HASH = buildField(CUS_CUSTOMER_ID_HASH, false, false, "",
			new ValidateString(CustomPagesConstants.CUS_CUSTOMER_ID_HASH, true, null, null, ""));

		final String COUPON_CODES_RESOURCE_NM = "cusCoupon_codes";
		final String CC_ASSIGNED_DATE_NM = "assigned_date";
		final String CC_CUSTOMERID_NM = "customerID";
		final String CC_FIRST_NAME_NM = "first_name";
		final String CC_LAST_NAME_NM = "last_name";
		final String CC_PRODUCT_NM = "product";
		final String CC_REDEEMED_DATE_NM = "redeemed_date";
		final String CC_STATUS_NM = "status";
		final String CC_STATUS_REDEEMED = "R";

		final CustomResourcesActionProcessor UPDATE_COUPON = new CustomResourcesActionProcessor(Collections.singletonList(
			buildCustomResourceConfig(COUPON_CODES_RESOURCE_NM,
				Arrays.asList(
					buildCustomResourceField(
						new CustomResourceFieldValueGenerator.Builder()
							.acsFieldName(PKEY)
							.adobeDataType(ADT_STRING)
							.fieldName(PKEY)
							.build()
					),
					buildCustomResourceField(
						new FormFieldValueGenerator.Builder()
							.acsFieldName(CC_CUSTOMERID_NM)
							.adobeDataType(ADT_STRING)
							.fieldName(EMAIL)
							.build()
					),
					buildCustomResourceField(
						new CurrentDateTimeValueGenerator.Builder()
							.acsFieldName(CC_ASSIGNED_DATE_NM)
							.zoneId(ZONE_CST)
							.build()
					),
					buildCustomResourceField(
						new FormFieldValueGenerator.Builder()
							.acsFieldName(CC_FIRST_NAME_NM)
							.adobeDataType(ADT_STRING)
							.fieldName(FIRST_NAME)
							.build()
					),
					buildCustomResourceField(
						new FormFieldValueGenerator.Builder()
							.acsFieldName(CC_LAST_NAME_NM)
							.adobeDataType(ADT_STRING)
							.fieldName(LAST_NAME)
							.build()
					),
					buildCustomResourceField(
						new CurrentDateValueGenerator.Builder()
							.acsFieldName(CC_REDEEMED_DATE_NM)
							.build()
					),
					buildCustomResourceField(
						new ConstantValueGenerator.Builder<>()
							.acsFieldName(CC_STATUS_NM)
							.adobeDataType(ADT_STRING)
							.value(CC_STATUS_REDEEMED)
							.build()
					)
				))
		));

		final TriggeredEmailConfig CONFIRMATION_EMAIL_CONFIG = new TriggeredEmailConfig.Builder()
														   .contextField(new FormFieldValueGenerator.Builder()
																			 .acsFieldName(CUSTOMER_ID_HASH)
																			 .adobeDataType(ADT_STRING)
																			 .fieldName(CUS_CUSTOMER_ID_HASH)
																			 .build())
														   .email(new FormFieldValueGenerator.Builder()
																	  .acsFieldName(EMAIL)
																	  .adobeDataType(ADT_STRING)
																	  .fieldName(EMAIL)
																	  .build())
														   .eventIds(Map.of(
														   	"honeywellhome", "EVTfreeFilterResideo",
															   "resideo", "EVTfreeFilterResideo"))
														   .build();

		final TriggerEmailActionProcessor CONFIRMATION_EMAIL = new TriggerEmailActionProcessor(CONFIRMATION_EMAIL_CONFIG);

		List<ActionProcessor> UPDATE_COUPON_ACTIONS = Arrays.asList(UPDATE_COUPON, CONFIRMATION_EMAIL);

		final String CODE_FOUND_CONDITION_NM = "codeFound";
		final String CODE_NOT_FOUND_CONDITION_NM = String.format("Not %s", CODE_FOUND_CONDITION_NM);
		final String CODE_NOT_AVAILABLE_CONDITION_NM = "codeNotAvailable";
		final String CODE_VALID_PRD_CONDITION_NM = "codeHasValidProduct";
		final String CODE_INVALID_PRD_CONDITION_NM = String.format("Not %s", CODE_VALID_PRD_CONDITION_NM);
		final String CODE_AVAILABLE_CONDITION_NM = "codeAvailable";

		final Condition CODE_NOT_FOUND_CONDITION = new NotCondition(new CustomResourceFoundCondition(CODE_FOUND_CONDITION_NM, 0));
		final Condition CODE_INV_PRD_CONDITION = new NotCondition(new CustomResourceFieldValueCondition(CODE_VALID_PRD_CONDITION_NM,
			1, CC_PRODUCT_NM, false, Collections.singletonList("FreeFilter")));
		final Condition CODE_NOT_AVAILABLE_CONDITION = new CustomResourceFieldValueCondition(CODE_NOT_AVAILABLE_CONDITION_NM,
			2, CC_STATUS_NM, false, Collections.singletonList("R"));
		final Condition CODE_AVAILABLE_CONDITION = new CustomResourceFieldValueCondition(CODE_AVAILABLE_CONDITION_NM,
			3, CC_STATUS_NM, true, Arrays.asList("A", "N"));

		AppPage processCode = AppPage.builder(PROCESS_CODE_VIEW_NM, PageType.PAGE)
								  .controller(Controller.GET_CUSTOM_RESOURCE_BY_ID)
								  .customResource(COUPON_CODES_RESOURCE_NM)
								  .condition(CODE_NOT_FOUND_CONDITION)
								  .condition(CODE_INV_PRD_CONDITION)
								  .condition(CODE_NOT_AVAILABLE_CONDITION)
								  .condition(CODE_AVAILABLE_CONDITION)
								  .successNavigation(new ConditionalNavigation.Builder()
														 .condition(CODE_INVALID_PRD_CONDITION_NM, INVALID_CODE_VIEW_NM)
														 .condition(CODE_NOT_AVAILABLE_CONDITION_NM, INVALID_CODE_VIEW_NM)
														 .defaultViewName(CONFIRMATION_VIEW_NM)
														 .build())
								  .failureNavigation(INVALID_CODE_VIEW_NM)
								  .conditionalAction(CODE_NOT_FOUND_CONDITION_NM, Collections.emptyList())
								  .conditionalAction(CODE_INVALID_PRD_CONDITION_NM, Collections.emptyList())
								  .conditionalAction(CODE_NOT_AVAILABLE_CONDITION_NM, Collections.emptyList())
								  .conditionalAction(CODE_AVAILABLE_CONDITION_NM, UPDATE_COUPON_ACTIONS)
								  .addField(FLD_PROMO_CODE_PROCESS)
								  .addField(FLD_EMAIL_PROCESS)
								  .addField(FLD_PRODUCT)
								  .addField(FLD_FIRST_NAME)
								  .addField(FLD_LAST_NAME)
								  .addField(FLD_STATE)
								  .addField(FLD_ZIP)
								  .addField(FLD_RESIDENT)
								  .addField(FLD_CUSTOMER_ID_HASH)
								  .errorNavigation(ERROR_VIEW_NM)
								  .build();

		final AppPage confirmation = AppPage.builder(CONFIRMATION_VIEW_NM, PageType.PAGE)
										 .controller(Controller.CONFIRMATION_PAGE)
										 .addField(FLD_STATE)
										 .build();

		final AppPage invalidCode = AppPage.builder(INVALID_CODE_VIEW_NM, PageType.PAGE)
										.controller(Controller.GENERIC_PAGE)
										.build();

		final AppPage error = AppPage.builder(ERROR_VIEW_NM, PageType.ERROR)
								  .controller(Controller.ERROR_PAGE)
								  .build();

		return AppDetails.builder("resideo", "freeFilter")
				   .enable()
				   .addPage(index)
				   .addPage(processCode)
				   .addPage(confirmation)
				   .addPage(invalidCode)
				   .addPage(error)
				   .build();
	}
}
