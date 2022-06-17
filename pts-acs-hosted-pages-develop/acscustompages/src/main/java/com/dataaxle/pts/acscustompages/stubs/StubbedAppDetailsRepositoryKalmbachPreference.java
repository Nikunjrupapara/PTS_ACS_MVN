package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.controller.Controller;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.PageType;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.TriggeredEmailConfig;
import com.dataaxle.pts.acscustompages.model.conditions.HasServiceCondition;
import com.dataaxle.pts.acscustompages.model.conditions.NotCondition;
import com.dataaxle.pts.acscustompages.model.conditions.ProfileFoundCondition;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.validation.ValidateString;
import com.dataaxle.pts.acscustompages.model.valuegenerator.BooleanValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.FormFieldValueGenerator;
import com.dataaxle.pts.acscustompages.model.valuegenerator.ValueGenerator;
import com.dataaxle.pts.acscustompages.service.actions.ProfileActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.ServicesActionProcessor;
import com.dataaxle.pts.acscustompages.service.actions.TriggerEmailActionProcessor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StubbedAppDetailsRepositoryKalmbachPreference  extends StubbedAppDetailsRepositoryKalmbachMedia{



	static ValueGenerator<?> email = new FormFieldValueGenerator.Builder()
			.acsFieldName("email")
			.adobeDataType("string")
			.fieldName("email")
			.build();
	static ValueGenerator<?> firstName = new FormFieldValueGenerator.Builder()
			.acsFieldName("firstName")
			.adobeDataType("string")
			.fieldName("firstName")
			.ignoreWhenEmpty()
			.build();
	static ValueGenerator<?> lastName = new FormFieldValueGenerator.Builder()
			.acsFieldName("lastName")
			.adobeDataType("string")
			.fieldName("lastName")
			.ignoreWhenEmpty()
			.build();

	static ValueGenerator<?> birthDate = new FormFieldValueGenerator.Builder()
			.acsFieldName("birthDate")
			.adobeDataType("string")
			.fieldName("birthDate")
			.ignoreWhenEmpty()
			.build();


	static ValueGenerator<?> country = new FormFieldValueGenerator.Builder()
			.acsFieldName("location.countryCode")
			.adobeDataType("string")
			.fieldName("location.countryCode")
			.build();

// signup

	static ValueGenerator<?> cat_aircraft = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_aircraft").adobeDataType("string").fieldName("cusCat_aircraft").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_armors = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_armors").adobeDataType("string").fieldName("cusCat_armors").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_astronomy = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_astronomy").adobeDataType("string").fieldName("cusCat_astronomy").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_automobiles = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_automobiles").adobeDataType("string").fieldName("cusCat_automobiles").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_beginner = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_beginner").adobeDataType("string").fieldName("cusCat_beginner").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_biology = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_biology").adobeDataType("string").fieldName("cusCat_biology").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_chemistry = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_chemistry").adobeDataType("string").fieldName("cusCat_chemistry").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_deepskyobjects = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_deepskyobjects").adobeDataType("string").fieldName("cusCat_deepskyobjects").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_environment = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_environment").adobeDataType("string").fieldName("cusCat_environment").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_exoticobjects = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_exoticobjects").adobeDataType("string").fieldName("cusCat_exoticobjects").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_experttips = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_experttips").adobeDataType("string").fieldName("cusCat_experttips").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_figures = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_figures").adobeDataType("string").fieldName("cusCat_figures").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_freightrail = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_freightrail").adobeDataType("string").fieldName("cusCat_freightrail").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_health = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_health").adobeDataType("string").fieldName("cusCat_health").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_howto = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_howto").adobeDataType("string").fieldName("cusCat_howto").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_layouts = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_layouts").adobeDataType("string").fieldName("cusCat_layouts").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_mind = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_mind").adobeDataType("string").fieldName("cusCat_mind").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_observing = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_observing").adobeDataType("string").fieldName("cusCat_observing").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_oursolarsystem = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_oursolarsystem").adobeDataType("string").fieldName("cusCat_oursolarsystem").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_passengerrail = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_passengerrail").adobeDataType("string").fieldName("cusCat_passengerrail").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_photos = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_photos").adobeDataType("string").fieldName("cusCat_photos").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_physics = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_physics").adobeDataType("string").fieldName("cusCat_physics").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_planetearth = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_planetearth").adobeDataType("string").fieldName("cusCat_planetearth").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_railfanning = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_railfanning").adobeDataType("string").fieldName("cusCat_railfanning").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_railroadindustry = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_railroadindustry").adobeDataType("string").fieldName("cusCat_railroadindustry").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_railroadstories = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_railroadstories").adobeDataType("string").fieldName("cusCat_railroadstories").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_scifi = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_scifi").adobeDataType("string").fieldName("cusCat_scifi").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_ships = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_ships").adobeDataType("string").fieldName("cusCat_ships").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_spaceexploration = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_spaceexploration").adobeDataType("string").ignoreWhenEmpty().fieldName("cusCat_spaceexploration").build();
	static ValueGenerator<?> cat_spacenews = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_spacenews").adobeDataType("string").fieldName("cusCat_spacenews").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_technology = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_technology").adobeDataType("string").fieldName("cusCat_technology").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_trackplans = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_trackplans").adobeDataType("string").fieldName("cusCat_trackplans").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_trainbasics = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_trainbasics").adobeDataType("string").fieldName("cusCat_trainbasics").ignoreWhenEmpty().build();
	static ValueGenerator<?> cat_videos = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_videos").adobeDataType("string").fieldName("cusCat_videos").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwctr = new FormFieldValueGenerator.Builder().acsFieldName("cusCwctr").adobeDataType("string").fieldName("cusCwctr").ignoreWhenEmpty().build();
	static ValueGenerator<?> dactr = new FormFieldValueGenerator.Builder().acsFieldName("cusDactr").adobeDataType("string").fieldName("cusDactr").ignoreWhenEmpty().build();
	static ValueGenerator<?> scctr = new FormFieldValueGenerator.Builder().acsFieldName("cusScctr").adobeDataType("string").fieldName("cusScctr").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwctt = new FormFieldValueGenerator.Builder().acsFieldName("cusCwctt").adobeDataType("string").fieldName("cusCwctt").ignoreWhenEmpty().build();
	static ValueGenerator<?> dactt = new FormFieldValueGenerator.Builder().acsFieldName("cusDactt").adobeDataType("string").fieldName("cusDactt").ignoreWhenEmpty().build();
	static ValueGenerator<?> scctt = new FormFieldValueGenerator.Builder().acsFieldName("cusScctt").adobeDataType("string").fieldName("cusScctt").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwgrw = new FormFieldValueGenerator.Builder().acsFieldName("cusCwgrw").adobeDataType("string").fieldName("cusCwgrw").ignoreWhenEmpty().build();
	static ValueGenerator<?> dagrw = new FormFieldValueGenerator.Builder().acsFieldName("cusDagrw").adobeDataType("string").fieldName("cusDagrw").ignoreWhenEmpty().build();
	static ValueGenerator<?> scgrw = new FormFieldValueGenerator.Builder().acsFieldName("cusScgrw").adobeDataType("string").fieldName("cusScgrw").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwhbs = new FormFieldValueGenerator.Builder().acsFieldName("cusCwhbs").adobeDataType("string").fieldName("cusCwhbs").ignoreWhenEmpty().build();
	static ValueGenerator<?> dahbs = new FormFieldValueGenerator.Builder().acsFieldName("cusDahbs").adobeDataType("string").fieldName("cusDahbs").ignoreWhenEmpty().build();
	static ValueGenerator<?> schbs = new FormFieldValueGenerator.Builder().acsFieldName("cusSchbs").adobeDataType("string").fieldName("cusSchbs").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwmrr = new FormFieldValueGenerator.Builder().acsFieldName("cusCwmrr").adobeDataType("string").fieldName("cusCwmrr").ignoreWhenEmpty().build();
	static ValueGenerator<?> damrr = new FormFieldValueGenerator.Builder().acsFieldName("cusDamrr").adobeDataType("string").fieldName("cusDamrr").ignoreWhenEmpty().build();
	static ValueGenerator<?> scmrr = new FormFieldValueGenerator.Builder().acsFieldName("cusScmrr").adobeDataType("string").fieldName("cusScmrr").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwtrn = new FormFieldValueGenerator.Builder().acsFieldName("cusCwtrn").adobeDataType("string").fieldName("cusCwtrn").ignoreWhenEmpty().build();
	static ValueGenerator<?> datrn = new FormFieldValueGenerator.Builder().acsFieldName("cusDatrn").adobeDataType("string").fieldName("cusDatrn").ignoreWhenEmpty().build();
	static ValueGenerator<?> sctrn = new FormFieldValueGenerator.Builder().acsFieldName("cusSctrn").adobeDataType("string").fieldName("cusSctrn").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwtdc = new FormFieldValueGenerator.Builder().acsFieldName("cusCwtdc").adobeDataType("string").fieldName("cusCwtdc").ignoreWhenEmpty().build();
	static ValueGenerator<?> datdc = new FormFieldValueGenerator.Builder().acsFieldName("cusDatdc").adobeDataType("string").fieldName("cusDatdc").ignoreWhenEmpty().build();
	static ValueGenerator<?> sctdc = new FormFieldValueGenerator.Builder().acsFieldName("cusSctdc").adobeDataType("string").fieldName("cusSctdc").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwtin = new FormFieldValueGenerator.Builder().acsFieldName("cusCwtind").adobeDataType("string").fieldName("cusCwtind").ignoreWhenEmpty().build();
	static ValueGenerator<?> datin = new FormFieldValueGenerator.Builder().acsFieldName("cusDatind").adobeDataType("string").fieldName("cusDatind").ignoreWhenEmpty().build();
	static ValueGenerator<?> sctin = new FormFieldValueGenerator.Builder().acsFieldName("cusSctind").adobeDataType("string").fieldName("cusSctind").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwasy = new FormFieldValueGenerator.Builder().acsFieldName("cusCwasy").adobeDataType("string").fieldName("cusCwasy").ignoreWhenEmpty().build();
	static ValueGenerator<?> daasy = new FormFieldValueGenerator.Builder().acsFieldName("cusDaasy").adobeDataType("string").fieldName("cusDaasy").ignoreWhenEmpty().build();
	static ValueGenerator<?> scasy = new FormFieldValueGenerator.Builder().acsFieldName("cusScasy").adobeDataType("string").fieldName("cusScasy").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwdsc = new FormFieldValueGenerator.Builder().acsFieldName("cusCwdsc").adobeDataType("string").fieldName("cusCwdsc").ignoreWhenEmpty().build();
	static ValueGenerator<?> dadsc = new FormFieldValueGenerator.Builder().acsFieldName("cusDadsc").adobeDataType("string").fieldName("cusDadsc").ignoreWhenEmpty().build();
	static ValueGenerator<?> scdsc = new FormFieldValueGenerator.Builder().acsFieldName("cusScdsc").adobeDataType("string").fieldName("cusScdsc").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwsci = new FormFieldValueGenerator.Builder().acsFieldName("cusCwsci").adobeDataType("string").fieldName("cusCwsci").ignoreWhenEmpty().build();
	static ValueGenerator<?> dasci = new FormFieldValueGenerator.Builder().acsFieldName("cusDasci").adobeDataType("string").fieldName("cusDasci").ignoreWhenEmpty().build();
	static ValueGenerator<?> scsci = new FormFieldValueGenerator.Builder().acsFieldName("cusScsci").adobeDataType("string").fieldName("cusScsci").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwasx = new FormFieldValueGenerator.Builder().acsFieldName("cusCwasx").adobeDataType("string").fieldName("cusCwasx").ignoreWhenEmpty().build();
	static ValueGenerator<?> daasx = new FormFieldValueGenerator.Builder().acsFieldName("cusDaasx").adobeDataType("string").fieldName("cusDaasx").ignoreWhenEmpty().build();
	static ValueGenerator<?> scasx = new FormFieldValueGenerator.Builder().acsFieldName("cusScasx").adobeDataType("string").fieldName("cusScasx").ignoreWhenEmpty().build();

	static ValueGenerator<?> cwfsm = new FormFieldValueGenerator.Builder().acsFieldName("cusCwfsm").adobeDataType("string").fieldName("cusCwfsm").ignoreWhenEmpty().build();
	static ValueGenerator<?> dafsm = new FormFieldValueGenerator.Builder().acsFieldName("cusDafsm").adobeDataType("string").fieldName("cusDafsm").ignoreWhenEmpty().build();
	static ValueGenerator<?> scfsm = new FormFieldValueGenerator.Builder().acsFieldName("cusScfsm").adobeDataType("string").fieldName("cusScfsm").ignoreWhenEmpty().build();


	static ValueGenerator<?> cusCwbir = new FormFieldValueGenerator.Builder().acsFieldName("cusCwbir").adobeDataType("string").fieldName("cusCwbir").ignoreWhenEmpty().build();
	static ValueGenerator<?> cusDabir = new FormFieldValueGenerator.Builder().acsFieldName("cusDabir").adobeDataType("string").fieldName("cusDabir").ignoreWhenEmpty().build();
	static ValueGenerator<?> cusScbir = new FormFieldValueGenerator.Builder().acsFieldName("cusScbir").adobeDataType("string").fieldName("cusScbir").ignoreWhenEmpty().build();

	static ValueGenerator<?> subscribedToScience = new BooleanValueGenerator.Builder().acsFieldName("cusEverSubScience").adobeDataType("boolean").fieldName("scienceOptIn").trueValue("Y").falseValue("").actsUpon("formData").ignoreWhenEmpty().build();
	static ValueGenerator<?> subscribedToHobby = new BooleanValueGenerator.Builder().acsFieldName("cusEverSubHobby").adobeDataType("boolean").fieldName("hobbyOptIn").trueValue("Y").falseValue("").actsUpon("formData").ignoreWhenEmpty().build();


