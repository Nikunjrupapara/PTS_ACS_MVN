package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.HiddenService;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.TriggeredEmailConfig;
import com.dataaxle.pts.acscustompages.model.conditions.FormFieldValueCondition;
import com.dataaxle.pts.acscustompages.model.conditions.HasServiceCondition;
import com.dataaxle.pts.acscustompages.model.conditions.NotCondition;
import com.dataaxle.pts.acscustompages.model.navigation.ConditionalNavigation;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.valuegenerator.BooleanValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ConstantValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CurrentDateTimeValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.OnOffValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.CustomResourcesActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ProfileActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.TriggerEmailActionProcessor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StubbedAppDetailRepositoryDillards extends StubbedAppDetailBase{

	static ValueGenerator<?> email = new FormFieldValueGenerator.Builder()
			.acsFieldName("email")
			.adobeDataType("string")
			.fieldName("email")
			.build();

	public static AppDetails unsubscribe() {

		AppPage lookup = AppPage.builder("lookup", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_SECURELINK)
				.successNavigation("index")
				.failureNavigation("error")
				.forwardQueryParameters()
				.forwardParameter("d")
				.build();



		AppPage unsubscribe = AppPage.builder("index", PageType.PROFILE)
				.controller(Controller.UPDATE_SERVICES)

				.condition(new FormFieldValueCondition("emailChange", 0, "processEmailChange", "Y"))
				.condition(new FormFieldValueCondition("unsubscribe", 1, "processUnsub", "Y"))
				//.condition(new NotCondition(new HasServiceCondition("unsubscribe", 2, "marketing")))


				.addField(buildField("email", false, false, "", new ValidateEmail("email", true, true, false, 5, 255)))
				.addField(buildField("emailNew", false, true, "", new ValidateEmail("emailNew", true, true, false, 5, 255)))

				.addField(buildField("cusCustomerIdHash", true, false, "", null))
				.addField(buildField("cusCustomerUUID", true, false, "", null))
				.addField(buildField("processEmailChange", true, false, "Y", null))
				.addField(buildField("processUnsub", true, false, "Y", null))

				.addField(buildField("d", true, false, "", null))

				.successNavigation(new ConditionalNavigation.Builder()
						.condition("emailChange", "change-email-confirm")
//						.condition("Not unsubscribe", "unsubscribe-confirm")
						.condition("unsubscribe", "unsubscribe-confirm")
						.defaultViewName("index")
						.build())
				.failureNavigation("index")
				.hiddenService(new HiddenService("marketing", ServiceAction.REMOVE))
				.conditionalAction("emailChange", Arrays.asList(
						new CustomResourcesActionProcessor(
								Collections.singletonList(
										buildCustomResourceConfig("cusEcoa", Arrays.asList(
												buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
																.acsFieldName("customerId")
																.adobeDataType("string")
															// TODO: this field does not exist, so value is null. Use email instead?
																.fieldName("email")
																.build()), // from profile/form field
												buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
																.acsFieldName("previousEmail")
																.adobeDataType("string")
																.fieldName("email")
																.build()),
												buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
																.acsFieldName("newEmail")
																.adobeDataType("string")
																.fieldName("emailNew")
																.build()),
												buildCustomResourceField(
														new CurrentDateTimeValueGenerator.Builder()
														.acsFieldName("requestTime").build()),
												buildCustomResourceField(
														new ConstantValueGenerator.Builder<String>()
																.acsFieldName("txEventId")
																.adobeDataType("string")
																.value("")
																.build()),
												buildCustomResourceField(
														new ConstantValueGenerator.Builder<String>()
																.acsFieldName("wkfStatus")
																.adobeDataType("string")
																.value("")
																.build())
										))
								)
						)
				))
