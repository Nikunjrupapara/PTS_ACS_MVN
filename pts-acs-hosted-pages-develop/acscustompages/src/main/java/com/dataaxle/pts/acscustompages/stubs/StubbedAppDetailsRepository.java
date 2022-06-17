package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.exception.AppDetailsNotFoundException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.CustomResourceConfig;
import com.dataaxle.pts.acscustompages.model.navigation.ConditionalNavigation;
import com.dataaxle.pts.acscustompages.model.HiddenService;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.TriggeredEmailConfig;
import com.dataaxle.pts.acscustompages.model.conditions.AlwaysTrueCondition;
import com.dataaxle.pts.acscustompages.model.conditions.HasServiceCondition;
import com.dataaxle.pts.acscustompages.model.conditions.NotCondition;
import com.dataaxle.pts.acscustompages.model.conditions.ProfileFoundCondition;
import com.dataaxle.pts.acscustompages.model.navigation.SimpleNavigation;
import com.dataaxle.pts.acscustompages.model.validation.ValidateBoolean;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.validation.ValidateString;
import com.dataaxle.pts.acscustompages.model.valuegenerator.BooleanValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ConstantValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CurrentDateTimeValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.OffsetDateValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ProfileValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.CustomResourcesActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ProfileActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.TriggerEmailActionProcessor;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUSTOMER_ID;

public class StubbedAppDetailsRepository extends StubbedAppDetailBase {

	// Temporary set up of AppDetails data until we decide how to do it properly
	private static final Table<String, String, AppDetails> apps = HashBasedTable.create();

	private static final LocalDateTime now = LocalDateTime.now();

	protected static final String EMAIL = "email";
	protected static final String FIRST_NAME = "firstName";
	protected static final String LAST_NAME = "lastName";
	protected static final String LOC_COUNTRY_CODE = "location.countryCode";
	protected static final String LOC_STATE_CODE = "location.stateCode";
	protected static final String LOC_ZIP_CODE = "location.zipCode";

	protected static final String ADT_BOOLEAN = "boolean";
	protected static final String ADT_DATETIME = "datetime";
	protected static final String ADT_INT64 = "int64";
	protected static final String ADT_STRING = "string";

	static {
		addApp(localhost_preferences());
		addApp(resideo_buoy_signup());
		addApp(resideo_email_check());
		addApp(resideo_energy_unsub());
		addApp(resideo_preference_centre());
		addApp(resideo_stormM5_signup());
		addApp(StubbedAppDetailsRepositoryUsBank.usbank_preferences_fidelity());
		addApp(StubbedAppDetailsRepositoryUsBank.usbank_preferences_generic());
		addApp(ymnewsolutions_notEnabled());
		addApp(ymnewsolutions_notYetEffective());
		addApp(ymnewsolutions_noLongerEffective());
		addApp(ymnewsolutions_marketing_preferences());
		addApp(ymnewsolutions_marketing_signup());
		addApp(ymnewsolutions_retail_register());
		addApp(resideo_product_registration());
		addApp(StubbedAppDetailsRepositoryResideo.freeFilterRegistration());

		addApp(StubbedAppDetailsRepositoryCfna.unsubscribe());

		addApp(StubbedAppDetailRepositoryDollartree.signup());
		addApp(StubbedAppDetailRepositoryDollartree.preferences());
		addApp(StubbedAppDetailRepositoryDollartree.unsubscribe());
		addApp(StubbedAppDetailRepositoryDollartree.contest());
		addApp(StubbedAppDetailRepositoryDollartree.isignup());

		addApp(StubbedAppDetailRepositoryDillards.unsubscribe());
		addApp(StubbedAppDetailRepositoryDillards.unsubscribeadhoc());

		addApp(StubbedAppDetailsRepositoryKalmbachCategoryOptIn.discoveryCategoryOptIn());
		addApp(StubbedAppDetailsRepositoryKalmbachDiscoverBlogOptIn.discoverOptIn());
		addApp(StubbedAppDetailsRepositoryKalmbachPreference.preference());
		addApp(StubbedAppDetailsRepositoryKalmbachCategoryOptIn.scienceCategoryOptIn());

		addApp(StubbedAppDetailsRepositoryKalmbachBeginner.signupASY());
		addApp(StubbedAppDetailsRepositoryKalmbachBeginner.signupCTT());
		addApp(StubbedAppDetailsRepositoryKalmbachBeginner.signupFSM());
		addApp(StubbedAppDetailsRepositoryKalmbachBeginner.signupMRR());

		addApp(StubbedAppDetailsRepositoryKodakMoments.memberUnsub());

		addApp(StubbedAppDetailsRepositoryBoscovsCoupon.boscovs_coupon());
		addApp(StubbedAppDetailsRepositoryBoscovsFhf.fhfRegistration());
		addApp(StubbedAppDetailsRepositoryBoscovsFhf.fhfRegistered());
		addApp(StubbedAppDetailsRepositoryBoscovsUnsubscribe.unsubscribe());


		addApp(StubbedAppDetailsRepositoryMcrewards.unsubscribeBrazil());
		addApp(StubbedAppDetailsRepositoryMcrewards.unsubscribePoland());
	}

	public AppDetails getAppDetails(String domain, String contextPath) {
		if (apps.contains(domain, contextPath)) {
			return apps.get(domain, contextPath);
		}
		throw new AppDetailsNotFoundException(domain, contextPath);
	}

	public static Optional<AppDetails> findById(AppDetails.AppDetailsId id) {
		if (apps.contains(id.getCompany(), id.getContextPath())) {
			return Optional.of(apps.get(id.getCompany(), id.getContextPath()));
		}
		return Optional.empty();
	}

	private static void addApp(AppDetails appDetails) {
		apps.put(appDetails.getCompany(), appDetails.getContextPath(), appDetails);
	}