// preference generators

	static ValueGenerator<?> pref_cat_aircraft = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_aircraft").adobeDataType("string").fieldName("cusCat_aircraft").build();
	static ValueGenerator<?> pref_cat_armors = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_armors").adobeDataType("string").fieldName("cusCat_armors").build();
	static ValueGenerator<?> pref_cat_astronomy = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_astronomy").adobeDataType("string").fieldName("cusCat_astronomy").build();
	static ValueGenerator<?> pref_cat_automobiles = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_automobiles").adobeDataType("string").fieldName("cusCat_automobiles").build();
	static ValueGenerator<?> pref_cat_beginner = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_beginner").adobeDataType("string").fieldName("cusCat_beginner").build();
	static ValueGenerator<?> pref_cat_biology = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_biology").adobeDataType("string").fieldName("cusCat_biology").build();
	static ValueGenerator<?> pref_cat_chemistry = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_chemistry").adobeDataType("string").fieldName("cusCat_chemistry").build();
	static ValueGenerator<?> pref_cat_deepskyobjects = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_deepskyobjects").adobeDataType("string").fieldName("cusCat_deepskyobjects").build();
	static ValueGenerator<?> pref_cat_environment = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_environment").adobeDataType("string").fieldName("cusCat_environment").build();
	static ValueGenerator<?> pref_cat_exoticobjects = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_exoticobjects").adobeDataType("string").fieldName("cusCat_exoticobjects").build();
	static ValueGenerator<?> pref_cat_experttips = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_experttips").adobeDataType("string").fieldName("cusCat_experttips").build();
	static ValueGenerator<?> pref_cat_figures = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_figures").adobeDataType("string").fieldName("cusCat_figures").build();
	static ValueGenerator<?> pref_cat_freightrail = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_freightrail").adobeDataType("string").fieldName("cusCat_freightrail").build();
	static ValueGenerator<?> pref_cat_health = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_health").adobeDataType("string").fieldName("cusCat_health").build();
	static ValueGenerator<?> pref_cat_howto = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_howto").adobeDataType("string").fieldName("cusCat_howto").build();
	static ValueGenerator<?> pref_cat_layouts = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_layouts").adobeDataType("string").fieldName("cusCat_layouts").build();
	static ValueGenerator<?> pref_cat_mind = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_mind").adobeDataType("string").fieldName("cusCat_mind").build();
	static ValueGenerator<?> pref_cat_observing = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_observing").adobeDataType("string").fieldName("cusCat_observing").build();
	static ValueGenerator<?> pref_cat_oursolarsystem = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_oursolarsystem").adobeDataType("string").fieldName("cusCat_oursolarsystem").build();
	static ValueGenerator<?> pref_cat_passengerrail = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_passengerrail").adobeDataType("string").fieldName("cusCat_passengerrail").build();
	static ValueGenerator<?> pref_cat_photos = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_photos").adobeDataType("string").fieldName("cusCat_photos").build();
	static ValueGenerator<?> pref_cat_physics = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_physics").adobeDataType("string").fieldName("cusCat_physics").build();
	static ValueGenerator<?> pref_cat_planetearth = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_planetearth").adobeDataType("string").fieldName("cusCat_planetearth").build();
	static ValueGenerator<?> pref_cat_railfanning = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_railfanning").adobeDataType("string").fieldName("cusCat_railfanning").build();
	static ValueGenerator<?> pref_cat_railroadindustry = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_railroadindustry").adobeDataType("string").fieldName("cusCat_railroadindustry").build();
	static ValueGenerator<?> pref_cat_railroadstories = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_railroadstories").adobeDataType("string").fieldName("cusCat_railroadstories").build();
	static ValueGenerator<?> pref_cat_scifi = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_scifi").adobeDataType("string").fieldName("cusCat_scifi").build();
	static ValueGenerator<?> pref_cat_ships = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_ships").adobeDataType("string").fieldName("cusCat_ships").build();
	static ValueGenerator<?> pref_cat_spaceexploration = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_spaceexploration").adobeDataType("string").fieldName("cusCat_spaceexploration").build();
	static ValueGenerator<?> pref_cat_spacenews = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_spacenews").adobeDataType("string").fieldName("cusCat_spacenews").build();
	static ValueGenerator<?> pref_cat_technology = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_technology").adobeDataType("string").fieldName("cusCat_technology").build();
	static ValueGenerator<?> pref_cat_trackplans = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_trackplans").adobeDataType("string").fieldName("cusCat_trackplans").build();
	static ValueGenerator<?> pref_cat_trainbasics = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_trainbasics").adobeDataType("string").fieldName("cusCat_trainbasics").build();
	static ValueGenerator<?> pref_cat_videos = new FormFieldValueGenerator.Builder().acsFieldName("cusCat_videos").adobeDataType("string").fieldName("cusCat_videos").build();

	static ValueGenerator<?> pref_cwctr = new FormFieldValueGenerator.Builder().acsFieldName("cusCwctr").adobeDataType("string").fieldName("cusCwctr").build();
	static ValueGenerator<?> pref_dactr = new FormFieldValueGenerator.Builder().acsFieldName("cusDactr").adobeDataType("string").fieldName("cusDactr").build();
	static ValueGenerator<?> pref_scctr = new FormFieldValueGenerator.Builder().acsFieldName("cusScctr").adobeDataType("string").fieldName("cusScctr").build();

	static ValueGenerator<?> pref_cwctt = new FormFieldValueGenerator.Builder().acsFieldName("cusCwctt").adobeDataType("string").fieldName("cusCwctt").build();
	static ValueGenerator<?> pref_dactt = new FormFieldValueGenerator.Builder().acsFieldName("cusDactt").adobeDataType("string").fieldName("cusDactt").build();
	static ValueGenerator<?> pref_scctt = new FormFieldValueGenerator.Builder().acsFieldName("cusScctt").adobeDataType("string").fieldName("cusScctt").build();

	static ValueGenerator<?> pref_cwgrw = new FormFieldValueGenerator.Builder().acsFieldName("cusCwgrw").adobeDataType("string").fieldName("cusCwgrw").build();
	static ValueGenerator<?> pref_dagrw = new FormFieldValueGenerator.Builder().acsFieldName("cusDagrw").adobeDataType("string").fieldName("cusDagrw").build();
	static ValueGenerator<?> pref_scgrw = new FormFieldValueGenerator.Builder().acsFieldName("cusScgrw").adobeDataType("string").fieldName("cusScgrw").build();

	static ValueGenerator<?> pref_cwhbs = new FormFieldValueGenerator.Builder().acsFieldName("cusCwhbs").adobeDataType("string").fieldName("cusCwhbs").build();
	static ValueGenerator<?> pref_dahbs = new FormFieldValueGenerator.Builder().acsFieldName("cusDahbs").adobeDataType("string").fieldName("cusDahbs").build();
	static ValueGenerator<?> pref_schbs = new FormFieldValueGenerator.Builder().acsFieldName("cusSchbs").adobeDataType("string").fieldName("cusSchbs").build();

	static ValueGenerator<?> pref_cwmrr = new FormFieldValueGenerator.Builder().acsFieldName("cusCwmrr").adobeDataType("string").fieldName("cusCwmrr").build();
	static ValueGenerator<?> pref_damrr = new FormFieldValueGenerator.Builder().acsFieldName("cusDamrr").adobeDataType("string").fieldName("cusDamrr").build();
	static ValueGenerator<?> pref_scmrr = new FormFieldValueGenerator.Builder().acsFieldName("cusScmrr").adobeDataType("string").fieldName("cusScmrr").build();

	static ValueGenerator<?> pref_cwtrn = new FormFieldValueGenerator.Builder().acsFieldName("cusCwtrn").adobeDataType("string").fieldName("cusCwtrn").build();
	static ValueGenerator<?> pref_datrn = new FormFieldValueGenerator.Builder().acsFieldName("cusDatrn").adobeDataType("string").fieldName("cusDatrn").build();
	static ValueGenerator<?> pref_sctrn = new FormFieldValueGenerator.Builder().acsFieldName("cusSctrn").adobeDataType("string").fieldName("cusSctrn").build();

	static ValueGenerator<?> pref_cwtdc = new FormFieldValueGenerator.Builder().acsFieldName("cusCwtdc").adobeDataType("string").fieldName("cusCwtdc").build();
	static ValueGenerator<?> pref_datdc = new FormFieldValueGenerator.Builder().acsFieldName("cusDatdc").adobeDataType("string").fieldName("cusDatdc").build();
	static ValueGenerator<?> pref_sctdc = new FormFieldValueGenerator.Builder().acsFieldName("cusSctdc").adobeDataType("string").fieldName("cusSctdc").build();

	static ValueGenerator<?> pref_cwtin = new FormFieldValueGenerator.Builder().acsFieldName("cusCwtind").adobeDataType("string").fieldName("cusCwtind").build();
	static ValueGenerator<?> pref_datin = new FormFieldValueGenerator.Builder().acsFieldName("cusDatind").adobeDataType("string").fieldName("cusDatind").build();
	static ValueGenerator<?> pref_sctin = new FormFieldValueGenerator.Builder().acsFieldName("cusSctind").adobeDataType("string").fieldName("cusSctind").build();

	static ValueGenerator<?> pref_cwasy = new FormFieldValueGenerator.Builder().acsFieldName("cusCwasy").adobeDataType("string").fieldName("cusCwasy").build();
	static ValueGenerator<?> pref_daasy = new FormFieldValueGenerator.Builder().acsFieldName("cusDaasy").adobeDataType("string").fieldName("cusDaasy").build();
	static ValueGenerator<?> pref_scasy = new FormFieldValueGenerator.Builder().acsFieldName("cusScasy").adobeDataType("string").fieldName("cusScasy").build();

	static ValueGenerator<?> pref_cwdsc = new FormFieldValueGenerator.Builder().acsFieldName("cusCwdsc").adobeDataType("string").fieldName("cusCwdsc").build();
	static ValueGenerator<?> pref_dadsc = new FormFieldValueGenerator.Builder().acsFieldName("cusDadsc").adobeDataType("string").fieldName("cusDadsc").build();
	static ValueGenerator<?> pref_scdsc = new FormFieldValueGenerator.Builder().acsFieldName("cusScdsc").adobeDataType("string").fieldName("cusScdsc").build();

	static ValueGenerator<?> pref_cwsci = new FormFieldValueGenerator.Builder().acsFieldName("cusCwsci").adobeDataType("string").fieldName("cusCwsci").build();
	static ValueGenerator<?> pref_dasci = new FormFieldValueGenerator.Builder().acsFieldName("cusDasci").adobeDataType("string").fieldName("cusDasci").build();
	static ValueGenerator<?> pref_scsci = new FormFieldValueGenerator.Builder().acsFieldName("cusScsci").adobeDataType("string").fieldName("cusScsci").build();

	static ValueGenerator<?> pref_cwasx = new FormFieldValueGenerator.Builder().acsFieldName("cusCwasx").adobeDataType("string").fieldName("cusCwasx").build();
	static ValueGenerator<?> pref_daasx = new FormFieldValueGenerator.Builder().acsFieldName("cusDaasx").adobeDataType("string").fieldName("cusDaasx").build();
	static ValueGenerator<?> pref_scasx = new FormFieldValueGenerator.Builder().acsFieldName("cusScasx").adobeDataType("string").fieldName("cusScasx").build();

	static ValueGenerator<?> pref_cwfsm = new FormFieldValueGenerator.Builder().acsFieldName("cusCwfsm").adobeDataType("string").fieldName("cusCwfsm").build();
	static ValueGenerator<?> pref_dafsm = new FormFieldValueGenerator.Builder().acsFieldName("cusDafsm").adobeDataType("string").fieldName("cusDafsm").build();
	static ValueGenerator<?> pref_scfsm = new FormFieldValueGenerator.Builder().acsFieldName("cusScfsm").adobeDataType("string").fieldName("cusScfsm").build();



	static PageField<?> emailField = buildField("email", false, true, "", new ValidateEmail("email", true, true, false, null, null));
	static PageField<?> firstNameField = buildField("firstName", false, false, "", new ValidateString("firstName", false, 0, 50, ""));
	static PageField<?> lastNameField = buildField("lastName", false, false, "", new ValidateString("lastName", false, 0, 50, ""));
	static PageField<?> birthDateField = buildField("birthDate", true, false, "", new ValidateString("birthDate", false, 0, 19, "(^$|[0-9]{4}-[0-9]{2}-[0-9]{2})")); //  YYYY-MM-DDT00:00:00



	static PageField<?> countryField = buildField("location.countryCode", false, false, "", new ValidateString("location.countryCode", false, 0, 50, ""));



	static PageField<?> cat_aircraftField = buildField("cusCat_aircraft", false, false, "", new ValidateString("cusCat_aircraft", false, 0, 3, ""));
	static PageField<?> cat_armorsField = buildField("cusCat_armors", false, false, "", new ValidateString("cusCat_armors", false, 0, 3, ""));
	static PageField<?> cat_astronomyField = buildField("cusCat_astronomy", false, false, "", new ValidateString("cusCat_astronomy", false, 0, 3, ""));
	static PageField<?> cat_automobilesField = buildField("cusCat_automobiles", false, false, "", new ValidateString("cusCat_automobiles", false, 0, 3, ""));
	static PageField<?> cat_beginnerField = buildField("cusCat_beginner", false, false, "", new ValidateString("cusCat_beginner", false, 0, 3, ""));
	static PageField<?> cat_biologyField = buildField("cusCat_biology", false, false, "", new ValidateString("cusCat_biology", false, 0, 3, ""));
	static PageField<?> cat_chemistryField = buildField("cusCat_chemistry", false, false, "", new ValidateString("cusCat_chemistry", false, 0, 3, ""));
	static PageField<?> cat_deepskyobjectsField = buildField("cusCat_deepskyobjects", false, false, "", new ValidateString("cusCat_deepskyobjects", false, 0, 3, ""));
	static PageField<?> cat_environmentField = buildField("cusCat_environment", false, false, "", new ValidateString("cusCat_environment", false, 0, 3, ""));
	static PageField<?> cat_exoticobjectsField = buildField("cusCat_exoticobjects", false, false, "", new ValidateString("cusCat_exoticobjects", false, 0, 3, ""));
	static PageField<?> cat_experttipsField = buildField("cusCat_experttips", false, false, "", new ValidateString("cusCat_experttips", false, 0, 3, ""));
	static PageField<?> cat_figuresField = buildField("cusCat_figures", false, false, "", new ValidateString("cusCat_figures", false, 0, 3, ""));
	static PageField<?> cat_freightrailField = buildField("cusCat_freightrail", false, false, "", new ValidateString("cusCat_freightrail", false, 0, 3, ""));
	static PageField<?> cat_healthField = buildField("cusCat_health", false, false, "", new ValidateString("cusCat_health", false, 0, 3, ""));
	static PageField<?> cat_howtoField = buildField("cusCat_howto", false, false, "", new ValidateString("cusCat_howto", false, 0, 3, ""));
	static PageField<?> cat_layoutsField = buildField("cusCat_layouts", false, false, "", new ValidateString("cusCat_layouts", false, 0, 3, ""));
	static PageField<?> cat_mindField = buildField("cusCat_mind", false, false, "", new ValidateString("cusCat_mind", false, 0, 3, ""));
	static PageField<?> cat_observingField = buildField("cusCat_observing", false, false, "", new ValidateString("cusCat_observing", false, 0, 3, ""));
	static PageField<?> cat_oursolarsystemField = buildField("cusCat_oursolarsystem", false, false, "", new ValidateString("cusCat_oursolarsystem", false, 0, 3, ""));
	static PageField<?> cat_passengerrailField = buildField("cusCat_passengerrail", false, false, "", new ValidateString("cusCat_passengerrail", false, 0, 3, ""));
	static PageField<?> cat_photosField = buildField("cusCat_photos", false, false, "", new ValidateString("cusCat_photos", false, 0, 3, ""));
	static PageField<?> cat_physicsField = buildField("cusCat_physics", false, false, "", new ValidateString("cusCat_physics", false, 0, 3, ""));
	static PageField<?> cat_planetearthField = buildField("cusCat_planetearth", false, false, "", new ValidateString("cusCat_planetearth", false, 0, 3, ""));
	static PageField<?> cat_railfanningField = buildField("cusCat_railfanning", false, false, "", new ValidateString("cusCat_railfanning", false, 0, 3, ""));
	static PageField<?> cat_railroadindustryField = buildField("cusCat_railroadindustry", false, false, "", new ValidateString("cusCat_railroadindustry", false, 0, 3, ""));
	static PageField<?> cat_railroadstoriesField = buildField("cusCat_railroadstories", false, false, "", new ValidateString("cusCat_railroadstories", false, 0, 3, ""));
	static PageField<?> cat_scifiField = buildField("cusCat_scifi", false, false, "", new ValidateString("cusCat_scifi", false, 0, 3, ""));
	static PageField<?> cat_shipsField = buildField("cusCat_ships", false, false, "", new ValidateString("cusCat_ships", false, 0, 3, ""));
	static PageField<?> cat_spaceexplorationField = buildField("cusCat_spaceexploration", false, false, "", new ValidateString("cusCat_spaceexploration", false, 0, 3, ""));
	static PageField<?> cat_spacenewsField = buildField("cusCat_spacenews", false, false, "", new ValidateString("cusCat_spacenews", false, 0, 3, ""));
	static PageField<?> cat_technologyField = buildField("cusCat_technology", false, false, "", new ValidateString("cusCat_technology", false, 0, 3, ""));
	static PageField<?> cat_trackplansField = buildField("cusCat_trackplans", false, false, "", new ValidateString("cusCat_trackplans", false, 0, 3, ""));
	static PageField<?> cat_trainbasicsField = buildField("cusCat_trainbasics", false, false, "", new ValidateString("cusCat_trainbasics", false, 0, 3, ""));
	static PageField<?> cat_videosField = buildField("cusCat_videos", false, false, "", new ValidateString("cusCat_videos", false, 0, 3, ""));

	static PageField<?> cwctrField = buildField("cusCwctr", true, false, "", null);
	static PageField<?> dactrField = buildField("cusDactr", true, false, "", null);
	static PageField<?> scctrField = buildField("cusScctr", true, false, "", null);

	static PageField<?> cwcttField = buildField("cusCwctt", true, false, "", null);
	static PageField<?> dacttField = buildField("cusDactt", true, false, "", null);
	static PageField<?> sccttField = buildField("cusScctt", true, false, "", null);

	static PageField<?> cwgrwField = buildField("cusCwgrw", true, false, "", null);
	static PageField<?> dagrwField = buildField("cusDagrw", true, false, "", null);
	static PageField<?> scgrwField = buildField("cusScgrw", true, false, "", null);

	static PageField<?> cwhbsField = buildField("cusCwhbs", true, false, "", null);
	static PageField<?> dahbsField = buildField("cusDahbs", true, false, "", null);
	static PageField<?> schbsField = buildField("cusSchbs", true, false, "", null);

	static PageField<?> cwmrrField = buildField("cusCwmrr", true, false, "", null);
	static PageField<?> damrrField = buildField("cusDamrr", true, false, "", null);
	static PageField<?> scmrrField = buildField("cusScmrr", true, false, "", null);

	static PageField<?> cwtrnField = buildField("cusCwtrn", true, false, "", null);
	static PageField<?> datrnField = buildField("cusDatrn", true, false, "", null);
	static PageField<?> sctrnField = buildField("cusSctrn", true, false, "", null);

	static PageField<?> cwtdcField = buildField("cusCwtdc", true, false, "", null);
	static PageField<?> datdcField = buildField("cusDatdc", true, false, "", null);
	static PageField<?> sctdcField = buildField("cusSctdc", true, false, "", null);

	static PageField<?> cwtinField = buildField("cusCwtind", true, false, "", null);
	static PageField<?> datinField = buildField("cusDatind", true, false, "", null);
	static PageField<?> sctinField = buildField("cusSctind", true, false, "", null);

	static PageField<?> cwasyField = buildField("cusCwasy", true, false, "", null);
	static PageField<?> daasyField = buildField("cusDaasy", true, false, "", null);
	static PageField<?> scasyField = buildField("cusScasy", true, false, "", null);

	static PageField<?> cwdscField = buildField("cusCwdsc", true, false, "", null);
	static PageField<?> dadscField = buildField("cusDadsc", true, false, "", null);
	static PageField<?> scdscField = buildField("cusScdsc", true, false, "", null);

	static PageField<?> cwsciField = buildField("cusCwsci", true, false, "", null);
	static PageField<?> dasciField = buildField("cusDasci", true, false, "", null);
	static PageField<?> scsciField = buildField("cusScsci", true, false, "", null);

	static PageField<?> cwasxField = buildField("cusCwasx", true, false, "", null);
	static PageField<?> daasxField = buildField("cusDaasx", true, false, "", null);
	static PageField<?> scasxField = buildField("cusScasx", true, false, "", null);

	static PageField<?> cwfsmField = buildField("cusCwfsm", true, false, "", null);
	static PageField<?> dafsmField = buildField("cusDafsm", true, false, "", null);
	static PageField<?> scfsmField = buildField("cusScfsm", true, false, "", null);

	static PageField<?> cusCwbirField = buildField("cusCwbir", true, false, "", null);
	static PageField<?> cusDabirField = buildField("cusDabir", true, false, "", null);
	static PageField<?> cusScbirField = buildField("cusScbir", true, false, "", null);


	static PageField<?> customerIdHash = buildField("cusCustomerIdHash", true, false, "", null);
	static PageField<?> customerUUID =	buildField("cusCustomerUUID", true, false, "", null);


