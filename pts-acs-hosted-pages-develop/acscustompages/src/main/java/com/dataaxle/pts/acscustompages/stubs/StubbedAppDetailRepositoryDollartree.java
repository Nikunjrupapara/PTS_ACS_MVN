package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.HiddenService;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.TriggeredEmailConfig;
import com.dataaxle.pts.acscustompages.model.conditions.AlwaysTrueCondition;
import com.dataaxle.pts.acscustompages.model.conditions.HasServiceCondition;
import com.dataaxle.pts.acscustompages.model.conditions.NotCondition;
import com.dataaxle.pts.acscustompages.model.conditions.ProfileFoundCondition;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.validation.ValidateString;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ConstantValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CurrentDateTimeValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.OnOffValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.ProfileActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.TriggerEmailActionProcessor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StubbedAppDetailRepositoryDollartree extends StubbedAppDetailBase{

	static ValueGenerator<?> email = new FormFieldValueGenerator.Builder()
			.acsFieldName("email")
			.adobeDataType("string")
			.fieldName("email")
			.build();
	static ValueGenerator<?> firstName = new FormFieldValueGenerator.Builder()
			.acsFieldName("firstName")
			.adobeDataType("string")
			.fieldName("firstName")
			.build();
	static ValueGenerator<?> lastName = new FormFieldValueGenerator.Builder()
			.acsFieldName("lastName")
			.adobeDataType("string")
			.fieldName("lastName")
			.build();

	static ValueGenerator<?> zip = new FormFieldValueGenerator.Builder().acsFieldName("location.zipCode")
												.adobeDataType("string").fieldName("zip").build();


	static ValueGenerator<?> source = new ConstantValueGenerator.Builder().value("Signup").acsFieldName("cusSource").adobeDataType("string").build();

	static TriggeredEmailConfig emailConfig = new TriggeredEmailConfig.Builder()
			.eventIds(
					Map.of("dollartree", "EVT_signupWelcome"))
			.email(
					email)
			.contextField(
					new FormFieldValueGenerator.Builder()
							.acsFieldName("customerIdHash")
							.adobeDataType("string")
							.fieldName("cusCustomerIdHash")
							.build()
			)
			.build();


	static ValueGenerator<?> artsCraftsCategory = new OnOffValueGenerator.Builder()
			.acsFieldName("cusCraftOrg").acsOnValue("Y").acsOffValue("N")
			.actsUpon("formData").adobeDataType("string").fieldName("cusCraftOrg")
			.formOnValue("Y").build();

	static ValueGenerator<?> floristOrg = new OnOffValueGenerator.Builder()
			.acsFieldName("cusFloristOrg").acsOnValue("Y").acsOffValue("N")
			.actsUpon("formData").adobeDataType("string").fieldName("floristOrg")
			.formOnValue("Y").build();

	static ValueGenerator<?> educationOrg =	new OnOffValueGenerator.Builder()
			.acsFieldName("cusEducationOrg").acsOnValue("Y").acsOffValue("N")
			.actsUpon("formData").adobeDataType("string").fieldName("educationOrg")
			.formOnValue("Y").build();

	static ValueGenerator<?> nonprofitOrg = new OnOffValueGenerator.Builder()
			.acsFieldName("cusNonprofitOrg").acsOnValue("Y").acsOffValue("N")
			.actsUpon("formData").adobeDataType("string").fieldName("nonprofitOrg")
			.formOnValue("Y").build();

	static ValueGenerator<?> restaurantOrg = new OnOffValueGenerator.Builder()
			.acsFieldName("cusRestaurantOrg").acsOnValue("Y").acsOffValue("N")
			.actsUpon("formData").adobeDataType("string").fieldName("restaurantOrg")
			.formOnValue("Y").build();

	static ValueGenerator<?> cateringOrg  =	new OnOffValueGenerator.Builder()
			.acsFieldName("cusCateringOrg").acsOnValue("Y").acsOffValue("N")
			.actsUpon("formData").adobeDataType("string").fieldName("cateringOrg")
			.formOnValue("Y").build();

	static ValueGenerator<?> constCusNewMerchCategory = new ConstantValueGenerator.Builder<String>().acsFieldName("cusNewMerchCategory")
												.adobeDataType("string").value("Y").build();


	public static AppDetails contest() {
		ValueGenerator<?> source = new ConstantValueGenerator.Builder().value("contest").acsFieldName("cusSource").adobeDataType("string").build();


		AppPage signup = AppPage.builder("signup", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_CUSTOMERID)
				.condition(new NotCondition(new ProfileFoundCondition("profileExists", 0)))
				.condition(new HasServiceCondition("subscribedToDTMarketing", 2, "DTmarketing"))
				.condition(new NotCondition(new HasServiceCondition("subscribedToDTMarketing", 3, "DTmarketing")))
				.successNavigation("confirm")
				.failureNavigation("confirm")
				.fields(Arrays.asList(
						buildField("subscribe", false, false, "",
								new ValidateString("subscribe", true, null, null, "Y")),

						buildField("email", false, true, "",
								new ValidateEmail("email", true, true, false, null, null)),

						buildField("firstName", false, false, "",
								new ValidateString("firstName", false, 0, 50, "")),

						buildField("lastName", false, false, "",
								new ValidateString("lastName", false, 0, 50, "")),

						buildField("zip", false, false, "",
								new ValidateString("zip", true, 5, 5, "[0-9]{:min,:max}")),
						buildField("cusCraftOrg", false, false, "",
								new ValidateString("cusCraftOrg", false, 0, 5, "")),
						buildField("floristOrg", false, false, "",
								new ValidateString("floristOrg", false, 0, 5, "")),
						buildField("educationOrg", false, false, "",
								new ValidateString("educationOrg", false, 0, 5, "")),
						buildField("nonprofitOrg", false, false, "",
								new ValidateString("nonprofitOrg", false, 0, 5, "")),
						buildField("restaurantOrg", false, false, "",
								new ValidateString("restaurantOrg", false, 0, 5, "")),
						buildField("cateringOrg", false, false, "",
								new ValidateString("cateringOrg", false, 0, 5, ""))

				))
				.hiddenService(buildService("DTmarketing", ServiceAction.ADD))
				.conditionalAction("Not profileExists",Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										zip,

										firstName,
										lastName,

										source,
										artsCraftsCategory,
										floristOrg,
										educationOrg,
										nonprofitOrg,
										restaurantOrg,
										cateringOrg,
										constCusNewMerchCategory,
										new CurrentDateTimeValueGenerator.Builder()
														.acsFieldName("cusContestDate").build()

//										,new OnOffValueGenerator.Builder()
//												.acsFieldName("cusNewMerchCategory").acsOnValue("Y").acsOffValue("N")
//												.actsUpon("formData").adobeDataType("string").fieldName("newMerchCategory")
//												.formOnValue("Y").build()
								)
						)
						,new ServicesActionProcessor()
						,new TriggerEmailActionProcessor(emailConfig)
				)).conditionalAction("Not subscribedToDTMarketing",Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										zip,

										firstName,
										lastName,

										source,
										artsCraftsCategory,
										floristOrg,
										educationOrg,
										nonprofitOrg,
										restaurantOrg,
										cateringOrg,
										constCusNewMerchCategory,
										new CurrentDateTimeValueGenerator.Builder()
												.acsFieldName("cusContestDate").build()
//
//										,new OnOffValueGenerator.Builder()
//												.acsFieldName("cusNewMerchCategory").acsOnValue("Y").acsOffValue("N")
//												.actsUpon("formData").adobeDataType("string").fieldName("newMerchCategory")
//												.formOnValue("Y").build()
								)
						)
						,new ServicesActionProcessor()
						,new TriggerEmailActionProcessor(emailConfig)
				)).conditionalAction("subscribedToDTMarketing",Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										zip,

										firstName,
										lastName,

										artsCraftsCategory,
										floristOrg,
										educationOrg,
										nonprofitOrg,
										restaurantOrg,
										cateringOrg,
										constCusNewMerchCategory,
										new CurrentDateTimeValueGenerator.Builder()
												.acsFieldName("cusContestDate").build()

//										,new OnOffValueGenerator.Builder()
//												.acsFieldName("cusNewMerchCategory").acsOnValue("Y").acsOffValue("N")
//												.actsUpon("formData").adobeDataType("string").fieldName("newMerchCategory")
//												.formOnValue("Y").build()
								)
						)
				))
				.build();

		AppPage confirmation = AppPage.builder("confirm", PageType.PAGE)
				.controller(Controller.CONFIRMATION_PAGE)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();

		AppPage privacy = AppPage.builder("privacy", PageType.ENTRY_POINT)
				.controller(Controller.GENERIC_PAGE)
				.build();

		AppPage rules = AppPage.builder("rules", PageType.ENTRY_POINT)
				.controller(Controller.GENERIC_PAGE)
				.build();

		AppPage winners = AppPage.builder("winners", PageType.ENTRY_POINT)
				.controller(Controller.GENERIC_PAGE)
				.build();

		AppPage error = AppPage.builder("error", PageType.ERROR)
				.controller(Controller.ERROR_PAGE)
				.build();

		return AppDetails.builder("dollartree", "contest")
				.enable()
				//.jwt("")
				.addPage(signup)
				.addPage(confirmation)
				.addPage(privacy)
				.addPage(rules)
				.addPage(winners)
				.addPage(error)
				.build();
	}




	public static AppDetails signup() {

		AppPage signup = AppPage.builder("index", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_CUSTOMERID)
				.condition(new NotCondition(new ProfileFoundCondition("profileExists", 0)))
				.condition(new HasServiceCondition("subscribedToDTMarketing", 2, "DTmarketing"))
				.condition(new NotCondition(new HasServiceCondition("subscribedToDTMarketing", 3, "DTmarketing")))
				.successNavigation("confirm")
				.failureNavigation("confirm")
				.fields(Arrays.asList(
						buildField("subscribe", false, false, "",
								new ValidateString("subscribe", true, null, null, "")),
						buildField("email", false, true, "",
								new ValidateEmail("email", true, true, false, null, null)),


						buildField("zip", false, false, "",
								new ValidateString("zip", true, 5, 5, "[0-9]{:min,:max}")),
						buildField("cusCraftOrg", false, false, "",
								new ValidateString("cusCraftOrg", false, 0, 5, "")),
						buildField("floristOrg", false, false, "",
								new ValidateString("floristOrg", false, 0, 5, "")),
						buildField("educationOrg", false, false, "",
								new ValidateString("educationOrg", false, 0, 5, "")),
						buildField("nonprofitOrg", false, false, "",
								new ValidateString("nonprofitOrg", false, 0, 5, "")),
						buildField("restaurantOrg", false, false, "",
								new ValidateString("restaurantOrg", false, 0, 5, "")),
						buildField("cateringOrg", false, false, "",
								new ValidateString("cateringOrg", false, 0, 5, ""))

				))
				.hiddenService(buildService("DTmarketing", ServiceAction.ADD))
				.conditionalAction("Not profileExists",Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										zip,
										source,
										artsCraftsCategory,
										floristOrg,
										educationOrg,
										nonprofitOrg,
										restaurantOrg,
										cateringOrg,
										constCusNewMerchCategory
								)
						)
						,new ServicesActionProcessor()
						,new TriggerEmailActionProcessor(emailConfig)
				)).conditionalAction("Not subscribedToDTMarketing",Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										zip,
										source,
										artsCraftsCategory,
										floristOrg,
										educationOrg,
										nonprofitOrg,
										restaurantOrg,
										cateringOrg,
										constCusNewMerchCategory
								)
						)
						,new ServicesActionProcessor()
						,new TriggerEmailActionProcessor(emailConfig)
				)).conditionalAction("subscribedToDTMarketing",Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										zip,
										source,
										artsCraftsCategory,
										floristOrg,
										educationOrg,
										nonprofitOrg,
										restaurantOrg,
										cateringOrg,
										constCusNewMerchCategory
								)
						)
				))
				.build();

		AppPage confirmation = AppPage.builder("confirm", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();

		AppPage error = AppPage.builder("error", PageType.ERROR)
				.controller(Controller.ERROR_PAGE)
				.build();

		return AppDetails.builder("dollartree", "signup")
				.enable()
				//.jwt("")
				.addPage(signup)
				.addPage(confirmation)
				.addPage(error)
				.build();
	}



	public static AppDetails isignup() {
		AppPage signup = AppPage.builder("signup", PageType.ENTRY_POINT)
				.controller(Controller.POST_PROFILE)
				.condition(new NotCondition(new ProfileFoundCondition("profileExists", 0)))
				.condition(new HasServiceCondition("subscribedToDTMarketing", 2, "DTmarketing"))
				.condition(new NotCondition(new HasServiceCondition("subscribedToDTMarketing", 3, "DTmarketing")))
				.fields(Arrays.asList(
						buildField("email", true, true, "",
								new ValidateEmail("email", true, true, false, null, null)),
						buildField("zip", true, false, "",
								new ValidateString("zip", true, 5, 5, "[0-9]{:min,:max}"))

				))
				.hiddenService(buildService("DTmarketing", ServiceAction.ADD))
				.conditionalAction("Not profileExists",Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										zip,
										source
								)
						)
						,new ServicesActionProcessor()
						,new TriggerEmailActionProcessor(emailConfig)
				)).conditionalAction("Not subscribedToDTMarketing",Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										zip,
										source
								)
						)
						,new ServicesActionProcessor()
						,new TriggerEmailActionProcessor(emailConfig)
				)).conditionalAction("subscribedToDTMarketing",Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										zip,
										source
								)
						)
				))
			.recaptchaKey("6LeiCpMcAAAAAIjz35SrXWCSnKYBRl9OgjvV5Dd1")
			.recaptchaSecret("6LeiCpMcAAAAAJMthq1gkrd0b87s4kRSDxeA8AR8")
			.recaptchaVersion("v3")
			.recaptchaAction("submit")
			.recaptchaScore(0.5)
				.build();

		AppPage error = AppPage.builder("error", PageType.ERROR)
				.controller(Controller.ERROR_PAGE)
				.build();

		return AppDetails.builder("dollartree", "isignup")
				.enable()
				.addPage(signup)
				.addPage(error)
				.build();
	}



	public static AppDetails unsubscribe() {

		AppPage lookup = AppPage.builder("lookup", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_SECURELINK)
				.successNavigation("index")
				.failureNavigation("index")
				.build();

		AppPage unsubscribe = AppPage.builder("index", PageType.PROFILE)
				.controller(Controller.UPDATE_SERVICES)
				.addField(buildField("email", true, false, "", null))
				.addField(buildField("cusCustomerIdHash", true, false, "", null))
				.addField(buildField("cusCustomerUUID", true, false, "", null))
				.successNavigation("confirm")
				.failureNavigation("confirm")
				.hiddenService(new HiddenService("DTmarketing", ServiceAction.REMOVE))
				.defaultActions(Collections.singletonList(new ServicesActionProcessor()))
				.build();

		AppPage confirmation = AppPage.builder("confirm", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();

		AppPage unsubscribeEmail = AppPage.builder("byemail", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_CUSTOMERID)
				.addField(buildField("email", true, true, "", new ValidateEmail("email", true, true, false, null, null)))
				.successNavigation("index")
				.failureNavigation("confirm")
				.recaptchaSecret("6LdGBssbAAAAAIPUGTgYsP0dp3h3F5d7eg5AsAB-")
				.recaptchaKey("6LdGBssbAAAAAO25kOakgQEtoG0DxeSfGrPOw1qC")
				.build();

		AppPage error = AppPage.builder("error", PageType.ERROR)
				.controller(Controller.ERROR_PAGE)
				.build();


		return AppDetails.builder("dollartree", "unsubscribe")
				.enable()
				//.jwt("")
				.addPage(unsubscribe)
				.addPage(unsubscribeEmail)
				.addPage(confirmation)
				.addPage(lookup)
				.addPage(error)
				.build();
	}


	public static AppDetails preferences() {

		AppPage lookup = AppPage.builder("lookup", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_SECURELINK)
				.successNavigation("index")
				.failureNavigation("error")
				.build();

		AppPage signup = AppPage.builder("index", PageType.PROFILE)
				.controller(Controller.UPDATE_PROFILE)
				.defaultCondition()
				.successNavigation("confirm")
				.failureNavigation("error") // profile is not found
				.fields(Arrays.asList(
						buildField("email", false, true, "",
								new ValidateEmail("email", true, true, false, null, null)),
						buildField("location.zipCode", false, true, "",
								new ValidateString("location.zipCode", true, 5, 5, "[0-9]{:min,:max}")),

						buildField("cusCraftOrg", false, true, "",
								new ValidateString("cusCraftOrg", false, 0, 5, "")),
						buildField("cusFloristOrg", false, true, "",
								new ValidateString("cusFloristOrg", false, 0, 5, "")),
						buildField("cusEducationOrg", false, true, "",
								new ValidateString("cusEducationOrg", false, 0, 5, "")),
						buildField("cusNonprofitOrg", false, true, "",
								new ValidateString("cusNonprofitOrg", false, 0, 5, "")),
						buildField("cusRestaurantOrg", false, true, "",
								new ValidateString("cusRestaurantOrg", false, 0, 5, "")),
						buildField("cusCateringOrg", false, true, "",
								new ValidateString("cusCateringOrg", false, 0, 5, "")),


						buildField("cusCustomerIdHash", true, false, "", null),
						buildField("cusCustomerUUID", true, false, "", null)


				))
				.hiddenService(buildService("DTmarketing", ServiceAction.ADD))
				.defaultActions(Arrays.asList(
						new ProfileActionProcessor(
								List.of(email,
										source,
										new FormFieldValueGenerator.Builder().acsFieldName("location.zipCode")
												.adobeDataType("string").fieldName("location.zipCode").build(),

										new OnOffValueGenerator.Builder()
												.acsFieldName("cusCraftOrg").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusCraftOrg")
												.formOnValue("Y").build(),
										new OnOffValueGenerator.Builder()
												.acsFieldName("cusFloristOrg").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusFloristOrg")
												.formOnValue("Y").build(),
										new OnOffValueGenerator.Builder()
												.acsFieldName("cusEducationOrg").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusEducationOrg")
												.formOnValue("Y").build(),
										new OnOffValueGenerator.Builder()
												.acsFieldName("cusNonprofitOrg").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusNonprofitOrg")
												.formOnValue("Y").build(),
										new OnOffValueGenerator.Builder()
												.acsFieldName("cusRestaurantOrg").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusRestaurantOrg")
												.formOnValue("Y").build(),
										new OnOffValueGenerator.Builder()
												.acsFieldName("cusCateringOrg").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusCateringOrg")
												.formOnValue("Y").build()

								)
						),
						new ServicesActionProcessor()
//						,new TriggerEmailActionProcessor(emailConfig)
				))
				.build();

		AppPage confirmation = AppPage.builder("confirm", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();

		AppPage error = AppPage.builder("error", PageType.ERROR)
				.controller(Controller.ERROR_PAGE)
				.build();

		return AppDetails.builder("dollartree", "preferences")
				.enable()
				//.jwt("")
				.addPage(lookup)
				.addPage(signup)
				.addPage(confirmation)
				.addPage(error)
				.build();
	}
}
