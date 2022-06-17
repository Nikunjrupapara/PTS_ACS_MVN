package com.dataaxle.pts.acscustompages.stubs;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_ID_HASH;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.HiddenService;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;

import java.util.List;

public class StubbedAppDetailsRepositoryCfna extends StubbedAppDetailBase {

	public static AppDetails unsubscribe() {

		final AppPage lookup = AppPage.builder("lookup", PageType.ENTRY_POINT)
			.controller(Controller.GET_PROFILE_SECURELINK)
			.successNavigation("index")
			.failureNavigation("error")
			.forwardQueryParameters()
			.forwardParameter("brand")
			.build();

		final AppPage unsubscribe = AppPage.builder("index", PageType.PROFILE)
			.controller(Controller.UPDATE_SERVICES)
			.defaultCondition()
			.addField(buildField(CUS_CUSTOMER_ID_HASH, true, false, "", null))
			.successNavigation("confirm")
			.failureNavigation("error")
			.errorNavigation("error")
			.hiddenService(new HiddenService("marketing", ServiceAction.REMOVE))
			.defaultActions(List.of(new ServicesActionProcessor()))
			.forwardQueryParameters()
			.forwardParameter("brand")
			.build();

		final AppPage unsubConfirm = AppPage.builder("confirm", PageType.PAGE)
			.controller(Controller.PROFILE_CONFIRM)
			.build();

		final AppPage error = AppPage.builder("error", PageType.ERROR)
			.controller(Controller.ERROR_PAGE)
			.build();

		return AppDetails.builder("cfna", "unsubscribe")
			.addPage(lookup)
			.addPage(unsubscribe)
			.addPage(unsubConfirm)
			.addPage(error)
			.enable()
			.build();
	}
}
