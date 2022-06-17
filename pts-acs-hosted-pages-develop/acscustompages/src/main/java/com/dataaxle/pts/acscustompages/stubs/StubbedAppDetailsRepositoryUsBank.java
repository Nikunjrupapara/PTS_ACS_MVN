package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.conditions.FormFieldValueCondition;
import com.dataaxle.pts.acscustompages.model.navigation.ConditionalNavigation;
import com.dataaxle.pts.acscustompages.model.validation.ValidateBoolean;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.validation.ValidateString;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ConstantValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CurrentDateTimeValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.ProfileActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StubbedAppDetailsRepositoryUsBank extends StubbedAppDetailBase {
	static AppDetails usbank_preferences_generic() {
		AppPage lookup = AppPage.builder("lookup", PageType.PAGE).controller(Controller.GET_PROFILE_SECURELINK).successNavigation("preferences")
			.failureNavigation("signup").build();
		AppPage signup = AppPage.builder("signup", PageType.ENTRY_POINT)
							 .controller(Controller.GET_PROFILE_FILTER)
							 .successNavigation("preferences")
							 .failureNavigation("signup-new")
							.profileFilterName("byEmail_Acctl4_Partner")
							 .profileFilterParameter("cusAcctL4", "acctl4")
							.profileFilterParameter("cusPartner", "partner")
							 .fields(
							 	Arrays.asList(
							 		buildField("email", false, true, "",
										new ValidateEmail("email", true, true,
											true, 5, 100)),
									buildField("confirmEmail", false, false, "",
										new ValidateString("confirmEmail", true, 5,
											100, "")),
									buildField("cusAcctL4", false, true, "",
										new ValidateString("cusAcctL4", true, 4, 4, "[0-9]{4}")),
									buildField("cusPartner", true, true, "",
										new ValidateString("cusPartner", true, 2, 3, "[A-Z]{:min,:max}"))
								)).build();
		AppPage signupNew = AppPage.builder("signup-new", PageType.PAGE)
								.controller(Controller.CREATE_PROFILE)
								.successNavigation("confirm")
								.failureNavigation("error")
								.fields(Arrays.asList(
									buildField("email", true, true, "",
										new ValidateEmail("email", true, true,
											false, 5, 100)),
									buildField("cusAcctL4", true, true, "",
										new ValidateString("cusAcctL4", true, 4, 4, "[0-9]{4}")),
									buildField("cusPartner", true, true, "",
										new ValidateString("cusPartner", true, 2, 3, "[A-Z]{:min,:max}")),
									buildField("firstName", false, true, "",
										new ValidateString("firstName", true, 2, 20, "")),
									buildField("lastName", false, true, "",
										new ValidateString("lastName", true, 2, 20, "[\\p{L} -'\\.]{:min,:max}")),
									buildField("cusOver18", false, false, false,
										new ValidateBoolean("cusOver18", true)),
									buildField("cusProfileKey", true, false, "", null),
									buildField("cusZipCode", false, true, "",
										new ValidateString("cusZipCode", true, 5, 5, "[0-9]{:min,:max}"))
								))
								.hiddenService(buildService("marketing", ServiceAction.ADD))
								.hiddenService(buildService("service", ServiceAction.ADD))
								.defaultActions(Arrays.asList(
									new ProfileActionProcessor(List.of(
										new FormFieldValueGenerator.Builder().acsFieldName("cusAcctL4")
														 .adobeDataType("string").fieldName("cusAcctL4").build(),
										new FormFieldValueGenerator.Builder().acsFieldName("cusPartner")
														  .adobeDataType("string").fieldName("cusPartner").build(),
										new FormFieldValueGenerator.Builder().acsFieldName("cusPostalCode")
															 .adobeDataType("string").fieldName("cusZipCode").build(),
										new FormFieldValueGenerator.Builder().acsFieldName("cusProfileKey")
											.adobeDataType("string").fieldName("cusProfileKey").build(),
										new ConstantValueGenerator.Builder<>().acsFieldName("cusSignUpHostedPage")
											.adobeDataType("string").value("Yes").build(),
										new FormFieldValueGenerator.Builder().acsFieldName("email")
													  .adobeDataType("string").fieldName("email").build(),
										new FormFieldValueGenerator.Builder().acsFieldName("firstName")
														  .adobeDataType("string").fieldName("firstName").build(),
										new FormFieldValueGenerator.Builder().acsFieldName("lastName")
														 .adobeDataType("string").fieldName("lastName").build()
									)),
									new ServicesActionProcessor()
//									,new TriggerEmailActionProcessor(
//										new TriggeredEmailConfig.Builder()
//											.eventIds(usbankTriggeredEmails())
//											.email(new FormFieldValueGenerator.Builder()
//													   .acsFieldName("email")
//													   .adobeDataType("string")
//													   .fieldName("email")
//													   .build())
//											.contextField(
//												new FormFieldValueGenerator.Builder()
//													.acsFieldName("customerIdHash")
//													.adobeDataType("string")
//													.fieldName("cusCustomerIdHash")
//													.build()
//											).build()
//									)
								))
								.build();
		AppPage preferences = AppPage.builder("preferences", PageType.PAGE)
								.controller(Controller.UPDATE_SERVICES)
								.condition(new FormFieldValueCondition("userSubscribed", 0, "radios", "Y"))
								.condition(new FormFieldValueCondition("userUnsubscribed", 1, "radios", "N"))
								.successNavigation(new ConditionalNavigation.Builder()
													   .condition("userUnsubscribed", "unsub-survey")
													   .defaultViewName("confirm")
													   .build())
								.failureNavigation("error")
								.fields(Arrays.asList(
								  	buildField("email", true, true, "",
										new ValidateEmail("email", true, true,
											false, 5, 100)),
									buildField("cusAcctL4", true, true, "",
										new ValidateString("cusAcctL4", true, 4, 4, "[0-9]{4}")),
									buildField("cusPartner", true, true, "",
										new ValidateString("cusPartner", true, 2, 3, "[A-Z]{:min,:max}")),
									buildField("cusCustomerIdHash", true, false, "",
										new ValidateString("cusCustomerIdHash", true, 32, 256, "")),
									buildField("radios", false, false, "",
										new ValidateString("radios", true, 1, 1, "[N|Y]"))
								))
								.serviceField(buildServiceField("radios", "marketing", "Y", "N"))
								.conditionalAction("userSubscribed", Arrays.asList(
									new ServicesActionProcessor()
//									,new TriggerEmailActionProcessor(
//										TriggeredEmailConfig.builder()
//											.eventIds(usbankTriggeredEmails())
//											.email(new FormFieldValueGenerator.Builder()
//													   .acsFieldName("email")
//													   .adobeDataType("string")
//													   .fieldName("email")
//													   .build())
//											.contextField(
//												new FormFieldValueGenerator.Builder()
//													.acsFieldName("customerIdHash")
//													.adobeDataType("string")
//													.fieldName("cusCustomerIdHash")
//													.build()
//											).build()
//										)
									)
								)
								.conditionalAction("userUnsubscribed", Collections.singletonList(
									new ServicesActionProcessor()
								))
								  .disableSessionStorage()
								.missingInputView("signup")
								.build();
		AppPage unsubSurvey = AppPage.builder("unsub-survey", PageType.PAGE)
								.controller(Controller.UPDATE_PROFILE)
								.successNavigation("confirm")
								.failureNavigation("error")
								.fields(Arrays.asList(
									buildField("email", true, true, "",
										new ValidateEmail("email", true, false, false, 5, 100)),
									buildField("cusAcctL4", true, true, "",
										new ValidateString("cusAcctL4", true, 4, 4, "[0-9]{4}")),
									buildField("cusPartner", true, true, "",
										new ValidateString("cusPartner", true, 2, 3, "[A-Z]{:min,:max}")),
									buildField("cusCustomerIdHash", true, false, "",
										new ValidateString("cusCustomerIdHash", true, 32, 256, "")),
									buildField("cusUnsubReason", false, true, "",
										new ValidateString("cusUnsubReason", true, 1, 1, "")),
									buildField("cusUnsubReasonDesc", false, true, "",
										new ValidateString("cusUnsubReason", false, 0, 255, "[\\p{Alnum}\\p{Punct}]{:min,:max}"))
									)
								)
								.defaultActions(
									Arrays.asList(
										new ProfileActionProcessor(List.of(
											new FormFieldValueGenerator.Builder().acsFieldName("cusUnsubReason")
																  .adobeDataType("string").fieldName("cusUnsubReason").build(),
											new FormFieldValueGenerator.Builder().acsFieldName("cusUnsubReasonDesc")
																  .adobeDataType("string").fieldName("cusUnsubReasonDesc").build(),
											new CurrentDateTimeValueGenerator
													.Builder().acsFieldName("cusUnsubReasonDate").build()
										))
//										,new TriggerEmailActionProcessor(
//											TriggeredEmailConfig.builder()
//												.eventIds(usbankTriggeredEmails())
//												.email(new FormFieldValueGenerator.Builder()
//														   .acsFieldName("email")
//														   .adobeDataType("string")
//														   .fieldName("email")
//														   .build())
//												.contextField(
//													new FormFieldValueGenerator.Builder()
//														.acsFieldName("customerIdHash")
//														.adobeDataType("string")
//														.fieldName("cusCustomerIdHash")
//														.build()
//												).build()
//										)
									)
								)
								.missingInputView("signup")
								  .disableSessionStorage()
								.build();
		AppPage confirm = AppPage.builder("confirm", PageType.PAGE)
								.controller(Controller.PROFILE_CONFIRM)
								.fields(Arrays.asList(
							  		buildField("email", true, true, "", null),
									buildField("confirmEmail", true, false, "", null),
									buildField("cusAcctL4", true, true, "", null),
									buildField("cusPartner", true, true, "BMW", null)
								))
								.missingInputView("signup")
								.build();
		AppPage error = AppPage.builder("error", PageType.ERROR).controller(Controller.ERROR_PAGE).build();
		return AppDetails.builder("usbank", "preferences")
				   .enable()
				   .jwt("")
				   .pages(Arrays.asList(lookup, signup, signupNew, preferences, confirm, unsubSurvey, error))
				   .addHeader("Content-Security-Policy", "frame-ancestors 'self'")
				   .addHeader("X-Frame-Options", "SAMEORIGIN")
				   .build();
	}

	static AppDetails usbank_preferences_fidelity() {
		AppPage lookup = AppPage.builder("lookup", PageType.PAGE).controller(Controller.GET_PROFILE_SECURELINK).successNavigation("preferences")
			.failureNavigation("login").build();
		AppPage login = AppPage.builder("login", PageType.ENTRY_POINT)
							.controller(Controller.GET_PROFILE_FILTER)
							.successNavigation("preferences")
							.failureNavigation("error")
							.profileFilterName("byEmail_Acctl4_Partner")
							.profileFilterParameter("cusAcctL4", "acctl4")
							.profileFilterParameter("cusPartner", "partner")
							.fields(Arrays.asList(
								buildField("email", false, true, "",
									new ValidateEmail("email", true, true, false, 5, 100)),
								buildField("cusAcctL4", false, true, "",
									new ValidateString("cusAcctL4", true, 4, 4, "[0-9]{4}")),
								buildField("cusPartner", true, true, "",
									new ValidateString("cusPartner", true, 3, 3, "[A-Z]{:min,:max}"))
							)).build();
		AppPage preferences = AppPage.builder("preferences", PageType.PAGE)
								.controller(Controller.UPDATE_SERVICES)
								.condition(new FormFieldValueCondition("userSubscribed", 0, "radios", "Y"))
								.condition(new FormFieldValueCondition("userUnsubscribed", 1, "radios", "N"))
								.successNavigation(new ConditionalNavigation.Builder()
													   .condition("userUnsubscribed", "unsub-survey")
													   .defaultViewName("confirm")
													   .build())
								.failureNavigation("error")
								.fields(Arrays.asList(
								  	buildField("email", true, true, "",
										new ValidateEmail("email", true, true, false, 5, 100)),
									buildField("cusAcctL4", true, true, "",
										new ValidateString("cusAcctL4", true, 4, 4, "[0-9]{4}")),
									buildField("cusPartner", true, true, "",
										new ValidateString("cusPartner", true, 3, 3, "[A-Z]{:min,:max}")),
									buildField("cusCustomerIdHash", true, false, "",
										new ValidateString("cusCustomerIdHash", true, 32, 256, "")),
									buildField("radios", false, false, "",
										new ValidateString("radios", true, 1, 1, "[N|Y]"))
								))
								.serviceField(buildServiceField("radios", "marketing", "Y", "N"))
								.conditionalAction("userSubscribed", Arrays.asList(
									new ServicesActionProcessor()
//									,new TriggerEmailActionProcessor(
//										TriggeredEmailConfig.builder()
//											.eventIds(usbankTriggeredEmails())
//											.email(new FormFieldValueGenerator.Builder()
//													   .acsFieldName("email")
//													   .adobeDataType("string")
//													   .fieldName("email")
//													   .build())
//											.contextField(
//												new FormFieldValueGenerator.Builder()
//													.acsFieldName("customerIdHash")
//													.adobeDataType("string")
//													.fieldName("cusCustomerIdHash")
//													.build()
//											).build()
//									)
								))
								.conditionalAction("userUnsubscribed", Collections.singletonList(
									new ServicesActionProcessor()
								))
								.missingInputView("login")
								.build();
		AppPage unsubSurvey = AppPage.builder("unsub-survey", PageType.PAGE)
								  .controller(Controller.UPDATE_PROFILE)
								  .successNavigation("confirm")
								  .failureNavigation("error")
								  .fields(Arrays.asList(
									  buildField("email", true, true, "",
										  new ValidateEmail("email", true, false, false, 5, 100)),
									  buildField("cusAcctL4", true, true, "",
										  new ValidateString("cusAcctL4", true, 4, 4, "[0-9]{4}")),
									  buildField("cusPartner", true, true, "",
										  new ValidateString("cusPartner", true, 2, 3, "[A-Z]{:min,:max}")),
									  buildField("cusCustomerIdHash", true, false, "",
										  new ValidateString("cusCustomerIdHash", true, 32, 256, "")),
									  buildField("cusUnsubReason", false, true, "",
										  new ValidateString("cusUnsubReason", true, 1, 1, "")),
									  buildField("cusUnsubReasonDesc", false, true, "",
										  new ValidateString("cusUnsubReason", false, 0, 255, "[\\p{Alnum}\\p{Punct}]{:min,:max}"))
									  )
								  )
								  .defaultActions(
									  Arrays.asList(
										  new ProfileActionProcessor(List.of(
											  new FormFieldValueGenerator.Builder().acsFieldName("cusUnsubReason")
																	.adobeDataType("string").fieldName("cusUnsubReason").build(),
											  new FormFieldValueGenerator.Builder().acsFieldName("cusUnsubReasonDesc")
																		.adobeDataType("string").fieldName("cusUnsubReasonDesc").build(),
											  new CurrentDateTimeValueGenerator.Builder().acsFieldName("cusUnsubReasonDate").build()
										  ))
//										,new TriggerEmailActionProcessor(
//											  TriggeredEmailConfig.builder()
//												  .eventIds(usbankTriggeredEmails())
//												  .email(new FormFieldValueGenerator.Builder()
//															 .acsFieldName("email")
//															 .adobeDataType("string")
//															 .fieldName("email")
//															 .build())
//												  .contextField(
//													  new FormFieldValueGenerator.Builder()
//														  .acsFieldName("customerIdHash")
//														  .adobeDataType("string")
//														  .fieldName("cusCustomerIdHash")
//														  .build()
//												  ).build()
//										  )
									  )
								  )
								  .missingInputView("signup")
								  .build();
		AppPage confirm = AppPage.builder("confirm", PageType.PAGE)
							.controller(Controller.PROFILE_CONFIRM)
							.fields(Arrays.asList(
								buildField("email", true, true, "", null),
								buildField("cusAcctL4", true, true, "", null),
								buildField("cusPartner", true, true, "", null)
							)).missingInputView("login").build();
		AppPage error = AppPage.builder("error", PageType.ERROR).controller(Controller.ERROR_PAGE).build();
		return AppDetails.builder("usbank", "fidelity")
				   .enable()
				   .jwt("")
				   .pages(Arrays.asList(lookup, login, preferences, confirm, unsubSurvey, error))
				   .addHeader("Content-Security-Policy", "frame-ancestors 'self'")
				   .addHeader("X-Frame-Options", "SAMEORIGIN")
				   .build();
	}
}