	private static AppDetails ymnewsolutions_marketing_preferences() {
		AppPage lookup = AppPage.builder("lookup", PageType.ENTRY_POINT)
							 .controller(Controller.GET_PROFILE_SECURELINK)
							 .successNavigation("index")
							 .failureNavigation("error")
							 .build();
		AppPage index = AppPage.builder("index", PageType.PAGE)
							.controller(Controller.UPDATE_PROFILE)
							.successNavigation("index")
							.failureNavigation("notfound")
							.fields(Arrays.asList(
								buildField("cusCustomerIdHash", true, false, "", null),
								buildField("cusCustomerUUID", true, false, "", null),
								buildField("email", false, true, "",
									new ValidateEmail("email", true, true, true, 5, 100)),
								buildField("firstName", false, true, "",
									new ValidateString("firstName", true, 2, 20, "[/p{L}-]{:min,:max}")),
								buildField("lastName", false, true, "",
									new ValidateString("lastName", true, 2, 20, "[/p{L}-]{:min,:max}"))
							))
							.serviceField(buildServiceField("cusRewardsActive", "Y", "N", "cusRewardsActive",
								"Y", "N", "yesMarketingAService"))
							.serviceProfileField(buildServiceField("cusPurchaseOrderFlag",
								"transactional", "", "cusPurchaseOrderFlag",
								true, false, "yesMarketingTransactionalA"))
							.serviceProfileField(buildServiceField("unsuball", "1", "0", "cusShortNumber",
								(short) 1, (short) 0, Collections.emptyList(), Arrays.asList("yesMarketingTransactionalA",
									"yesMarketingAService", "yesMarketingBService")))
							.defaultActions(Arrays.asList(
								new ProfileActionProcessor(List.of(
									new BooleanValueGenerator.Builder()
																.acsFieldName("cusPurchaseOrderFlag").actsUpon("formData")
																.adobeDataType("boolean").falseValue("").fieldName("cusPurchaseOrderFlag")
																.trueValue("transactional").build(),
									new FormFieldValueGenerator.Builder().acsFieldName("cusRewardsActive")
															.adobeDataType("string").fieldName("cusRewardsActive").build(),
									new FormFieldValueGenerator.Builder().acsFieldName("email").adobeDataType("string")
												 .fieldName("email").build(),
									new FormFieldValueGenerator.Builder().acsFieldName("firstName")
													 .adobeDataType("string").fieldName("firstName").build(),
									new FormFieldValueGenerator.Builder().acsFieldName("lastName")
													.adobeDataType("string").fieldName("lastName").build(),
									new FormFieldValueGenerator.Builder().acsFieldName("unsuball")
													.adobeDataType("short").fieldName("unsuball").build()
								)),
								new ServicesActionProcessor(),
								new TriggerEmailActionProcessor(
									TriggeredEmailConfig.builder()
										.eventIds(Map.of("ymnewsolutions", "TestEventId"))
										.email(
											new FormFieldValueGenerator.Builder()
												.acsFieldName("email")
												.adobeDataType("string")
												.fieldName("email")
												.build())
										.expiration(
											new OffsetDateValueGenerator.Builder()
												.startDate(new CurrentDateTimeValueGenerator.Builder().build())
												.hours(4L).minutes(30L)
												.build())
										.scheduled(new CurrentDateTimeValueGenerator.Builder().build())
										.contextField(new FormFieldValueGenerator.Builder()
																			.acsFieldName("email")
																			.adobeDataType("string")
																			.fieldName("email")
																			.build())
										.contextField(new FormFieldValueGenerator.Builder()
																				.acsFieldName("firstName")
																				.adobeDataType("string")
																				.fieldName("firstName")
																				.build())
										.build()
									)
								)
							)
							.missingInputView("error").build();
		AppPage notFound = AppPage.builder("notfound", PageType.PAGE).build();
		AppPage error = AppPage.builder("error", PageType.ERROR).build();
		return AppDetails.builder("ymnewsolutions", "marketing/preferences")
				   .effectiveFrom(now.minusDays(30L))
				   .effectiveTo(now.plusDays(30L))
				   .enable()
				   .jwt("jwt.string")
				   .pages(Arrays.asList(lookup, index, notFound, error))
				   .build();
	}

	private static AppDetails ymnewsolutions_marketing_signup() {
		AppPage index = AppPage.builder("index", PageType.ENTRY_POINT).controller(Controller.CREATE_PROFILE)
							.successNavigation("thankyou")
							.failureNavigation("error").forwardQueryParameters()
							.noRedirectOnError()
							.fields(Arrays.asList(
								buildField("email", false, true, "",
									new ValidateEmail("email", true, true, true, 5, 100)),
								buildField("confirmEmail", false, false, "", null),
								buildField("firstName", false, true, "",
									new ValidateString("firstName", true, 2, 20, "[\\p{L} -'\\.]{:min,:max}")),
								buildField("lastName", false, true, "",
									new ValidateString("lastName", true, 2, 20, "[\\p{L} -'\\.]{:min,:max}"))
							))
							// a hidden service
							.hiddenService(buildService("yesMarketingTransactionalA", ServiceAction.ADD))
							// service field linked to a profile field
							.serviceProfileField(buildServiceField("rewardsActive", "Y", "N",
								"cusRewardsActive", "Y", "N", "yesMarketingBService"))
							// standalone field which controls a service opt in
							.serviceField(buildServiceField("marketingOptIn", "yesMarketingAService", "marketing",
								""))
							.defaultActions(Arrays.asList(
								new ProfileActionProcessor(List.of(
									new FormFieldValueGenerator.Builder()
										.acsFieldName("email")
										.adobeDataType("string")
										.fieldName("email")
										.build(),
									new FormFieldValueGenerator.Builder()
										.acsFieldName("firstName")
										.adobeDataType("string")
										.fieldName("firstName")
										.build(),
									new FormFieldValueGenerator.Builder()
										.acsFieldName("lastName")
										.adobeDataType("string")
										.fieldName("lastName")
										.build(),
									new FormFieldValueGenerator.Builder()
										.acsFieldName("cusCustomerId")
										.adobeDataType("string")
										.fieldName("email")
										.build()
								)),
								new ServicesActionProcessor()
							))
							.missingInputView("error")
							.build();
		AppPage thankyou = AppPage.builder("thankyou", PageType.PAGE)
							   .controller(Controller.PROFILE_CONFIRM)
							   .noRedirectOnError()
							   .fields(Arrays.asList(
								   buildField("email", false, false, "", null),
								   buildField("firstName", false, false, "", null),
								   buildField("lastName", false, false, "", null)
							   ))
							   .build();
		AppPage error = AppPage.builder("error", PageType.ERROR)
							.controller(Controller.ERROR_PAGE)
							.build();
		return AppDetails.builder("ymnewsolutions", "marketing/signup")
			.effectiveFrom(now.minusDays(30L)).effectiveTo(now.plusDays(30L)).enable()
			.jwt("")
			.pages(Arrays.asList(index, thankyou, error)).build();
	}

