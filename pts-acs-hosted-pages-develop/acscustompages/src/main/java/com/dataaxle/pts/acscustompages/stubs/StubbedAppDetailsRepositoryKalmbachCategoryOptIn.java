package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.validation.ValidateDate;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.ProfileActionProcessor;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;

import java.util.Collections;
import java.util.List;

public class StubbedAppDetailsRepositoryKalmbachCategoryOptIn extends StubbedAppDetailsRepositoryKalmbachMedia {

	public static final String BIRTH_DATE_NM = "birthDate";

	private static final String PG_NM_CONFIRMATION = "confirmation";
	private static final String PG_NM_INDEX = "index";
	private static final String PG_NM_PREFERENCES = "preferences";
	private static final String PG_NM_NOTFOUND = "notfound";
	private static final String PG_NM_ERROR = "error";

	private static final PageField<?> FLD_CUSTOMER_ID_HASH = buildField(CustomPagesConstants.CUS_CUSTOMER_ID_HASH, true, false, "", null);
	private static final PageField<?> FLD_EMAIL = buildField("email", false, false, "", null);
	private static final PageField<?> FLD_INT_DSC_ENVIRONMENT = buildField(INT_DSC_ENVIRONMENT_NM, false, false, "", null);
	private static final PageField<?> FLD_INT_DSC_HEALTH = buildField(INT_DSC_HEALTH_NM, false, false, "", null);
	private static final PageField<?> FLD_INT_DSC_LIVING = buildField(INT_DSC_LIVING_NM, false, false, "", null);
	private static final PageField<?> FLD_INT_DSC_MIND = buildField(INT_DSC_MIND_NM, false, false, "", null);
	private static final PageField<?> FLD_INT_DSC_SCIKIDS = buildField(INT_DSC_SCIKIDS_NM, false, false, "", null);
	private static final PageField<?> FLD_INT_DSC_SPACE = buildField(INT_DSC_SPACE_NM, false, false, "", null);
	private static final PageField<?> FLD_INT_DSC_STEM = buildField(INT_DSC_STEM_NM, false, false, "", null);
	private static final PageField<?> FLD_INT_DSC_TECH = buildField(INT_DSC_TECH_NM, false, false, "", null);
	private static final PageField<?> FLD_INT_EDUCATOR = buildField(INT_EDUCATOR_NM, false, false, "", null);

	private static final ValueGenerator<?> VG_INT_DSC_ENVIRONMENT = new FormFieldValueGenerator.Builder()
												   .acsFieldName(INT_DSC_ENVIRONMENT_NM)
												   .adobeDataType("string")
												   .fieldName(INT_DSC_ENVIRONMENT_NM)
												   .build();
	private static final ValueGenerator<?> VG_INT_DSC_HEALTH = new FormFieldValueGenerator.Builder()
											  .acsFieldName(INT_DSC_HEALTH_NM)
											  .adobeDataType("string")
											  .fieldName(INT_DSC_HEALTH_NM)
											  .build();
	private static final ValueGenerator<?> VG_INT_DSC_LIVING = new FormFieldValueGenerator.Builder()
											  .acsFieldName(INT_DSC_LIVING_NM)
											  .adobeDataType("string")
											  .fieldName(INT_DSC_LIVING_NM)
											  .build();
	private static final ValueGenerator<?> VG_INT_DSC_MIND = new FormFieldValueGenerator.Builder()
											.acsFieldName(INT_DSC_MIND_NM)
											.adobeDataType("string")
											.fieldName(INT_DSC_MIND_NM)
											.build();
	private static final ValueGenerator<?> VG_INT_DSC_SCIKIDS = new FormFieldValueGenerator.Builder()
											   .acsFieldName(INT_DSC_SCIKIDS_NM)
											   .adobeDataType("string")
											   .fieldName(INT_DSC_SCIKIDS_NM)
											   .build();
	private static final ValueGenerator<?> VG_INT_DSC_SPACE = new FormFieldValueGenerator.Builder()
											 .acsFieldName(INT_DSC_SPACE_NM)
											 .adobeDataType("string")
											 .fieldName(INT_DSC_SPACE_NM)
											 .build();
	private static final ValueGenerator<?> VG_INT_DSC_STEM = new FormFieldValueGenerator.Builder()
											.acsFieldName(INT_DSC_STEM_NM)
											.adobeDataType("string")
											.fieldName(INT_DSC_STEM_NM)
											.build();
	private static final ValueGenerator<?> VG_INT_DSC_TECH = new FormFieldValueGenerator.Builder()
											.acsFieldName(INT_DSC_TECH_NM)
											.adobeDataType("string")
											.fieldName(INT_DSC_TECH_NM)
											.build();
	private static final ValueGenerator<?> VG_INT_EDUCATOR = new FormFieldValueGenerator.Builder()
											.acsFieldName(INT_EDUCATOR_NM)
											.adobeDataType("string")
											.fieldName(INT_EDUCATOR_NM)
											.build();

	private static final AppPage confirmation = AppPage.builder(PG_NM_CONFIRMATION, PageType.PAGE)
							   .controller(Controller.PROFILE_CONFIRM)
							   .build();

	private static final AppPage notFound = AppPage.builder(PG_NM_NOTFOUND, PageType.PAGE)
						   .controller(Controller.GENERIC_PAGE)
						   .build();