//	static TriggeredEmailConfig emailConfig = new TriggeredEmailConfig.Builder()
//			.eventIds(
//					Map.of("dollartree", "EVT_signupWelcome"))
//			.email(
//					email)
//			.contextField(
//					new FormFieldValueGenerator.Builder()
//							.acsFieldName("customerIdHash")
//							.adobeDataType("string")
//							.fieldName("cusCustomerIdHash")
//							.build()
//			)
//			.build();





	public static AppDetails preference() {

		AppPage lookup = AppPage.builder("lookup", PageType.ENTRY_POINT)
				.controller(Controller.GET_PROFILE_SECURELINK)
				.successNavigation("index")
				.failureNavigation("signup")
				.build();

		AppPage signup = AppPage.builder("signup",PageType.ENTRY_POINT)
				.controller(Controller.CREATE_PROFILE)
				.successNavigation("confirm")
				.failureNavigation("confirm")

				.serviceField(buildServiceField("scienceOptIn", "science", "Y",
						"", true))
				.serviceField(buildServiceField("hobbyOptIn", "hobby", "Y",
						"", true))

				.fields(Arrays.asList(emailField,firstNameField,lastNameField,birthDateField,countryField,cusCwbirField,cusDabirField,cusScbirField,

						cat_aircraftField,cat_armorsField,cat_astronomyField,cat_automobilesField,cat_beginnerField,cat_biologyField,cat_chemistryField,cat_deepskyobjectsField,cat_environmentField,cat_exoticobjectsField,cat_experttipsField,cat_figuresField,cat_freightrailField,cat_healthField,cat_howtoField,cat_layoutsField,cat_mindField,cat_observingField,cat_oursolarsystemField,cat_passengerrailField,cat_photosField,cat_physicsField,cat_planetearthField,cat_railfanningField,cat_railroadindustryField,cat_railroadstoriesField,cat_scifiField,cat_shipsField,cat_spaceexplorationField,cat_spacenewsField,cat_technologyField,cat_trackplansField,cat_trainbasicsField,cat_videosField,
						cwctrField,cwcttField,cwgrwField,cwhbsField,cwmrrField,cwtrnField,cwtdcField,cwtinField,cwasyField,cwdscField,cwsciField,cwasxField,cwfsmField,
						dactrField,dacttField,dagrwField,dahbsField,damrrField,datrnField,datdcField,datinField,daasyField,dadscField,dasciField,daasxField,dafsmField,
						scctrField,sccttField,scgrwField,schbsField,scmrrField,sctrnField,sctdcField,sctinField,scasyField,scdscField,scsciField,scasxField,scfsmField,
						customerIdHash,customerUUID
						))
				.defaultActions(Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,firstName,lastName,birthDate,country,subscribedToScience,subscribedToHobby,cusCwbir,cusDabir,cusScbir,
										cat_aircraft,cat_armors,cat_astronomy,cat_automobiles,cat_beginner,cat_biology,cat_chemistry,cat_deepskyobjects,cat_environment,cat_exoticobjects,cat_experttips,cat_figures,cat_freightrail,cat_health,cat_howto,cat_layouts,cat_mind,cat_observing,cat_oursolarsystem,cat_passengerrail,cat_photos,cat_physics,cat_planetearth,cat_railfanning,cat_railroadindustry,cat_railroadstories,cat_scifi,cat_ships,cat_spaceexploration,cat_spacenews,cat_technology,cat_trackplans,cat_trainbasics,cat_videos,
										cwctr,cwctt,cwgrw,cwhbs,cwmrr,cwtrn,cwtdc,cwtin,cwasy,cwdsc,cwsci,cwasx,cwfsm,
										dactr,dactt,dagrw,dahbs,damrr,datrn,datdc,datin,daasy,dadsc,dasci,daasx,dafsm,
										scctr,scctt,scgrw,schbs,scmrr,sctrn,sctdc,sctin,scasy,scdsc,scsci,scasx,scfsm
										)
						),
						new ServicesActionProcessor()

				))
				.build();

		AppPage index = AppPage.builder("index",PageType.PROFILE)
				.controller(Controller.UPDATE_SERVICES)
				.successNavigation("confirm")
				.failureNavigation("error")

				.serviceField(buildServiceField("scienceOptIn", "science", "Y",
						"", true))
				.serviceField(buildServiceField("hobbyOptIn", "hobby", "Y",
						"", true))

				.fields(Arrays.asList(emailField,firstNameField,lastNameField,birthDateField,countryField,cusCwbirField,cusDabirField,cusScbirField,
						cat_aircraftField,cat_armorsField,cat_astronomyField,cat_automobilesField,cat_beginnerField,cat_biologyField,cat_chemistryField,cat_deepskyobjectsField,cat_environmentField,cat_exoticobjectsField,cat_experttipsField,cat_figuresField,cat_freightrailField,cat_healthField,cat_howtoField,cat_layoutsField,cat_mindField,cat_observingField,cat_oursolarsystemField,cat_passengerrailField,cat_photosField,cat_physicsField,cat_planetearthField,cat_railfanningField,cat_railroadindustryField,cat_railroadstoriesField,cat_scifiField,cat_shipsField,cat_spaceexplorationField,cat_spacenewsField,cat_technologyField,cat_trackplansField,cat_trainbasicsField,cat_videosField,
						cwctrField,cwcttField,cwgrwField,cwhbsField,cwmrrField,cwtrnField,cwtdcField,cwtinField,cwasyField,cwdscField,cwsciField,cwasxField,cwfsmField,
						dactrField,dacttField,dagrwField,dahbsField,damrrField,datrnField,datdcField,datinField,daasyField,dadscField,dasciField,daasxField,dafsmField,
						scctrField,sccttField,scgrwField,schbsField,scmrrField,sctrnField,sctdcField,sctinField,scasyField,scdscField,scsciField,scasxField,scfsmField,
						customerIdHash,customerUUID
				))
				.defaultActions(Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,firstName,lastName,birthDate,country,subscribedToScience,subscribedToHobby,cusCwbir,cusDabir,cusScbir,
										pref_cat_aircraft,pref_cat_armors,pref_cat_astronomy,pref_cat_automobiles,pref_cat_beginner,pref_cat_biology,pref_cat_chemistry,pref_cat_deepskyobjects,pref_cat_environment,pref_cat_exoticobjects,pref_cat_experttips,pref_cat_figures,pref_cat_freightrail,pref_cat_health,pref_cat_howto,pref_cat_layouts,pref_cat_mind,pref_cat_observing,pref_cat_oursolarsystem,pref_cat_passengerrail,pref_cat_photos,pref_cat_physics,pref_cat_planetearth,pref_cat_railfanning,pref_cat_railroadindustry,pref_cat_railroadstories,pref_cat_scifi,pref_cat_ships,pref_cat_spaceexploration,pref_cat_spacenews,pref_cat_technology,pref_cat_trackplans,pref_cat_trainbasics,pref_cat_videos,
										pref_cwctr,pref_cwctt,pref_cwgrw,pref_cwhbs,pref_cwmrr,pref_cwtrn,pref_cwtdc,pref_cwtin,pref_cwasy,pref_cwdsc,pref_cwsci,pref_cwasx,pref_cwfsm,
										pref_dactr,pref_dactt,pref_dagrw,pref_dahbs,pref_damrr,pref_datrn,pref_datdc,pref_datin,pref_daasy,pref_dadsc,pref_dasci,pref_daasx,pref_dafsm,
										pref_scctr,pref_scctt,pref_scgrw,pref_schbs,pref_scmrr,pref_sctrn,pref_sctdc,pref_sctin,pref_scasy,pref_scdsc,pref_scsci,pref_scasx,pref_scfsm
								)
						),
						new ServicesActionProcessor()

				))
				.build();






		AppPage testSignup = AppPage.builder("test_signup",PageType.ENTRY_POINT)
				.controller(Controller.CREATE_PROFILE)
				.successNavigation("confirm")
				.failureNavigation("error")
				.serviceField(buildServiceField("scienceOptIn", "science", "Y",
						"", true))
				.serviceField(buildServiceField("hobbyOptIn", "hobby", "Y",
						"", true))
				.fields(Arrays.asList(emailField,
						cat_aircraftField,cat_armorsField,
						cwctrField,cwcttField,
						dactrField,dacttField,
						scctrField,sccttField
				))
				.defaultActions(Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										cat_aircraft,
										cat_armors,
										cwctr,
										dactr,
										scctr,
										cwctt,
										dactt,
										scctt
								)
						),
						new ServicesActionProcessor()

				))
				.build();

		AppPage testPref = AppPage.builder("test_pref",PageType.ENTRY_POINT)
				.controller(Controller.CREATE_PROFILE)
				.successNavigation("confirm")
				.failureNavigation("error")
				.serviceField(buildServiceField("scienceOptIn", "science", "Y",
						"", true))
				.serviceField(buildServiceField("hobbyOptIn", "hobby", "Y",
						"", true))
				.fields(Arrays.asList(emailField,
						cat_aircraftField,cat_armorsField,
						cwctrField,cwcttField,
						dactrField,dacttField,
						scctrField,sccttField
				))
				.defaultActions(Arrays.asList(
						new ProfileActionProcessor(
								List.of(
										email,
										pref_cat_aircraft,
										pref_cat_armors,
										pref_cwctr,
										pref_dactr,
										pref_scctr,
										pref_cwctt,
										pref_dactt,
										pref_scctt
								)
						),
						new ServicesActionProcessor()

				))
				.build();