	private static AppDetails ymnewsolutions_retail_register() {
		AppPage success = AppPage.builder("thankyou", PageType.PAGE)
							  .build();
		AppPage index = AppPage.builder("index", PageType.ENTRY_POINT)
							.controller(Controller.CREATE_PROFILE)
							.successNavigation("success")
							.fields(Arrays.asList(
								buildField("email", false, true, "",
									new ValidateEmail("email", true, true, true, 5, 100)),
								buildField("firstName", false, true, "",
									new ValidateString("firstName", true, 2, 20, "[\\p{L} -'\\.]{:min,:max}"))
							)).build();
		AppPage emailLookup = AppPage.builder("emailLookup", PageType.PAGE)
								  .controller(Controller.GET_PROFILE_CUSTOMERID)
								  .successNavigation("thankyou")
								  .addField(buildField("email", false, true, "",
									  new ValidateEmail("email", true, true, false, 5, 50)))
								  .build();
		return AppDetails.builder("ymnewsolutions", "register/retail")
			.effectiveFrom(now.minusDays(30L)).effectiveTo(now.plusDays(30L)).enable()
			.jwt("JWT.token.data").pages(Arrays.asList(index, success, emailLookup)).build();
	}

	private static AppDetails resideo_preference_centre() {
		/*
		TODO: this app has no error page so a server error will result in redirection to a generic error page.
		Need to decide how to handle this more gracefully, probably by defining a branded error page
		*/
		AppPage lookup = AppPage.builder("lookup", PageType.ENTRY_POINT)
							.controller(Controller.GET_PROFILE_SECURELINK)
							.successNavigation("preferences")
							.failureNavigation("preferences")
							 .forwardQueryParameters()
							 .forwardParameter("deliveryId")
							 .build();
		AppPage preferences = AppPage.builder("preferences", PageType.PROFILE)
								  .controller(Controller.UPDATE_SERVICES)
								  .successNavigation("preferences").failureNavigation("preferences")
									.forwardQueryParameters()
									.forwardParameter("deliveryId")
								  .fields(Arrays.asList(
									  buildField("cusCustomerId", true, false, "", null),
									  buildField("cusCustomerIdHash", true, false, "", null),
									  buildField("cusCustomerUUID", true, false, "", null),
									  buildField("email", true, false, "null", null),
									  buildField("source", true, false, "", null),
									  buildField("deliveryId", true, false, "", null)
								  ))
								  .serviceField(buildServiceField("emails", "honeywellhomeemails", "emails", ""))
								  .serviceField(buildServiceField("offers", "honeywellhomepresentedoffers", "offers", ""))
								  .serviceField(buildServiceField("asks", "honeywellhomeasks", "asks", ""))
								  .serviceField(buildServiceField("support", "connectedhome", "support", ""))
								  .serviceField(buildServiceField("onlyenergy", "onlyenergy", "",
									  Collections.singletonList("honeywellhomeenergyreport"), Arrays.asList("honeywellhomeemails",
										  "honeywellhomepresentedoffers", "honeywellhomeasks", "connectedhome")))
								  .serviceField(buildServiceField("unsuball", "unsuball", "", Collections.emptyList(),
									  Arrays.asList("honeywellhomeemails", "honeywellhomepresentedoffers", "honeywellhomeasks",
										  "connectedhome", "honeywellhomeenergyreport")))
								  .defaultActions(Arrays.asList(
								  		new ServicesActionProcessor(),
										new CustomResourcesActionProcessor(
											Collections.singletonList(
												buildCustomResourceConfig("cusHosted_pages_logs", Arrays.asList(
													buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
															.acsFieldName("customerId")
															.adobeDataType("string")
															.fieldName("cusCustomerId")
															.build()), // from profile/form field
													buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
															.acsFieldName("email")
															.adobeDataType("string")
															.fieldName("email")
															.build()),
													buildCustomResourceField(
														new BooleanValueGenerator.Builder()
															.acsFieldName("optInAsks").actsUpon("formData")
															.adobeDataType("boolean").falseValue("").fieldName("asks")
															.trueValue("asks").build()),
													buildCustomResourceField(
														new BooleanValueGenerator.Builder().actsUpon("formData")
															.acsFieldName("optInEmails").adobeDataType("boolean")
															.falseValue("").fieldName("emails").trueValue("emails").build()),
													buildCustomResourceField(
														new BooleanValueGenerator.Builder().acsFieldName("optInEnergy")
															.actsUpon("formData").adobeDataType("boolean").falseValue("")
															.fieldName("onlyenergy").trueValue("onlyenergy").build()),
													buildCustomResourceField(
														new BooleanValueGenerator.Builder().acsFieldName("optInOffers")
															.actsUpon("formData").adobeDataType("boolean").falseValue("")
															.fieldName("offers").trueValue("offers").build()),
													buildCustomResourceField(
														new BooleanValueGenerator.Builder().acsFieldName("optInSupport")
															.actsUpon("formData").adobeDataType("boolean").falseValue("")
															.fieldName("support").trueValue("support").build()),
													buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
															.acsFieldName("source").adobeDataType("string")
															.fieldName("source").build()),
													buildCustomResourceField(
														new BooleanValueGenerator.Builder().acsFieldName("unsubAll")
															.actsUpon("formData").adobeDataType("boolean").falseValue("")
															.fieldName("unsuball").trueValue("unsuball").build()),
													buildCustomResourceField(
														new FormFieldValueGenerator.Builder()
															.acsFieldName("deliveryId")
															.adobeDataType("string")
															.fieldName("deliveryId")
															.build()
													)
												))
											)
										)
									  )
								  )
								  .missingInputView("preferences").build();
		return AppDetails.builder("resideo", "preferences")
				   .enable()
				   .jwt("")
				   .pages(Arrays.asList(lookup, preferences))
				   .build();
	}


	private static AppDetails resideo_stormM5_signup(){

		TriggeredEmailConfig emailConfig = new TriggeredEmailConfig.Builder()
				.eventIds(
						Map.of("honeywellhome", "EVTstormSignupConfirmation",
								"resideo", "EVTstormSignupConfirmation"
						))
				.email(
						new FormFieldValueGenerator.Builder()
								.acsFieldName("email")
								.adobeDataType("string")
								.fieldName("email").build())
				.contextField(
						new ConstantValueGenerator.Builder<String>()
							.acsFieldName("Source")
							.adobeDataType("string")
							.value("stormM5EmailSignup").build())
				.contextField(
						new FormFieldValueGenerator.Builder()
								.acsFieldName("customerIdHash")
								.adobeDataType("string")
								.fieldName("cusCustomerIdHash")
								.build()
				)
				.build();

		AppPage signup = AppPage.builder("signup", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_CUSTOMERID)
				.condition(new NotCondition(new ProfileFoundCondition("profileExists", 0)))
				.condition(new HasServiceCondition("subscribedToHHE", 2, "honeywellhomeemails"))
				.condition(new NotCondition(new HasServiceCondition("subscribedToHHE", 3, "honeywellhomeemails")))
				.successNavigation(new ConditionalNavigation.Builder()
						.condition("subscribedToHHE", "subscribed")
						.condition("Not subscribedToHHE", "unsubscribed")
						.defaultViewName("almostDone")
						.build())
				.failureNavigation("almostDone")
				.fields(Arrays.asList(
						buildField("email", false, true, "",
								new ValidateEmail("email", true, true, false, null, null)),
						buildField("firstName", false, false, "",
								new ValidateString("firstName", true, 2, 30, "[\\p{Alnum}\\p{Punct}]{:min,:max}")),
						buildField("lastName", false, false, "",
								new ValidateString("lastName", true, 2, 30, "[\\p{Alnum}\\p{Punct}]{:min,:max}")),
						buildField("usCitizen", false, false, false,
								new ValidateBoolean("usCitizen", true))
				))
				.conditionalAction(
						"Not profileExists", Arrays.asList(
								new ProfileActionProcessor(
										List.of(
												new FormFieldValueGenerator.Builder().acsFieldName("email")
														.adobeDataType("string").fieldName("email").build(),
												new FormFieldValueGenerator.Builder().acsFieldName("firstName")
														.adobeDataType("string").fieldName("firstName").build(),
												new FormFieldValueGenerator.Builder().acsFieldName("lastName")
														.adobeDataType("string").fieldName("lastName").build()
										)
								),
								new TriggerEmailActionProcessor(emailConfig)
						)
				)
				.conditionalAction("subscribedToHHE", Collections.singletonList(
						new ProfileActionProcessor(
								List.of(new ConstantValueGenerator.Builder<String>()
											.acsFieldName("cusStormM5Page")
											.adobeDataType("string")
											.value("Y")
											.build())
						))
				)
				.conditionalAction("Not subscribedToHHE", Collections.emptyList())
				.build();
		AppPage unsubscribed = AppPage.builder("unsubscribed", PageType.PAGE)
				.controller(Controller.TRIGGER_EMAIL)
				.condition(new AlwaysTrueCondition("default", 0))
				.successNavigation("almostDone")
				.failureNavigation(new SimpleNavigation.Builder().viewName("unsubscribed").disableRedirect().build())
				.addField(buildField("email", false, false, "", null))
				.addField(buildField("cusCustomerIdHash", true, true, "", null))
				.addField(buildField("firstName", true, false, "", null))
				.addField(buildField("lastName", true, false, "", null))
				.conditionalAction("default", Collections.singletonList(
						new TriggerEmailActionProcessor(emailConfig)
				))
				.build();
		AppPage almostDone = AppPage.builder("almostDone", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();
		AppPage subscribed = AppPage.builder("subscribed", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();
		AppPage confirmation = AppPage.builder("confirmation", PageType.PAGE)
				.controller(Controller.NONE)
				.successNavigation(new SimpleNavigation.Builder()
						.viewName("confirmation")
						.disableRedirect()
						.build())
				.failureNavigation("error")
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.hiddenService(buildService("honeywellhomeemails", ServiceAction.ADD))
				.hiddenService(buildService("honeywellhomeasks", ServiceAction.ADD))
				.hiddenService(buildService("honeywellhomepresentedoffers", ServiceAction.ADD))
				.condition(new AlwaysTrueCondition("default", 0))
				.defaultActions(Arrays.asList(
						new ProfileActionProcessor(
							List.of(new ConstantValueGenerator.Builder<String>()
										.acsFieldName("cusStormM5Page")
										.adobeDataType("string")
										.value("Y")
										.build())
						),
						new ServicesActionProcessor(),
						new CustomResourcesActionProcessor(Collections.singletonList(
								buildCustomResourceConfig("cusHosted_pages_logs", Arrays.asList(
										buildCustomResourceField(new ProfileValueGenerator.Builder().acsFieldName("customerId")
												.adobeDataType("string").profileFieldName("cusCustomerId").build()), // from profile/form field
										buildCustomResourceField(new ProfileValueGenerator.Builder().acsFieldName("email")
												.adobeDataType("string").profileFieldName("email").build()),
										buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																	 .acsFieldName("optInAsks")
																	 .adobeDataType("boolean")
																	 .value(true)
																	 .build()),
										buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																	 .acsFieldName("optInEmails")
																	 .adobeDataType("boolean")
																	 .value(true)
																	 .build()),
										buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																	 .acsFieldName("optInOffers")
																	 .adobeDataType("boolean")
																	 .value(true)
																	 .build()),
										buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																	 .acsFieldName("optInEnergy")
																	 .adobeDataType("boolean")
																	 .value(false)
																	 .build()),
										buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																	 .acsFieldName("optInSupport")
																	 .adobeDataType("boolean")
																	 .value(false)
																	 .build()),
										buildCustomResourceField(new ConstantValueGenerator.Builder<String>()
																	 .acsFieldName("source")
																	 .adobeDataType("string")
																	 .value("stormM5EmailSignup")
																	 .build()),
										buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																	 .acsFieldName("unsubAll")
																	 .adobeDataType("boolean")
																	 .value(false)
																	 .build())
								))
						)
						)
						)
				)
				.build();
		return AppDetails.builder("resideo", "stormM5")
	//			.enable()
				.jwt("")
				.addPage(signup)
				.addPage(unsubscribed)
				.addPage(subscribed)
				.addPage(almostDone)
				.addPage(confirmation)
				.build();
	}

	private static AppDetails resideo_product_registration(){
		CustomResourcesActionProcessor cusHosted_pages_logs = new CustomResourcesActionProcessor(
				List.of(
						buildCustomResourceConfig("cusHosted_pages_logs", Arrays.asList(
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("customerId")
												.adobeDataType("string")
												.fieldName("email")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("productName")
												.adobeDataType("string")
												.fieldName("product")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("modelNumber")
												.adobeDataType("string")
												.fieldName("productId")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("email")
												.adobeDataType("string")
												.fieldName("email")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("source")
												.adobeDataType("string")
												.fieldName("source")
												.build()),
								buildCustomResourceField(
										new ConstantValueGenerator.Builder<String>()
												.acsFieldName("exportStatus")
												.adobeDataType("string")
												.value("N").build()),
//								buildCustomResourceField(
//										new CurrentDateTimeValueGenerator.Builder()
//												.acsFieldName("exported").build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("zip")
												.adobeDataType("string")
												.fieldName("zip")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("mobile")
												.adobeDataType("string")
												.fieldName("phone")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("firstName")
												.adobeDataType("string")
												.fieldName("firstName")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("lastName")
												.adobeDataType("string")
												.fieldName("lastName")
												.build()),

								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("country")
												.adobeDataType("string")
												.fieldName("country")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("city")
												.adobeDataType("string")
												.fieldName("city")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("address")
												.adobeDataType("string")
												.fieldName("address")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("state")
												.adobeDataType("string")
												.fieldName("state")
												.build()),
								buildCustomResourceField(
										new FormFieldValueGenerator.Builder()
												.acsFieldName("smsFlag")
												.adobeDataType("string")
												.fieldName("sms")
												.build())
						))));

		Map<String, String> eventIds = Map.of(
			"honeywellhome", "EVTproductRegistrationConfirmationHoneywell",
			"resideo", "EVTproductRegistrationConfirmationResideo"
		);

		TriggeredEmailConfig emailConfigExisting = new TriggeredEmailConfig.Builder()
				.eventIds(eventIds)
				.email(
						new FormFieldValueGenerator.Builder()
								.acsFieldName("email")
								.adobeDataType("string")
								.fieldName("email").build())