	private static final AppPage error = AppPage.builder(PG_NM_ERROR, PageType.ERROR)
						.controller(Controller.GENERIC_PAGE)
						.build();

	public static AppDetails discoveryCategoryOptIn() {

		AppPage index = AppPage.builder(PG_NM_INDEX, PageType.ENTRY_POINT)
							.controller(Controller.GET_PROFILE_SECURELINK)
							.successNavigation(PG_NM_PREFERENCES)
							.errorNavigation(PG_NM_ERROR)
							.failureNavigation(PG_NM_NOTFOUND)
							.build();

		PageField<?> FLD_BDAY = buildField(BIRTH_DATE_NM, false, false, "",
			new ValidateDate(BIRTH_DATE_NM, false, "uuuu-MM-dd"));

		ValueGenerator<?> VG_BDAY = new FormFieldValueGenerator.Builder()
										.acsFieldName("birthDate")
										.adobeDataType("date")
										.fieldName(BIRTH_DATE_NM)
										.build();

		ProfileActionProcessor UPDATE_PROFILE = new ProfileActionProcessor(
			List.of(VG_BDAY, VG_INT_DSC_ENVIRONMENT, VG_INT_DSC_HEALTH, VG_INT_DSC_LIVING, VG_INT_DSC_MIND,
				VG_INT_DSC_SCIKIDS, VG_INT_DSC_SPACE, VG_INT_DSC_STEM, VG_INT_DSC_TECH, VG_INT_EDUCATOR));

		AppPage preferences = AppPage.builder(PG_NM_PREFERENCES, PageType.PAGE)
								  .controller(Controller.UPDATE_PROFILE)
								  .successNavigation(PG_NM_CONFIRMATION)
								  .failureNavigation(PG_NM_ERROR)
								  .errorNavigation(PG_NM_ERROR)
								  .missingInputView(PG_NM_NOTFOUND)
								  .addField(FLD_BDAY)
								  .addField(FLD_CUSTOMER_ID_HASH)
								  .addField(FLD_EMAIL)
								  .addField(FLD_INT_DSC_ENVIRONMENT)
								  .addField(FLD_INT_DSC_HEALTH)
								  .addField(FLD_INT_DSC_LIVING)
								  .addField(FLD_INT_DSC_MIND)
								  .addField(FLD_INT_DSC_SCIKIDS)
								  .addField(FLD_INT_DSC_SPACE)
								  .addField(FLD_INT_DSC_STEM)
								  .addField(FLD_INT_DSC_TECH)
								  .addField(FLD_INT_EDUCATOR)
								  .defaultActions(Collections.singletonList(UPDATE_PROFILE))
								  .build();

		return AppDetails.builder(KALMBACH_NM, "category/discovery")
				   .addPage(index)
				   .addPage(preferences)
				   .addPage(confirmation)
				   .addPage(notFound)
				   .addPage(error)
				   .enable()
				   .build();
	}

	public static AppDetails scienceCategoryOptIn() {

		AppPage index = AppPage.builder(PG_NM_INDEX, PageType.ENTRY_POINT)
							.controller(Controller.GET_PROFILE_SECURELINK)
							.successNavigation(PG_NM_PREFERENCES)
							.errorNavigation(PG_NM_ERROR)
							.failureNavigation(PG_NM_NOTFOUND)
							.build();

		ProfileActionProcessor UPDATE_PROFILE = new ProfileActionProcessor(
			List.of(VG_INT_DSC_ENVIRONMENT, VG_INT_DSC_HEALTH, VG_INT_DSC_LIVING, VG_INT_DSC_MIND,
				VG_INT_DSC_SCIKIDS, VG_INT_DSC_SPACE, VG_INT_DSC_STEM, VG_INT_DSC_TECH, VG_INT_EDUCATOR));

		AppPage preferences = AppPage.builder(PG_NM_PREFERENCES, PageType.PAGE)
								  .controller(Controller.UPDATE_PROFILE)
								  .successNavigation(PG_NM_CONFIRMATION)
								  .failureNavigation(PG_NM_ERROR)
								  .errorNavigation(PG_NM_ERROR)
								  .missingInputView(PG_NM_NOTFOUND)
								  .addField(FLD_CUSTOMER_ID_HASH)
								  .addField(FLD_EMAIL)
								  .addField(FLD_INT_DSC_ENVIRONMENT)
								  .addField(FLD_INT_DSC_HEALTH)
								  .addField(FLD_INT_DSC_LIVING)
								  .addField(FLD_INT_DSC_MIND)
								  .addField(FLD_INT_DSC_SCIKIDS)
								  .addField(FLD_INT_DSC_SPACE)
								  .addField(FLD_INT_DSC_STEM)
								  .addField(FLD_INT_DSC_TECH)
								  .addField(FLD_INT_EDUCATOR)
								  .defaultActions(Collections.singletonList(UPDATE_PROFILE))
								  .build();

		return AppDetails.builder(KALMBACH_NM, "category/science")
				   .addPage(index)
				   .addPage(preferences)
				   .addPage(confirmation)
				   .addPage(notFound)
				   .addPage(error)
				   .enable()
				   .build();
	}
}