//				.conditionalAction("Not unsubscribe", Arrays.asList(
//						new ServicesActionProcessor()
//				))
				.conditionalAction("unsubscribe", Arrays.asList(
						new ServicesActionProcessor(),
						new ProfileActionProcessor(
								List.of(email,
										new FormFieldValueGenerator.Builder().acsFieldName("cusUnsubFeedback")
												.adobeDataType("string").fieldName("cusUnsubFeedback").build()
										,
										new OnOffValueGenerator.Builder()
												.acsFieldName("cusUnsubTooManyEmails").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusUnsubTooManyEmails")
												.formOnValue("Y").build(),
										new OnOffValueGenerator.Builder()
												.acsFieldName("cusUnsubNoInterestingContent").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusUnsubNoInterestingContent")
												.formOnValue("Y").build(),
										new OnOffValueGenerator.Builder()
												.acsFieldName("cusUnsubNoMoreShop").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusUnsubNoMoreShop")
												.formOnValue("Y").build(),
										new OnOffValueGenerator.Builder()
												.acsFieldName("cusUnsubNotViewablePhone").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusUnsubNotViewablePhone")
												.formOnValue("Y").build(),
										new OnOffValueGenerator.Builder()
												.acsFieldName("cusUnsubOther").acsOnValue("Y").acsOffValue("N")
												.actsUpon("formData").adobeDataType("string").fieldName("cusUnsubOther")
												.formOnValue("Y").build()

							)
						)

						,new CustomResourcesActionProcessor(
								Collections.singletonList(
										buildCustomResourceConfig("cusHostedPagesLogs", Arrays.asList(

												buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
																.acsFieldName("email")
																.adobeDataType("string")
																.fieldName("email")
																.build()),
												buildCustomResourceField(
														new ConstantValueGenerator.Builder().value("unsubscribe")
																.acsFieldName("source")
																.adobeDataType("string")
																.build()),
												buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
																.acsFieldName("deliveryLabel")
																.adobeDataType("string")
																.fieldName("d")
																.build()
												),
												buildCustomResourceField(
														new ConstantValueGenerator.Builder().value("unsub")
																.acsFieldName("action")
																.adobeDataType("string")
																.build())

										))
								)
						)
				))
				.build();



		/*
			cusEcoa
				customerId
				newEmail
				previousEmail
				requestTime
				txEventId
				wkfStatus
				title


		 */

		AppPage unsubConfirmation = AppPage.builder("unsubscribe-confirm", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();

		AppPage emailChangeConfirmation = AppPage.builder("change-email-confirm", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();




		AppPage error = AppPage.builder("error", PageType.ERROR)
				.controller(Controller.ERROR_PAGE)
				.build();


		return AppDetails.builder("dillards", "unsubscribe")
				.enable()
				//.jwt("")
				.addPage(error)
				.addPage(unsubscribe)
				.addPage(unsubConfirmation)
				.addPage(emailChangeConfirmation)
				.addPage(lookup)
				.build();
	}

	public static AppDetails unsubscribeadhoc() {


		AppPage lookup = AppPage.builder("lookup", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_SECURELINK)
				.successNavigation("index")
				.failureNavigation("error")
				.build();



		AppPage unsubscribe = AppPage.builder("index", PageType.PROFILE)
				.controller(Controller.UPDATE_SERVICES)

				.condition(new FormFieldValueCondition("emailChange", 0, "processEmailChange", "Y"))
				.condition(new FormFieldValueCondition("unsubscribe", 1, "processUnsub", "Y"))

				.addField(buildField("email", false, false, "", new ValidateEmail("email", true, true, false, 5, 255)))
				.addField(buildField("emailNew", false, true, "", new ValidateEmail("emailNew", true, true, false, 5, 255)))

				.addField(buildField("cusCustomerIdHash", true, false, "", null))
				.addField(buildField("cusCustomerUUID", true, false, "", null))
				.addField(buildField("processEmailChange", true, false, "Y", null))
				.addField(buildField("processUnsub", true, false, "Y", null))

				.successNavigation(new ConditionalNavigation.Builder()
						.condition("emailChange", "change-email-confirm")
						.condition("unsubscribe", "unsubscribe-confirm")
						.defaultViewName("index")
						.build())
				.failureNavigation("index")
				.hiddenService(new HiddenService("adhoc", ServiceAction.REMOVE))
				.conditionalAction("emailChange", Arrays.asList(
						new CustomResourcesActionProcessor(
								Collections.singletonList(
										buildCustomResourceConfig("cusEcoa", Arrays.asList(
												buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
																.acsFieldName("customerId")
																.adobeDataType("string")
																.fieldName("email")
																.build()), // from profile/form field
												buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
																.acsFieldName("previousEmail")
																.adobeDataType("string")
																.fieldName("email")
																.build()),
												buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
																.acsFieldName("newEmail")
																.adobeDataType("string")
																.fieldName("emailNew")
																.build()),
												buildCustomResourceField(
														new CurrentDateTimeValueGenerator.Builder()
																.acsFieldName("requestTime").build()),
												buildCustomResourceField(
														new ConstantValueGenerator.Builder<String>()
																.acsFieldName("txEventId")
																.adobeDataType("string")
																.value("")
																.build()),
												buildCustomResourceField(
														new ConstantValueGenerator.Builder<String>()
																.acsFieldName("wkfStatus")
																.adobeDataType("string")
																.value("")
																.build())
										))
								)
						)
				))
				.conditionalAction("unsubscribe", Arrays.asList(
						new ServicesActionProcessor()
				))
				.build();



		/*
			cusEcoa
				customerId
				newEmail
				previousEmail
				requestTime
				txEventId
				wkfStatus
				title


		 */

		AppPage unsubConfirmation = AppPage.builder("unsubscribe-confirm", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();

		AppPage emailChangeConfirmation = AppPage.builder("change-email-confirm", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();




		AppPage error = AppPage.builder("error", PageType.ERROR)
				.controller(Controller.ERROR_PAGE)
				.build();


		return AppDetails.builder("dillards", "unsubadhoc")
				.enable()
				//.jwt("")
				.addPage(error)
				.addPage(unsubscribe)
				.addPage(unsubConfirmation)
				.addPage(emailChangeConfirmation)
				.addPage(lookup)
				.build();
	}
}