//				.contextField("Source",
//						new ConstantValueGenerator.Builder<String>()
//								.acsFieldName("Source")
//								.adobeDataType("string")
//								.value("prodReg").build())
				.contextField(
						new ProfileValueGenerator.Builder()
								.acsFieldName("customerIdHash")
								.adobeDataType("string")
								.profileFieldName("cusCustomerIdHash")
								.build()
				)
				.build();

		TriggeredEmailConfig emailConfigNew = new TriggeredEmailConfig.Builder()
				.eventIds(eventIds)
				.email(
						new FormFieldValueGenerator.Builder()
								.acsFieldName("email")
								.adobeDataType("string")
								.fieldName("email").build())
//				.contextField("Source",
//						new ConstantValueGenerator.Builder<String>()
//								.acsFieldName("Source")
//								.adobeDataType("string")
//								.value("prodReg").build())
				.contextField(
						new ConstantValueGenerator.Builder<String>()
								.acsFieldName("customerIdHash")
								.adobeDataType("string")
								.value("").build()
				)
				.build();

		AppPage confirmation = AppPage.builder("confirmation", PageType.PAGE)
				.controller(Controller.CONFIRMATION_PAGE)
				.build();
		AppPage error = AppPage.builder("error", PageType.ERROR)
				.controller(Controller.ERROR_PAGE)
				.build();
		AppPage index = AppPage.builder("index", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_CUSTOMERID)
				.condition(new NotCondition(new ProfileFoundCondition("profileExists", 0)))
				.condition(new ProfileFoundCondition("profileExists", 1))

