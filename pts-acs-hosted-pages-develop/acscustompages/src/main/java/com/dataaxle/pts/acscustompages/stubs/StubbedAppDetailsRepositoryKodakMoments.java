package com.dataaxle.pts.acscustompages.stubs;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.EMAIL;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceField;
import com.dataaxle.pts.acscustompages.model.conditions.AndCondition;
import com.dataaxle.pts.acscustompages.model.conditions.Condition;
import com.dataaxle.pts.acscustompages.model.conditions.FormFieldValueCondition;
import com.dataaxle.pts.acscustompages.model.conditions.NotCondition;
import com.dataaxle.pts.acscustompages.model.conditions.OrCondition;
import com.dataaxle.pts.acscustompages.model.conditions.ProfileFoundCondition;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.service.actions.ActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;

import java.util.Collections;
import java.util.List;

public class StubbedAppDetailsRepositoryKodakMoments extends StubbedAppDetailBase {

	public static AppDetails memberUnsub() {

		final PageField<?> FLD_EMAIL =  buildField(EMAIL, false, true, "",
			new ValidateEmail(EMAIL, true, true, false, 5, 100));

		final String UNSUB_MKTING_NM = "all";
		final String UNSUB_MEMBER_NM = "one";

		final ServiceField<?> SF_UNSUB_MKTING_KMART = buildServiceField(UNSUB_MKTING_NM, "1", "",
			Collections.emptyList(), List.of("marketing", "kmart-au"));
		final ServiceField<?> SF_UNSUB_MEMBER = buildServiceField(UNSUB_MEMBER_NM, "Member", "1",
			"", true, true);

		final String COND_PROFILE_FOUND_NM = "profileFound";
		final String COND_NO_PROFILE_NM = "Not profileFound";
		final String COND_NONE_CHECKED_NM = "noBoxesChecked";
		final String COND_ANY_BOX_CHECKED_NM = "anyBoxChecked";
		final String COND_UNSUB_MKTING_NM = "unsubMarketing";
		final String COND_UNSUB_MEMBER_NM = "unsubMember";

		final Condition COND_NO_PROFILE = new NotCondition(new ProfileFoundCondition(COND_PROFILE_FOUND_NM, 10));
		final Condition COND_UNSUB_MKTING_CHECKED = new FormFieldValueCondition(COND_UNSUB_MKTING_NM, 40, UNSUB_MKTING_NM, "1");
		final Condition COND_UNSUB_MEMBER_CHECKED = new FormFieldValueCondition(COND_UNSUB_MEMBER_NM, 50, UNSUB_MEMBER_NM, "1");

		final Condition COND_NO_BOXES_CHECKED = new AndCondition(COND_NONE_CHECKED_NM, 20,
			new NotCondition(COND_UNSUB_MKTING_CHECKED), new NotCondition(COND_UNSUB_MEMBER_CHECKED));
		final Condition COND_ANY_BOX_CHECKED = new OrCondition(COND_ANY_BOX_CHECKED_NM, 30,
			COND_UNSUB_MKTING_CHECKED, COND_UNSUB_MEMBER_CHECKED);

		final List<ActionProcessor> NO_ACTION = Collections.emptyList();
		final List<ActionProcessor> UNSUB_ACTION = Collections.singletonList(new ServicesActionProcessor());

		final AppPage index = AppPage.builder("index", PageType.ENTRY_POINT)
								  .controller(Controller.GET_PROFILE_CUSTOMERID)
								  .successNavigation("confirmation")
								  .failureNavigation("confirmation")
								  .errorNavigation("error")
								  .condition(COND_NO_PROFILE)
								  .condition(COND_NO_BOXES_CHECKED)
								  .condition(COND_ANY_BOX_CHECKED)
								  .addField(FLD_EMAIL)
								  .serviceField(SF_UNSUB_MKTING_KMART)
								  .serviceField(SF_UNSUB_MEMBER)
								  .conditionalAction(COND_NO_PROFILE_NM, NO_ACTION)
								  .conditionalAction(COND_NONE_CHECKED_NM, NO_ACTION)
								  .conditionalAction(COND_ANY_BOX_CHECKED_NM, UNSUB_ACTION)
								  .recaptchaSecret("6LcY7rEbAAAAAFX4eLw1kj0QtyLa3VnP8ams5zFL")
				                  .recaptchaKey("6LcY7rEbAAAAAHYKam_v6um6h06EtJ_PGOFpzWod")
								  .build();

		final AppPage confirmation = AppPage.builder("confirmation", PageType.PAGE)
										 .controller(Controller.PROFILE_CONFIRM)
										 .build();

		final AppPage error = AppPage.builder("error", PageType.ERROR)
								  .controller(Controller.ERROR_PAGE)
								  .build();

		return AppDetails.builder("kodakmoments", "member-unsub")
				   .addPage(index)
				   .addPage(confirmation)
				   .addPage(error)
				   .enable()
				   .build();
	}
}
