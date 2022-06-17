package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.HiddenService;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.TriggeredEmailConfig;
import com.dataaxle.pts.acscustompages.model.conditions.AlwaysTrueCondition;
import com.dataaxle.pts.acscustompages.model.conditions.AndCondition;
import com.dataaxle.pts.acscustompages.model.conditions.FormFieldValueCondition;
import com.dataaxle.pts.acscustompages.model.conditions.HasEverHadServiceCondition;
import com.dataaxle.pts.acscustompages.model.conditions.HasServiceCondition;
import com.dataaxle.pts.acscustompages.model.conditions.NotCondition;
import com.dataaxle.pts.acscustompages.model.conditions.OrCondition;
import com.dataaxle.pts.acscustompages.model.conditions.ProfileFoundCondition;
import com.dataaxle.pts.acscustompages.model.conditions.ProfileStringFieldValueCondition;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.valuegenerator.BooleanValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ConstantValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ProfileValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.ProfileActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.TriggerEmailActionProcessor;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StubbedAppDetailsRepositoryKalmbachBeginner  extends StubbedAppDetailsRepositoryKalmbachMedia{

    public static AppDetails signupASY(){
        return signupPage("science","asy", "cusEverSubScience","EVTBGN00_ASY_000000_0STEP1");
    }

    public static AppDetails signupCTT(){
        return signupPage("hobby","ctt", "cusEverSubHobby", "EVTBGN00_CTT_000000_0STEP1");
    }

    public static AppDetails signupFSM(){
        return signupPage("hobby","fsm", "cusEverSubHobby","EVTBGN00_FSM_000000_0Step0_THKYOU");
    }

    public static AppDetails signupMRR(){
        return signupPage("hobby","mrr", "cusEverSubHobby", "EVTBGN00_MRR_000000_0STEP1");
    }

    public static AppDetails signupPage(String service, String variant, String everSub, String emailId) {

        ValueGenerator<?> email = new FormFieldValueGenerator.Builder().acsFieldName("email").adobeDataType("string").fieldName("email").build();

        PageField<?> customerIdHash = buildField("cusCustomerIdHash", true, false, "", null);
//        PageField<?> customerUUID = buildField("cusCustomerUUID", true, false, "", null);

        ValidateEmail validateEmail = new ValidateEmail("email", true, true, false, null, null);
        PageField<?> emailField = buildField("email", false, true, "",validateEmail);

        TriggeredEmailConfig emailConfig = buildEmailConfig(emailId, email);

        AppPage signup = AppPage.builder("signup", PageType.ENTRY_POINT)
                .controller(Controller.GET_PROFILE_CUSTOMERID)

                .condition(new NotCondition(new ProfileFoundCondition("profileExists", 0)))

                .condition(new NotCondition(new HasEverHadServiceCondition("wasSubscribed",10, service)))

                .condition(new AndCondition("existingAndDone",20,
                        new ProfileFoundCondition("profileExists",0),
                        // assume anything that is not "No" is done  (Yes, null or empty), not case insensitive
                        new NotCondition( new ProfileStringFieldValueCondition("cusBgn_"+variant, 0, "cusBgn_"+variant, Collections.singletonList("No"),false)))

                        //  new ProfileStringFieldValueCondition("cusBgn_"+variant, 0, "cusBgn_"+variant, Collections.singletonList("Yes")))
                )

                .condition(new AndCondition("extAndNotSubscribed",30,
                        new ProfileFoundCondition("profileExists",0),
                        new NotCondition(new HasServiceCondition("isSubscribed",0,service)))
                )

                .condition(new AlwaysTrueCondition("defaultAction", 999999))

                .successNavigation("confirm")
                .failureNavigation("confirm")

                .hiddenService(new HiddenService(service, ServiceAction.ADD))

                .fields(Arrays.asList(emailField, customerIdHash))

                .conditionalAction("Not profileExists",Arrays.asList(
                        new ServicesActionProcessor(),
                        new ProfileActionProcessor(
                                List.of(
                                        email,
                                        new ConstantValueGenerator.Builder<String>()
                                                .acsFieldName("cusBgn"+variant)
                                                .adobeDataType("string")
                                                .value("Yes").build(),
                                        new ConstantValueGenerator.Builder<Boolean>()
                                                .acsFieldName(everSub)
                                                .adobeDataType("boolean")
                                                .value(true).build()

                                )
                        )
                        ,new TriggerEmailActionProcessor(emailConfig)
                ))
                .conditionalAction("Not wasSubscribed",Arrays.asList(
                        new ServicesActionProcessor(),
                        new ProfileActionProcessor(
                                List.of(
                                        email,
                                        new ConstantValueGenerator.Builder<String>()
                                                .acsFieldName("cusBgn"+variant)
                                                .adobeDataType("string")
                                                .value("Yes").build(),
                                        new ConstantValueGenerator.Builder<Boolean>()
                                                .acsFieldName(everSub)
                                                .adobeDataType("boolean")
                                                .value(true).build()
                                )
                        )
                        ,new TriggerEmailActionProcessor(emailConfig)
                ))
                .conditionalAction("existingAndDone",Arrays.asList(
                        new ServicesActionProcessor(),
                        new ProfileActionProcessor(
                                List.of(
                                        email,
                                        new ConstantValueGenerator.Builder<String>()
                                                .acsFieldName("cusBgn"+variant)
                                                .adobeDataType("string")
                                                .value("Yes").build(),
                                        new ConstantValueGenerator.Builder<String>()
                                                .acsFieldName("cusBgn_"+variant)
                                                .adobeDataType("string")
                                                .value("No").build(),
                                        new ConstantValueGenerator.Builder<Boolean>()
                                                .acsFieldName(everSub)
                                                .adobeDataType("boolean")
                                                .value(true).build()
                                )
                        )
                        ,new TriggerEmailActionProcessor(emailConfig)
                ))
                .conditionalAction("extAndNotSubscribed",Arrays.asList(
                        new ServicesActionProcessor(),
                        new ProfileActionProcessor(
                                List.of(
                                        email,
                                        new ConstantValueGenerator.Builder<String>()
                                                .acsFieldName("cusBgn"+variant)
                                                .adobeDataType("string")
                                                .value("Yes").build(),
                                        new ConstantValueGenerator.Builder<String>()
                                                .acsFieldName("cusBgn_"+variant)
                                                .adobeDataType("string")
                                                .value("No").build(),
                                        new ConstantValueGenerator.Builder<Boolean>()
                                                .acsFieldName(everSub)
                                                .adobeDataType("boolean")
                                                .value(true).build()
                                )
                        )
                        ,new TriggerEmailActionProcessor(emailConfig)
                ))

                .conditionalAction("defaultAction", Arrays.asList())

                .build();

        AppPage confirmation = AppPage.builder("confirm", PageType.PAGE)
                .controller(Controller.PROFILE_CONFIRM)
                .fields(Collections.singletonList(
                        buildField("email", true, false, "", null)
                ))
                .build();

        return AppDetails.builder(KALMBACH_NM, "beginner/"+variant)
                .enable().addPage(signup).addPage(confirmation).build();

    }

    private static TriggeredEmailConfig buildEmailConfig(String emailId, ValueGenerator<?> email){
        return new TriggeredEmailConfig.Builder()
                .eventIds(Map.of("kalmbachmedia", emailId))   // Event needs to be verified/created
                .email(email)
                .contextField(new FormFieldValueGenerator.Builder()
                        .adobeDataType("string")
                        .acsFieldName("customerIdHash")
                        .fieldName("cusCustomerIdHash")
                        .build()
                )
                .build();
    }
}
