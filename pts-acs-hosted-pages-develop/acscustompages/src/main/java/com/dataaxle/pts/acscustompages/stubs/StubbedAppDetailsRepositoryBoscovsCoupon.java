package com.dataaxle.pts.acscustompages.stubs;

import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.ADT_DATETIME;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.ADT_INT64;
import static com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository.ADT_STRING;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.ACS_ID;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.PKEY;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.conditions.CustomResourceFoundCondition;
import com.dataaxle.pts.acscustompages.model.conditions.NotCondition;
import com.dataaxle.pts.acscustompages.model.navigation.NavigationProcessor;
import com.dataaxle.pts.acscustompages.model.navigation.SimpleNavigation;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ConstantValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CurrentDateTimeValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.CustomResourceFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.OffsetNumberValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.CustomResourcesActionProcessor;

import java.util.Collections;
import java.util.List;

public class StubbedAppDetailsRepositoryBoscovsCoupon extends StubbedAppDetailBase {

	public static AppDetails boscovs_coupon() {

		final String customResource = "cusCouponLog";
		final String ACCESSED_COUNT = "accessedCount";

		final String COUPON_FOUND_COND_NM = "coupon found";
		final String COUPON_NOT_FOUND_COND_NM = "Not " + COUPON_FOUND_COND_NM;


		final NavigationProcessor indexNav = new SimpleNavigation.Builder().viewName("index").disableRedirect().build();
		final AppPage index = AppPage.builder("index", PageType.ENTRY_POINT)
			.controller(Controller.NONE)
			.successNavigation(indexNav)
			.failureNavigation(indexNav)
			.errorNavigation(indexNav)
			.noRedirectOnError()
			.missingInputView("index")
			.customResource(customResource)
			.mapQueryParameter("l", "customerIdHash")
			.mapQueryParameter("s", "customerUuid")
			.mapQueryParameter("barcode", "barcode")
			.addField(buildField("accessedCount", false, false, 0, null))
			.addField(buildField("barcode", false, false, "", null))
			.addField(buildField("expireDate", false, false, "", null))
			.addField(buildField("image", false, false, "", null))
			.addField(buildField("instructions", false, false, "", null))
			.addField(buildField("legal", false, false, "", null))
			.addField(buildField("limit", false, false, 0, null))
			.addField(buildField("onlineCode", false, false, "", null))
			.condition(new CustomResourceFoundCondition(COUPON_FOUND_COND_NM, 0))
			.condition(new NotCondition(new CustomResourceFoundCondition(COUPON_FOUND_COND_NM, 1)))
			.conditionalAction(COUPON_NOT_FOUND_COND_NM, Collections.emptyList())
			.conditionalAction(COUPON_FOUND_COND_NM, List.of(
				new CustomResourcesActionProcessor(
					Collections.singletonList(
						buildCustomResourceConfig(customResource,
							List.of(
								buildCustomResourceField(
									new CustomResourceFieldValueGenerator.Builder()
										.acsFieldName(ACS_ID)
										.adobeDataType(ADT_STRING)
										.fieldName(ACS_ID)
										.build()
								),
								buildCustomResourceField(
									new OffsetNumberValueGenerator.Builder<>()
										.acsFieldName(ACCESSED_COUNT)
										.adobeDataType(ADT_INT64)
										.leftSide(
											(ValueGenerator<Object>) new CustomResourceFieldValueGenerator.Builder()
												.acsFieldName(ACCESSED_COUNT)
												.adobeDataType(ADT_INT64)
												.fieldName(ACCESSED_COUNT)
												.build()
										)
										.rightSide(
											(ValueGenerator<Object>) new ConstantValueGenerator.Builder<>()
												.acsFieldName("one")
												.adobeDataType(ADT_INT64)
												.value(1)
												.build()
										)
										.operator(OffsetNumberValueGenerator.Operator.ADD)
										.build()
								),
								buildCustomResourceField(
									new CurrentDateTimeValueGenerator.Builder()
										.acsFieldName("lastModified")
										.adobeDataType(ADT_DATETIME)
										.zoneId("UTC")
										.build()
								)
							))))
			))
			.build();
		return AppDetails.builder("boscovs", "coupon")
			.addPage(index)
			.enable()
			.build();
	}
}