//		AppPage index = AppPage.builder("index", PageType.ENTRY_POINT)
//				.controller(Controller.GET_PROFILE_CUSTOMERID)
//				.successNavigation("confirm")
//				.failureNavigation("confirm")
//				.fields(Arrays.asList(at_
//						buildField("subscribe", false, false, "",
//								new ValidateString("subscribe", true, null, null, "")),
//						buildField("email", false, true, "",
//								new ValidateEmail("email", true, true, false, null, null)),
//
//
//						buildField("zip", false, false, "",
//								new ValidateString("zip", true, 5, 5, "[0-9]{:min,:max}")),
//						buildField("cusCraftOrg", false, false, "",
//								new ValidateString("cusCraftOrg", false, 0, 5, "")),
//						buildField("floristOrg", false, false, "",
//								new ValidateString("floristOrg", false, 0, 5, "")),
//						buildField("educationOrg", false, false, "",
//								new ValidateString("educationOrg", false, 0, 5, "")),
//						buildField("nonprofitOrg", false, false, "",
//								new ValidateString("nonprofitOrg", false, 0, 5, "")),
//						buildField("restaurantOrg", false, false, "",
//								new ValidateString("restaurantOrg", false, 0, 5, "")),
//						buildField("cateringOrg", false, false, "",
//								new ValidateString("cateringOrg", false, 0, 5, ""))
//
//				))
//				.hiddenService(buildService("DTmarketing", ServiceAction.ADD))
//				.conditionalAction("Not profileExists",Arrays.asList(
//						new ProfileActionProcessor(
//								List.of(
//										email,
//										firstName,
//										lastName
//								)
//						)
//						,new ServicesActionProcessor()
//	//					,new TriggerEmailActionProcessor(emailConfig)
//				)).conditionalAction("Not subscribedToDTMarketing",Arrays.asList(
//						new ProfileActionProcessor(
//								List.of(
//										email,
//										firstName,
//										lastName
//								)
//						)
//						,new ServicesActionProcessor()
//	//					,new TriggerEmailActionProcessor(emailConfig)
//				)).conditionalAction("subscribedToDTMarketing",Arrays.asList(
//						new ProfileActionProcessor(
//								List.of(
//										email,
//										firstName,
//										lastName
//								)
//						)
//				))
//				.build();

		AppPage confirm = AppPage.builder("confirm", PageType.PAGE)
				.controller(Controller.PROFILE_CONFIRM)
				.fields(Collections.singletonList(
						buildField("email", true, false, "", null)
				))
				.build();

		return AppDetails.builder(KALMBACH_NM, "preferences")
				.enable()

				.addPage(lookup)
				.addPage(signup)
				.addPage(index)
				.addPage(confirm)

				.addPage(testSignup)
				.addPage(testPref)


				.build();
	}



}
