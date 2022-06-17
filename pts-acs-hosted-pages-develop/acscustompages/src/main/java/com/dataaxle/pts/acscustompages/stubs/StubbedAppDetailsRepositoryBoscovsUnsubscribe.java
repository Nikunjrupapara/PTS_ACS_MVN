package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.HiddenService;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.navigation.NavigationProcessor;
import com.dataaxle.pts.acscustompages.model.navigation.SimpleNavigation;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;

import java.util.Arrays;

public class StubbedAppDetailsRepositoryBoscovsUnsubscribe extends StubbedAppDetailBase {

	public static AppDetails unsubscribe() {
		final NavigationProcessor indexNav = new SimpleNavigation.Builder().viewName("index").disableRedirect().build();

		AppPage lookup = AppPage.builder("lookup", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_SECURELINK)
				.successNavigation("index")
				.failureNavigation("error")
				.build();


		AppPage unsubscribe = AppPage.builder("index", PageType.PROFILE)
				.controller(Controller.UPDATE_SERVICES)

				.defaultCondition()

				.addField(buildField("cusCustomerIdHash", true, false, "", null))
				.addField(buildField("cusCustomerUUID", true, false, "", null))

				.successNavigation("confirm")
				.failureNavigation("error")
				.hiddenService(new HiddenService("retail", ServiceAction.REMOVE))

				.defaultActions(Arrays.asList(new ServicesActionProcessor()))

				.build();

		AppPage unsubConfirmation = AppPage.builder("confirm", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.build();

		AppPage error = AppPage.builder("error", PageType.ERROR)
				.controller(Controller.ERROR_PAGE)
				.build();


		return AppDetails.builder("boscovs", "unsubscribe")
				.enable()
				//.jwt("")
				.addPage(error)
				.addPage(unsubscribe)
				.addPage(unsubConfirmation)
				.addPage(lookup)
				.build();

	}

}
