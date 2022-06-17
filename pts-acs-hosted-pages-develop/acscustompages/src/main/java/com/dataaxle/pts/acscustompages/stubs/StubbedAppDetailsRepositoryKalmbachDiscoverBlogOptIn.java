package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.HiddenService;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.conditions.AndCondition;
import com.dataaxle.pts.acscustompages.model.conditions.Condition;
import com.dataaxle.pts.acscustompages.model.conditions.HasEverHadServiceCondition;
import com.dataaxle.pts.acscustompages.model.conditions.HasServiceCondition;
import com.dataaxle.pts.acscustompages.model.conditions.NotCondition;
import com.dataaxle.pts.acscustompages.model.conditions.ProfileFoundCondition;
import com.dataaxle.pts.acscustompages.model.conditions.ProfileStringFieldValueCondition;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ConstantValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CurrentDateTimeValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.ActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ProfileActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;

import java.util.Collections;
import java.util.List;

public class StubbedAppDetailsRepositoryKalmbachDiscoverBlogOptIn extends StubbedAppDetailsRepositoryKalmbachMedia {

	public static AppDetails discoverOptIn() {
		final String PG_NM_CONFIRMATION = "confirmation";
		final String PG_NM_ERROR = "error";
		final String PG_NM_INDEX = "index";

		final String SVC_NM_SCIENCE = "science";

		final String YES = "Yes";
		final String NO = "No";
		final String EMPTY = "";

		final PageField<String> FLD_EMAIL = buildField("email", false, true, "",
			new ValidateEmail("email", true, true, false, 5, 100));

		final String EXISTS_UNSUB_SCIENCE_CONDITION_NM = "existsAndUnsubscribedScience";
		final String EXISTS_NEVER_SUB_SCIENCE_CONDITION_NM = "existsAndNeverSubScience";
		final String EXISTS_SUB_SCIENCE_CWDSC_NOT_SET_CONDITION_NM = "existsAndSubScienceAndCwdscNotSet";
		final String EXISTS_SUB_SCIENCE_CWDSC_SET_CONDITION_NM = "existsAndSubScienceAndCwdscSet";
		final String PROFILE_FOUND_CONDITION_NM = "profileFound";
		final String PROFILE_NOT_FOUND_CONDITION_NM = String.format("Not %s", PROFILE_FOUND_CONDITION_NM);
		final String EVER_SUBSCRIBED_SCIENCE_CONDITION_NM = "everSubscribedScience";
		final String SUBSCRIBED_SCIENCE_CONDITION_NM = "subscribedScience";
		final String CWDSC_NOT_SET_CONDITION_NM = "cwdscNotSet";
		final String CWDSC_YES_CONDITION_NM = "cwdscYes";

		final String FLD_NM_CWDSC = "cusCwdsc";
		final String FLD_NM_DADSC = "cusDadsc";
		final String FLD_NM_SCDSC = "cusScdsc";
		final String FLD_NM_OODSC = "cusOodsc";

		final Condition PROFILE_FOUND_COND = new ProfileFoundCondition(PROFILE_FOUND_CONDITION_NM, 0);
		final Condition PROFILE_NOT_FOUND_COND = new NotCondition(PROFILE_FOUND_COND, 0);

		final Condition EVER_SUBSCRIBED_SCIENCE_COND = new HasEverHadServiceCondition(EVER_SUBSCRIBED_SCIENCE_CONDITION_NM,
			20, SVC_NM_SCIENCE);
		final Condition SUBSCRIBED_SCIENCE_COND = new HasServiceCondition(SUBSCRIBED_SCIENCE_CONDITION_NM, 23, SVC_NM_SCIENCE);
		final Condition UNSUBSCRIBED_SCIENCE_COND = new NotCondition(SUBSCRIBED_SCIENCE_COND, 11);
		final Condition EXISTS_UNSUB_SCIENCE_COND = new AndCondition(EXISTS_UNSUB_SCIENCE_CONDITION_NM, 10,
			PROFILE_FOUND_COND, EVER_SUBSCRIBED_SCIENCE_COND, UNSUBSCRIBED_SCIENCE_COND);
		final Condition NEVER_SUBSCRIBED_SCIENCE_COND = new NotCondition(EVER_SUBSCRIBED_SCIENCE_COND, 21);
		final Condition EXISTS_NEVER_SUB_SCIENCE_COND = new AndCondition(EXISTS_NEVER_SUB_SCIENCE_CONDITION_NM, 20,
			PROFILE_FOUND_COND, NEVER_SUBSCRIBED_SCIENCE_COND);

		final Condition EXISTS_SUBSCRIBED_SCIENCE_CWDSC_NOT_SET_COND = new AndCondition(EXISTS_SUB_SCIENCE_CWDSC_NOT_SET_CONDITION_NM, 30,
			PROFILE_FOUND_COND, SUBSCRIBED_SCIENCE_COND, new ProfileStringFieldValueCondition(CWDSC_NOT_SET_CONDITION_NM,
			31, FLD_NM_CWDSC, List.of(EMPTY, NO), true, true));

		final Condition EXISTS_SUBSCRIBED_SCIENCE_CWDSC_SET_COND = new AndCondition(EXISTS_SUB_SCIENCE_CWDSC_SET_CONDITION_NM, 40,
			PROFILE_FOUND_COND, SUBSCRIBED_SCIENCE_COND, new ProfileStringFieldValueCondition(CWDSC_YES_CONDITION_NM, 41,
			FLD_NM_CWDSC, Collections.singletonList(YES)));

		final ValueGenerator<?> VG_EMAIL = new FormFieldValueGenerator.Builder()
											   .acsFieldName("email")
											   .adobeDataType("string")
											   .fieldName("email")
											   .build();

		final ValueGenerator<?> VG_CWDSC = new ConstantValueGenerator.Builder<>()
											   .acsFieldName(FLD_NM_CWDSC)
											   .adobeDataType("string")
											   .value(YES)
											   .build();

		final ValueGenerator<?> VG_DADSC = new CurrentDateTimeValueGenerator.Builder()
											   .acsFieldName(FLD_NM_DADSC)
											   .adobeDataType("datetime")
											   .build();

		final ValueGenerator<?> VG_SCDSC = new ConstantValueGenerator.Builder<>()
											   .acsFieldName(FLD_NM_SCDSC)
											   .adobeDataType("string")
											   .value("DSC_Blogs")
											   .build();

		final ValueGenerator<?> VG_OODSC = new ConstantValueGenerator.Builder<>()
											   .acsFieldName(FLD_NM_OODSC)
											   .adobeDataType("string")
											   .value("")
											   .build();

		final ValueGenerator<?> VG_ESS = new ConstantValueGenerator.Builder<>()
											 .acsFieldName("cusEverSubScience")
											 .adobeDataType("boolean")
											 .value(true)
											 .build();

		final List<ActionProcessor> ACTIONS_NEW_PROFILE = List.of(
			new ProfileActionProcessor(List.of(VG_EMAIL, VG_CWDSC, VG_DADSC, VG_SCDSC, VG_ESS)),
			new ServicesActionProcessor()
		);

		final List<ActionProcessor> ACTIONS_UNSUB_SCIENCE = List.of(
			new ProfileActionProcessor(List.of(VG_CWDSC, VG_DADSC, VG_SCDSC, VG_OODSC)),
			new ServicesActionProcessor()
		);

		final List<ActionProcessor> ACTIONS_NEVER_SUB_SCIENCE = List.of(
			new ProfileActionProcessor(List.of(VG_CWDSC, VG_DADSC, VG_SCDSC, VG_ESS)),
			new ServicesActionProcessor()
		);

		final List<ActionProcessor> ACTIONS_SUB_SCIENCE = List.of(
			new ProfileActionProcessor(List.of(VG_CWDSC, VG_DADSC, VG_SCDSC))
		);

		final List<ActionProcessor> ACTIONS_NONE = Collections.emptyList();

		final AppPage index = AppPage.builder(PG_NM_INDEX, PageType.ENTRY_POINT)
								  .controller(Controller.GET_PROFILE_CUSTOMERID)
								  .errorNavigation(PG_NM_ERROR)
								  .failureNavigation(PG_NM_CONFIRMATION)
								  .successNavigation(PG_NM_CONFIRMATION)
								  .addField(FLD_EMAIL)
								  .hiddenService(new HiddenService(SVC_NM_SCIENCE, ServiceAction.ADD))
								  .condition(PROFILE_NOT_FOUND_COND)
								  .condition(EXISTS_UNSUB_SCIENCE_COND)
								  .condition(EXISTS_NEVER_SUB_SCIENCE_COND)
								  .condition(EXISTS_SUBSCRIBED_SCIENCE_CWDSC_NOT_SET_COND)
								  .condition(EXISTS_SUBSCRIBED_SCIENCE_CWDSC_SET_COND)
								  .conditionalAction(PROFILE_NOT_FOUND_CONDITION_NM, ACTIONS_NEW_PROFILE)
								  .conditionalAction(EXISTS_UNSUB_SCIENCE_CONDITION_NM, ACTIONS_UNSUB_SCIENCE)
								  .conditionalAction(EXISTS_NEVER_SUB_SCIENCE_CONDITION_NM, ACTIONS_NEVER_SUB_SCIENCE)
								  .conditionalAction(EXISTS_SUB_SCIENCE_CWDSC_NOT_SET_CONDITION_NM, ACTIONS_SUB_SCIENCE)
								  .conditionalAction(EXISTS_SUB_SCIENCE_CWDSC_SET_CONDITION_NM, ACTIONS_NONE)
								  .build();

		final AppPage confirmation = AppPage.builder(PG_NM_CONFIRMATION, PageType.PAGE)
										 .controller(Controller.PROFILE_CONFIRM)
										 .build();

		final AppPage error = AppPage.builder(PG_NM_ERROR, PageType.ERROR)
												 .controller(Controller.GENERIC_PAGE)
												 .build();

		return AppDetails.builder(KALMBACH_NM, "discoverblog/optin")
				   .addPage(index)
				   .addPage(confirmation)
				   .addPage(error)
				   .enable()
				   .build();
	}
}
