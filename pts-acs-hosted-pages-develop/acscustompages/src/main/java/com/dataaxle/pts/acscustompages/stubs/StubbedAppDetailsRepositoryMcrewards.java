package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.HiddenService;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.navigation.NavigationProcessor;
import com.dataaxle.pts.acscustompages.model.navigation.SimpleNavigation;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.validation.ValidateString;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CurrentDateTimeValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.ProfileActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;
import java.util.Arrays;
import java.util.List;

public class StubbedAppDetailsRepositoryMcrewards extends StubbedAppDetailBase {

    public static AppDetails unsubscribePoland() {
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
                .addField(buildField("cusMemberICA", true, false, "", null))


                .successNavigation("confirm")
                .failureNavigation("error")
                .hiddenService(new HiddenService("mcRewardsPoland", ServiceAction.REMOVE))

                .defaultActions(Arrays.asList(new ServicesActionProcessor()))

                .build();

        AppPage unsubConfirmation = AppPage.builder("confirm", PageType.PAGE)
                .controller(Controller.PROFILE_CONFIRM)
                .build();

        AppPage error = AppPage.builder("error", PageType.ERROR)
                .controller(Controller.ERROR_PAGE)
                .build();


        return AppDetails.builder("mcrewardspoland", "unsubscribe")
                .enable()
                //.jwt("")
                .addPage(error)
                .addPage(unsubscribe)
                .addPage(unsubConfirmation)
                .addPage(lookup)
                .build();

    }

    public static AppDetails unsubscribeBrazil() {
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


                .successNavigation("unsub-survey")
                .failureNavigation("error")
                .hiddenService(new HiddenService("mastercardBrazil", ServiceAction.REMOVE))

                .defaultActions(Arrays.asList(new ServicesActionProcessor()))

                .build();

        AppPage unsubSurvey = AppPage.builder("unsub-survey", PageType.PAGE)
                .controller(Controller.UPDATE_PROFILE)
                .defaultCondition()
                .successNavigation("confirm")
                .failureNavigation("error")
                .fields(Arrays.asList(
//                                buildField("email", true, true, "",
//                                        new ValidateEmail("email", true, false, false, 5, 100)),
                                buildField("cusCustomerIdHash", true, false, "",
                                        new ValidateString("cusCustomerIdHash", true, 32, 256, "")),
                                buildField("cusUnsubReason", false, true, "",
                                        new ValidateString("cusUnsubReason", true, 1, 255, ""))
                        )
                )
                .defaultActions(
                        Arrays.asList(
                                new ProfileActionProcessor(List.of(
                                        new FormFieldValueGenerator.Builder().acsFieldName("cusUnsubReason")
                                                .adobeDataType("string").fieldName("cusUnsubReason").build(),
                                        new CurrentDateTimeValueGenerator
                                                .Builder().acsFieldName("cusUnsubReasonDate").build()
                                ))
                        )
                )
                .missingInputView("unsub-survey")
                .disableSessionStorage()
                .build();

        AppPage unsubConfirmation = AppPage.builder("confirm", PageType.PAGE)
                .controller(Controller.PROFILE_CONFIRM)
                .build();

        AppPage error = AppPage.builder("error", PageType.ERROR)
                .controller(Controller.ERROR_PAGE)
                .build();


        return AppDetails.builder("mcrewardsbrazil", "unsubscribe")
                .enable()
                //.jwt("")
                .addPage(error)
                .addPage(unsubSurvey)
                .addPage(unsubscribe)
                .addPage(unsubConfirmation)
                .addPage(lookup)
                .build();

    }

}