//				.successNavigation(new SimpleNavigation.Builder()
//										.viewName("confirmation")
//										.disableRedirect().build())   // issue with CONFIRMATION_PAGE  //

				.successNavigation("confirmation")
				.failureNavigation("confirmation") // if profile is not found error
					.errorNavigation("error") // error page
				.fields(Arrays.asList(
						buildField("email", false, true, "",
								new ValidateEmail("email", true, true, false, 5, 255)),
						buildField("firstName", false, false, "",
								new ValidateString("firstName", true, 1, 50, "[\\p{L} -'\\.]{:min,:max}")),
						buildField("lastName", false, false, "",
								new ValidateString("lastName", true, 1, 50, "[\\p{L} -'\\.]{:min,:max}")),
						buildField("country", false, false, "",
								new ValidateString("country", true, 2, 2, "[\\p{L} -'\\.]{:min,:max}")),
						buildField("address", false, false, "",
								new ValidateString("address", false, 0, 255, "[\\w ]{:min,:max}")),
						buildField("city", false, false, "",
								new ValidateString("city", false, 0, 150, "[\\p{L} -'\\.]{:min,:max}")),
						buildField("state", false, false, "",
								new ValidateString("state", true, 2, 2, "[\\p{L} -'\\.]{:min,:max}")),
						buildField("zip", false, false, "",null),
								//new ValidateString("zip", false, 0, 10, "[\\p{N} -]{:min,:max}")),
						buildField("phone", false, false, "",
								null),
						buildField("sms", false, false, "", null),
						buildField("parentCat", false, false, "",
								null),
						buildField("product", false, false, "",
								null),
						buildField("productId", false, false, "",
								null),
						buildField("source", false, false, "",
								null),
						buildField("resident", false, false, false,
								new ValidateBoolean("resident", true))
				))

				.hiddenService(buildService("honeywellhomepresentedoffers", ServiceAction.ADD))
				.hiddenService(buildService("honeywellhomeemails", ServiceAction.ADD))
				.hiddenService(buildService("honeywellhomeasks", ServiceAction.ADD))

				.conditionalAction("Not profileExists", Arrays.asList(
								new ProfileActionProcessor(
										List.of(
												new FormFieldValueGenerator.Builder().acsFieldName("email")
														.adobeDataType("string").fieldName("email").build(),
												new FormFieldValueGenerator.Builder().acsFieldName("firstName")
														.adobeDataType("string").fieldName("firstName").build(),
												new FormFieldValueGenerator.Builder().acsFieldName("lastName")
														.adobeDataType("string").fieldName("lastName").build(),
												new FormFieldValueGenerator.Builder().acsFieldName("location.countryCode")
													.adobeDataType("string").fieldName("country").build(),
												new FormFieldValueGenerator.Builder().acsFieldName("location.address1")
													.adobeDataType("string").fieldName("address").build(),
												new FormFieldValueGenerator.Builder().acsFieldName("location.city")
													.adobeDataType("string").fieldName("city").build(),
												new FormFieldValueGenerator.Builder().acsFieldName("location.stateCode")
													.adobeDataType("string").fieldName("state").build(),
												new FormFieldValueGenerator.Builder().acsFieldName("location.zipCode")
													.adobeDataType("string").fieldName("zip").build(),
												new FormFieldValueGenerator.Builder()
														.acsFieldName("cusSource").adobeDataType("string")
														.fieldName("source").build(),
												new FormFieldValueGenerator.Builder()
														.acsFieldName("cusMobileNumber").adobeDataType("string")
														.fieldName("phone").build(),
												new FormFieldValueGenerator.Builder()
														.acsFieldName("cusSmsFlag").adobeDataType("string")
														.fieldName("sms").build()
										)
								),
								cusHosted_pages_logs,
								new TriggerEmailActionProcessor(emailConfigNew),
								new ServicesActionProcessor()
						)
				)

				.conditionalAction("profileExists", Arrays.asList(
						cusHosted_pages_logs,
						new TriggerEmailActionProcessor(emailConfigExisting),
						new ServicesActionProcessor()
					)
				)

				.build();

		return AppDetails.builder("resideo", "productRegistration")
				.enable()
				.jwt("")
				.addPage(error)
				.addPage(index)
				.addPage(confirmation)
				.build();
	}

	private static AppDetails resideo_buoy_signup() {
		TriggeredEmailConfig emailConfig = new TriggeredEmailConfig.Builder()
										  .eventIds(
											  Map.of("honeywellhome", "EVTcbBuoySignupConfirmation",
												  "resideo", "EVTcbBuoySignupConfirmation"
											  ))
										  .email(
											  new FormFieldValueGenerator.Builder()
												  .acsFieldName("email")
												  .adobeDataType("string")
												  .fieldName("email").build())
										  .contextField("Source",
											  new ConstantValueGenerator.Builder<String>()
												  .acsFieldName("Source")
												  .adobeDataType("string")
												  .value("buoyEmailSignup").build())
										  .contextField(
											  new FormFieldValueGenerator.Builder()
												  .acsFieldName("customerIdHash")
												  .adobeDataType("string")
												  .fieldName("cusCustomerIdHash")
												  .build()
										  )
										  .build();

		AppPage signup = AppPage.builder("signup", PageType.ENTRY_POINT)
							 .controller(Controller.GET_PROFILE_CUSTOMERID)
							 .condition(new NotCondition(new ProfileFoundCondition("profileExists", 0)))
							 .condition(new HasServiceCondition("subscribedToHHE", 2, "honeywellhomeemails"))
							 .condition(new NotCondition(new HasServiceCondition("subscribedToHHE", 3, "honeywellhomeemails")))
							 .successNavigation(new ConditionalNavigation.Builder()
													.condition("subscribedToHHE", "subscribed")
													.condition("Not subscribedToHHE", "unsubscribed")
													.defaultViewName("almostDone")
													.build())
							 .failureNavigation("almostDone")
							 .fields(Arrays.asList(
								 buildField("email", false, true, "",
									 new ValidateEmail("email", true, true, false, null, null)),
								 buildField("firstName", false, false, "",
									 new ValidateString("firstName", true, 2, 30, "[\\p{Alnum}\\p{Punct}]{:min,:max}")),
								 buildField("lastName", false, false, "",
									 new ValidateString("lastName", true, 2, 30, "[\\p{Alnum}\\p{Punct}]{:min,:max}")),
								 buildField("usCitizen", false, false, false,
									 new ValidateBoolean("usCitizen", true))
							 ))
							 .conditionalAction(
								 "Not profileExists", Arrays.asList(
									 new ProfileActionProcessor(
									 	List.of(
										 new FormFieldValueGenerator.Builder().acsFieldName("email")
													  .adobeDataType("string").fieldName("email").build(),
										 new FormFieldValueGenerator.Builder().acsFieldName("firstName")
														  .adobeDataType("string").fieldName("firstName").build(),
										 new FormFieldValueGenerator.Builder().acsFieldName("lastName")
														 .adobeDataType("string").fieldName("lastName").build()
										)
									 ),
									 new TriggerEmailActionProcessor(emailConfig)
								 )
							 )
							 .conditionalAction("subscribedToHHE", Collections.singletonList(
								 new ProfileActionProcessor(
									 Collections.singletonList(new ConstantValueGenerator.Builder<String>()
																   .acsFieldName("cusBuoyPage")
																   .adobeDataType("string")
																   .value("Y")
																   .build())
								 ))
							 )
							 .conditionalAction("Not subscribedToHHE", Collections.emptyList())
							 .build();
		AppPage unsubscribed = AppPage.builder("unsubscribed", PageType.PAGE)
								   .controller(Controller.TRIGGER_EMAIL)
								   .condition(new AlwaysTrueCondition("default", 0))
								   .successNavigation("almostDone")
								   .failureNavigation(new SimpleNavigation.Builder().viewName("unsubscribed").disableRedirect().build())
								   .addField(buildField("email", false, false, "", null))
								   .addField(buildField("cusCustomerIdHash", true, false, "", null))
								   .addField(buildField("firstName", true, false, "", null))
								   .addField(buildField("lastName", true, false, "", null))
								   .conditionalAction("default", Collections.singletonList(
										new TriggerEmailActionProcessor(emailConfig)
								   ))
								   .build();
		AppPage almostDone = AppPage.builder("almostDone", PageType.PAGE)
								 .controller(Controller.PROFILE_CONFIRM)
								 .fields(Collections.singletonList(
									 buildField("email", true, false, "", null)
								 ))
								 .build();
		AppPage subscribed = AppPage.builder("subscribed", PageType.PAGE)
								 .controller(Controller.PROFILE_CONFIRM)
								 .fields(Collections.singletonList(
								 	buildField("email", true, false, "", null)
								 ))
								 .build();
		AppPage confirmation = AppPage.builder("confirmation", PageType.PAGE)
								.controller(Controller.NONE)
								.successNavigation(new SimpleNavigation.Builder()
													   .viewName("confirmation")
													   .disableRedirect()
													   .build())
								.failureNavigation("error")
								.fields(Collections.singletonList(
									buildField("email", true, false, "", null)
								))
								.hiddenService(buildService("honeywellhomeemails", ServiceAction.ADD))
								.hiddenService(buildService("honeywellhomeasks", ServiceAction.ADD))
								.hiddenService(buildService("honeywellhomepresentedoffers", ServiceAction.ADD))
								.condition(new AlwaysTrueCondition("default", 0))
								.defaultActions(Arrays.asList(
											new ProfileActionProcessor(
												Collections.singletonList(new ConstantValueGenerator.Builder<String>()
																			  .acsFieldName("cusBuoyPage")
																			  .adobeDataType("string")
																			  .value("Y")
																			  .build())
											),
											new ServicesActionProcessor(),
											new CustomResourcesActionProcessor(Collections.singletonList(
												buildCustomResourceConfig("cusHosted_pages_logs", Arrays.asList(
													buildCustomResourceField(new ProfileValueGenerator.Builder()
																				 .acsFieldName("customerId")
																				 .adobeDataType("string")
																				 .profileFieldName("cusCustomerId")
																				 .build()), // from profile/form field
													buildCustomResourceField(new ProfileValueGenerator.Builder().acsFieldName("email")
																				 .adobeDataType("string")
																				 .profileFieldName("email")
																				 .build()),
													buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																				 .acsFieldName("optInAsks")
																				 .adobeDataType("boolean")
																				 .value(true)
																				 .build()),
													buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																				 .acsFieldName("optInEmails")
																				 .adobeDataType("boolean")
																				 .value(true)
																				 .build()),
													buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																				 .acsFieldName("optInOffers")
																				 .adobeDataType("boolean")
																				 .value(true)
																				 .build()),
													buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																				 .acsFieldName("optInEnergy")
																				 .adobeDataType("boolean")
																				 .value(false)
																				 .build()),
													buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																				 .acsFieldName("optInSupport")
																				 .adobeDataType("boolean")
																				 .value(false)
																				 .build()),
													buildCustomResourceField(new ConstantValueGenerator.Builder<String>()
																				 .acsFieldName("source")
																				 .adobeDataType("string")
																				 .value("buoyEmailSignup")
																				 .build()),
													buildCustomResourceField(new ConstantValueGenerator.Builder<Boolean>()
																				 .acsFieldName("unsubAll")
																				 .adobeDataType("boolean")
																				 .value(false)
																				 .build())
												))
											)
										)
									)
								)
								.build();
		return AppDetails.builder("resideo", "buoySignup")
				   //.enable()
				   .jwt("")
				   .addPage(signup)
				   .addPage(unsubscribed)
				   .addPage(subscribed)
				   .addPage(almostDone)
				   .addPage(confirmation)
				   .build();
	}

	private static AppDetails resideo_email_check() {
		AppPage index = AppPage.builder("index", PageType.ENTRY_POINT)
							.controller(Controller.GET_PROFILE_CUSTOMERID)
							.condition(new ProfileFoundCondition("profileExists", 0))
							.condition(new NotCondition(new ProfileFoundCondition("profileExists", 1)))
							.conditionalAction("profileExists",
								Collections.singletonList(
									new TriggerEmailActionProcessor(
										new TriggeredEmailConfig.Builder()
											.eventIds(Map.of(
												"honeywellhome", "EVTpreferenceCenterLinkHoneywell",
												"resideo", "EVTpreferenceCenterLinkResideo"
												)
											)
											.email(
												new FormFieldValueGenerator.Builder()
													.acsFieldName("email")
													.adobeDataType("string")
													.fieldName("email")
													.build()
											)
											.contextField(
												new ProfileValueGenerator.Builder()
													.acsFieldName("customerIdHash")
													.adobeDataType("string")
													.profileFieldName("cusCustomerIdHash")
													.build()
											)
											.build()
									)
								)
							)
							.conditionalAction("Not profileExists", Collections.emptyList())
							.successNavigation("confirmation")
							.failureNavigation("confirmation")
							.fields(Arrays.asList(
								buildField("brand", true, false, "", null),
								buildField("email", false, true, "",
									new ValidateEmail("email", true, true, false, null, null))
							))
							.build();
		AppPage confirmation = AppPage.builder("confirmation", PageType.PAGE)
								   .controller(Controller.PROFILE_CONFIRM)
								   .addField(buildField("email", false, true, "", null))
								   .missingInputView("index")
								   .build();
		return AppDetails.builder("resideo", "emailCheck")
				   .enable()
				   .jwt("")
				   .addPage(index)
				   .addPage(confirmation)
				   .build();
	}

	public static AppDetails resideo_energy_unsub() {
		final String HOSTED_PAGES_LOGS_RESOURCE_NM = "cusHosted_pages_logs";

		final ValueGenerator<?> SOURCE_UNSUB = new ConstantValueGenerator.Builder<>()
				.acsFieldName("source")
				.adobeDataType(ADT_STRING)
				.value("energyReportsUnsub")
				.build();
		final ValueGenerator<?> SOURCE_RESUB = new ConstantValueGenerator.Builder<>()
				.acsFieldName("source")
				.adobeDataType(ADT_STRING)
				.value("energyReportsResub")
				.build();
		final ValueGenerator<?> VG_CUSTOMER_ID = new FormFieldValueGenerator.Builder()
				.acsFieldName(CUSTOMER_ID)
				.adobeDataType(ADT_STRING)
				.fieldName(EMAIL)
				.build();
		final ValueGenerator<?> VG_EMAIL = new FormFieldValueGenerator.Builder()
				.acsFieldName(EMAIL)
				.adobeDataType(ADT_STRING)
				.fieldName(EMAIL)
				.build();

		final CustomResourceConfig HOSTED_PAGES_LOGS_UNSUB_CONFIG = buildCustomResourceConfig(HOSTED_PAGES_LOGS_RESOURCE_NM,
				List.of(
						buildCustomResourceField(VG_EMAIL),
						buildCustomResourceField(VG_CUSTOMER_ID),
						buildCustomResourceField(SOURCE_UNSUB)
				));
		final CustomResourceConfig HOSTED_PAGES_LOGS_RESUB_CONFIG = buildCustomResourceConfig(HOSTED_PAGES_LOGS_RESOURCE_NM,
				List.of(
						buildCustomResourceField(VG_EMAIL),
						buildCustomResourceField(VG_CUSTOMER_ID),
						buildCustomResourceField(SOURCE_RESUB)
				));


		AppPage lookup = AppPage.builder("lookup", PageType.ENTRY_POINT)
							.controller(Controller.GET_PROFILE_SECURELINK)
							.successNavigation("unsubscribe")
							.failureNavigation("unsubscribe")
							.build();
		AppPage unsubscribe = AppPage.builder("unsubscribe", PageType.PAGE)
								  .controller(Controller.UPDATE_SERVICES)
								  .successNavigation("resubscribe")
								  .failureNavigation("subscribe")
								  .addField(buildField("email", true, false, "", null))
								  .addField(buildField("cusCustomerIdHash", true, false, "", null))
								  .addField(buildField("cusCustomerUUID", true, false, "", null))
								  .hiddenService(new HiddenService("honeywellhomeenergyreport", ServiceAction.REMOVE))
								  .defaultActions(List.of(new ServicesActionProcessor(),new CustomResourcesActionProcessor(List.of(HOSTED_PAGES_LOGS_UNSUB_CONFIG))))
								  .missingInputView("unsubscribe")
								  .build();
		AppPage resubscribe = AppPage.builder("resubscribe", PageType.PAGE)
								  .controller(Controller.UPDATE_SERVICES)
								  .successNavigation("confirmation")
								  .failureNavigation("resubscribe")
								  .addField(buildField("email", true, false, "", null))
								  .addField(buildField("cusCustomerIdHash", true, false, "", null))
								  .addField(buildField("cusCustomerUUID", true, false, "", null))
								  .hiddenService(new HiddenService("honeywellhomeenergyreport", ServiceAction.ADD))
				                  .defaultActions(List.of(new ServicesActionProcessor(),new CustomResourcesActionProcessor(List.of(HOSTED_PAGES_LOGS_RESUB_CONFIG))))
								  .missingInputView("resubscribe")
								  .build();
		AppPage confirmation = AppPage.builder("confirmation", PageType.PAGE)
								   .controller(Controller.PROFILE_CONFIRM)
								   .addField(buildField("cusCustomerIdHash", true, false, "", null))
								   .addField(buildField("cusCustomerUUID", true, false, "", null))
								   .missingInputView("lookup")
								   .build();
		return AppDetails.builder("resideo", "energyReport")
				   .addPage(lookup)
				   .addPage(unsubscribe)
				   .addPage(resubscribe)
				   .addPage(confirmation)
				   .enable()
				   .jwt("")
				   .build();
	}

	private static AppDetails ymnewsolutions_notEnabled() {
		AppPage error = AppPage.builder("error", PageType.ERROR).build();
		AppPage index = AppPage.builder("index", PageType.ENTRY_POINT).controller(Controller.GET_PROFILE_CUSTOMERID)
							.failureNavigation("error").build();
		return AppDetails.builder("ymnewsolutions", "authtest/notenabled")
			.effectiveFrom(now.minusMonths(1L)).effectiveTo(now.plusMonths(1L)).jwt("jwt")
				   .pages(Arrays.asList(error, index)).build();
	}

	private static AppDetails ymnewsolutions_notYetEffective() {
		AppPage error = AppPage.builder("error", PageType.ERROR).build();
		AppPage index = AppPage.builder("index", PageType.ENTRY_POINT).failureNavigation("error").build();
		return AppDetails.builder("ymnewsolutions", "authtest/noteffective")
			.effectiveFrom(now.plusDays(1L)).effectiveTo(now.plusMonths(1L)).enable().jwt("jwt")
				   .pages(Arrays.asList(error, index)).build();
	}

	private static AppDetails ymnewsolutions_noLongerEffective() {
		AppPage error = AppPage.builder("error", PageType.ERROR).build();
		AppPage index = AppPage.builder("index", PageType.ENTRY_POINT).failureNavigation("error").build();
		return AppDetails.builder("ymnewsolutions", "authtest/expired")
			.effectiveFrom(now.minusDays(30L)).effectiveTo(now.minusDays(1L)).enable().jwt("jwt")
			.pages(Arrays.asList(error, index)).build();
	}

	private static AppDetails localhost_preferences() {
		AppPage index = AppPage.builder("index", PageType.ENTRY_POINT).controller(Controller.UPDATE_PROFILE)
			.successNavigation("index").failureNavigation("notfound").build();
		AppPage notFound = AppPage.builder("notfound", PageType.PAGE).build();
		AppPage error = AppPage.builder("error", PageType.ERROR).build();
		return AppDetails.builder("ymnewsolutions", "preferences")
		.effectiveFrom(now.minusDays(30L)).effectiveTo(now.plusDays(30L)).enable().jwt("jwt.string")
		.pages(Arrays.asList(index, notFound, error)).build();
	}

	private static Map<String, String> usbankTriggeredEmails() {
		return Map.ofEntries(
			new AbstractMap.SimpleEntry<>("ACG", "EVTsign_uppageconfirmation_acg"),
			new AbstractMap.SimpleEntry<>("AMP", "EVTsign_uppageconfirmation_amp"),
			new AbstractMap.SimpleEntry<>("ASB", "EVTsign_uppageconfirmation_asb"),
			new AbstractMap.SimpleEntry<>("BMW", "EVTsign_uppageconfirmation_bmw"),
			new AbstractMap.SimpleEntry<>("EJ", "EVTsign_uppageconfirmation_ej"),
			new AbstractMap.SimpleEntry<>("ELA", "EVTsign_uppageconfirmation_ela"),
			new AbstractMap.SimpleEntry<>("ELL", "EVTsign_uppageconfirmation_ell"),
			new AbstractMap.SimpleEntry<>("FID", "EVTsign_uppageconfirmation_fid"),
			new AbstractMap.SimpleEntry<>("MNI", "EVTsign_uppageconfirmation_min"),
			new AbstractMap.SimpleEntry<>("MRD", "EVTsign_uppageconfirmation_mrd"),
			new AbstractMap.SimpleEntry<>("RAD", "EVTsign_uppageconfirmation_rad"),
			new AbstractMap.SimpleEntry<>("QKN", "EVTsign_uppageconfirmation_qkn")
		);
	}
}
