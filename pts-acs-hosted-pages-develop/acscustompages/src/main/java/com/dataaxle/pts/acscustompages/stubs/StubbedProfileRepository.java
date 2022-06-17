package com.dataaxle.pts.acscustompages.stubs;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_ID;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_ID_HASH;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_UUID;

import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.CurrentService;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class StubbedProfileRepository {

	private static final Table<String, String, ProfileResponse> profiles = HashBasedTable.create();

	private static final Table<String, String, AcsService> services = HashBasedTable.create();

	private static final Map<String, List<String>> customerIdOrderMap = new HashMap<>();

	static {
		createServices();
		customerIdSetUp();
		createProfiles();
		logProfiles();
	}

	public static ProfileResponse search(String company, Map<String, String> searchFields) {
		String customerIdHash = hashCustomerId(getCustomerId(company, searchFields));
		return getByCustomerIdHash(company, customerIdHash);
	}

	public static ProfileResponse find(String company, Map<String, String> searchFields) {
		Predicate<ProfileResponse> findProfile = profileResponse ->
			searchFields.entrySet().stream()
				.map(entry -> profileResponse.getProfile().getField(entry.getKey()).orElse("")
					.equals(entry.getValue()))
				.reduce(Boolean::logicalAnd)
				.orElse(false);
		;
		return profiles.row(company).values().stream()
			.filter(findProfile)
			.findFirst()
			.orElseThrow(() -> new ResourceNotFoundException(searchFields.toString()));
	}

	public static ProfileResponse getByCustomerIdHash(String company, String customerIdHash) {
		if (profiles.contains(company, customerIdHash)) {
			return profiles.get(company, customerIdHash);
		}
		throw new ResourceNotFoundException(customerIdHash);
	}

	public static CurrentService getCurrentService(String company, String serviceName) {
		AcsService acsService = services.get(company, serviceName);
		return new CurrentService(acsService.getName(), acsService.getLabel(), LocalDateTime.now());
	}

	public static String getCustomerIdHash(String company, Map<String, String> searchFields) {
		return hashCustomerId(getCustomerId(company, searchFields));
	}
	public static ProfileResponse createProfile(String company, Map<String, Object> profileFields) {
		return createProfile(company, profileFields, Collections.emptyList());
	}

	public static ProfileResponse createProfile(String company, Map<String, Object> profileFields, List<String> services) {
		Profile profile = new Profile();
		profileFields.forEach(profile::addField);
		profile.addField("PKey", UUID.randomUUID().toString());
		String acsId = UUID.randomUUID().toString();
		profile.addField("acsId", acsId);
		profile.addField("created", LocalDateTime.now().toString());
		Map<String, String> customerIdFields = customerIdOrderMap.get(company).stream()
												   .collect(Collectors.toMap(name -> name,
													   name -> (String) profileFields.getOrDefault(name, "")));
		String customerId = getCustomerId(company, customerIdFields);
		profile.addField(CUS_CUSTOMER_ID, customerId);
		String customerIdHash = hashCustomerId(customerId);
		profile.addField(CUS_CUSTOMER_ID_HASH, customerIdHash);
		String customerUuid = profileFields.containsKey(CUS_CUSTOMER_UUID) ? (String)profileFields.get(CUS_CUSTOMER_UUID) :
								  UUID.randomUUID().toString();
		profile.addField(CUS_CUSTOMER_UUID, customerUuid);
		ProfileResponse profileResponse = new ProfileResponse();
		profileResponse.setProfile(profile);
		List<CurrentService> currentServices = services.stream()
												   .map(name -> getCurrentService(company, name))
												   .collect(Collectors.toList());
		profileResponse.addCurrentServices(currentServices);
		profiles.put(company, customerIdHash, profileResponse);
		LOG.info("Created profile: customerId: {}, acsId: {}, customerIdHash: {}", customerId, acsId, customerIdHash);
		logProfile(profileResponse);
		return profileResponse;
	}

	public static ProfileResponse updateProfile(String company, String customerUniqueId, Map<String, Object> profileFields) {
		ProfileResponse profileResponse = profiles.get(company, customerUniqueId);
		Profile profile = profileResponse.getProfile();
		profileFields.forEach(profile::addField);
		logProfile(profileResponse);
		return profileResponse;
	}

	public static ProfileResponse createOrUpdateProfile(String company, String customerUniqueId,
														Map<String, Object> profileFields) {
		if (profiles.contains(company, customerUniqueId)) {
			return updateProfile(company, customerUniqueId, profileFields);
		}
		return createProfile(company, profileFields);
	}

	private static String getCustomerId(String company, Map<String, String> fields) {
		List<String> customerIdOrder = customerIdOrderMap.get(company);
		return customerIdOrder.stream()
				   .map(fields::get)
				   .collect(Collectors.joining("|"));
	}

	private static String hashCustomerId(String customerId) {
		return Hashing.sha256().hashString(customerId, StandardCharsets.UTF_8).toString();
	}

	private static void createProfiles() {
		addProfile("cfna", cfna_charlesb1());
		addProfile("dillards",dillards_adhoc_unsub_ak());
		addProfile("dillards",dillards_unsub_ak());
		addProfile("dollartree", dollartree_charlesb1());
		addProfile("kalmbachmedia", kalmbachmedia_charlesb());
		addProfile("kodakmoments", kodakmoments_charlesb());

		addProfile("mcrewardspoland",mcrewards_poland_ak());
		addProfile("mcrewardsbrazil",mcrewards_brazil_ak());

		addProfile("resideo", resideo_charlesb1());
		addProfile("resideo", resideo_charlesb2());
		addProfile("resideo", resideo_charlesb3());
		addProfile("resideo", resideo_error());
		addProfile("usbank", usbank_acg_charlesb1());
		addProfile("usbank", usbank_acg_charlesb2());
		addProfile("usbank", usbank_bmw_charlesb1());
		addProfile("usbank", usbank_bmw_charlesb2());
		addProfile("usbank", usbank_fid_charlesb1());
		addProfile("ymnewsolutions", ymnewsolutions_charlesb1());

		addAllKalmbachBegginnerUsers();
	}

	private static ProfileResponse cfna_charlesb1() {
		String company = "cfna";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charlesb@yesmail.com");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		profileFields.put("cusCustomerUUID", "1b19fb53-1a4b-4a28-8f2d-95141f4e4991");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
			getCurrentService(company, "marketing")
		)));
		return profileResponse;

	}

	private static ProfileResponse dillards_adhoc_unsub_ak() {
		String company = "dillards";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "andreyk@yesmail.com");
		profileFields.put("firstName", "Andrey");
		profileFields.put("lastName", "Kolesnik");
		profileFields.put(CUS_CUSTOMER_UUID, "10024");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
				getCurrentService(company, "adhoc")
		)));
		return profileResponse;
	}

	private static ProfileResponse dillards_unsub_ak() {
		String company = "dillards";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "andreyk2@yesmail.com");
		profileFields.put("firstName", "Andrey");
		profileFields.put("lastName", "Kolesnik");
		profileFields.put(CUS_CUSTOMER_UUID, "10025");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
				getCurrentService(company, "marketing")
		)));
		return profileResponse;
	}

	public static void addAllKalmbachBegginnerUsers(){
		addKalmbachBeginnerUsers("asy", "science", 100);
		addKalmbachBeginnerUsers("ctt", "hobby", 200);
		addKalmbachBeginnerUsers("fsm", "hobby", 300);
		addKalmbachBeginnerUsers("mrr", "hobby", 400);
	}

	public static void addKalmbachBeginnerUsers(String Brnd, String service, Integer init){
		// Existing New to Service
		addProfile("kalmbachmedia", kalmbachmedia_beginner(Brnd,null,null, null,false,init+""));

		// Unsubscribed
		addProfile("kalmbachmedia", kalmbachmedia_beginner(Brnd,null,null, null,true,++init+""));

		// Unsubscribed 2
		addProfile("kalmbachmedia", kalmbachmedia_beginner(Brnd,"Yes","No", null,true,++init+""));

		// Unsubscribed 3
		addProfile("kalmbachmedia", kalmbachmedia_beginner(Brnd,"Yes","Yes", null,true,++init+""));

		// Subscribed and done
		addProfile("kalmbachmedia", kalmbachmedia_beginner(Brnd,"Yes","Yes", service,true,++init+""));

		// Subscribed and done is empty
		addProfile("kalmbachmedia", kalmbachmedia_beginner(Brnd,"Yes","", service,true,++init+""));

		// Subscribed and done is null
		addProfile("kalmbachmedia", kalmbachmedia_beginner(Brnd,"Yes",null, service,true,++init+""));

		// Subscribed and not done
		addProfile("kalmbachmedia", kalmbachmedia_beginner(Brnd,"Yes","No", service,true,++init+""));

		// Subscribed and not done 2 (case ins)
		addProfile("kalmbachmedia", kalmbachmedia_beginner(Brnd,"Yes","no", service,true,++init+""));

	}

	public static void addProfile(String company, ProfileResponse profileResponse) {
		String customerIdHash = (String) profileResponse.getField(CUS_CUSTOMER_ID_HASH);
		profiles.put(company, customerIdHash, profileResponse);
	}

	private static void addService(String company, String service, String label) {
		services.put(company, service, new AcsService(service, label));
	}

	private static ProfileResponse dollartree_charlesb1() {
		String company = "dollartree";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charlesb@yesmail.com");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
			getCurrentService(company, "DTmarketing")
		)));
		return profileResponse;
	}

	private static ProfileResponse kalmbachmedia_beginner(String brand, String start, String done, String service, boolean subscribed, String uniqueNum) {
		String company = "kalmbachmedia";
		String email = brand+"_kalmbach_"+uniqueNum+"@testc.com";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email",email);
		profileFields.put("firstName", "And");
		profileFields.put("lastName", "Kol");
		profileFields.put(CUS_CUSTOMER_UUID, uniqueNum);

		if(start!=null)profileFields.put("cusBgn"+brand, start);
		if(done!=null)profileFields.put("cusBgn_"+brand, done);

		//if("hobby".equals(service))
			profileFields.put("cusEverSubHobby", subscribed);
		//if("science".equals(service))
			profileFields.put("cusEverSubScience", subscribed);

		ProfileResponse profileResponse = createProfile(company, profileFields);

		if(service!=null) profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(getCurrentService(company, service))));

		return profileResponse;
	}


	private static ProfileResponse kalmbachmedia_charlesb() {
		String company = "kalmbachmedia";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charlesb@yesmail.com");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		profileFields.put(CUS_CUSTOMER_UUID, "9275f07c-90c5-4c5d-8d24-3a9fb6e06a8a");
		profileFields.put("cusInt_dsc_health", "Yes");
		profileFields.put("cusInt_dsc_mind", "Yes");
		profileFields.put("cusInt_dsc_stem", "Yes");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
			getCurrentService(company, "science")
		/*ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
			getCurrentService(company, "hobby")*/
		)));
		return profileResponse;
	}

	private static ProfileResponse kodakmoments_charlesb() {
		String company = "kodakmoments";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charlesb@yesmail.com");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
			getCurrentService(company, "marketing")
		)));
		return profileResponse;
	}

	private static ProfileResponse mcrewards_poland_ak() {
		String company = "mcrewardspoland";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "andreyk@yesmail.com");
		profileFields.put("firstName", "Andrey");
		profileFields.put("lastName", "Kolesnik");
		profileFields.put("cusMemberICA","14019");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
				getCurrentService(company, "mcRewardsPoland")
		)));
		return profileResponse;
	}

	private static ProfileResponse mcrewards_brazil_ak() {
		String company = "mcrewardsbrazil";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "andreyk@yesmail.com");
		profileFields.put("firstName", "Andrey");
		profileFields.put("lastName", "Kolesnik");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
				getCurrentService(company, "mastercardBrazil")
		)));
		return profileResponse;
	}

	private static ProfileResponse resideo_charlesb1() {
		String company = "resideo";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charlesb@yesmail.com");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
			getCurrentService(company, "honeywellhomeemails")
		)));
		return profileResponse;
	}

	private static ProfileResponse resideo_charlesb2() {
		String company = "resideo";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charles.berger@yesmail.com");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.emptyList()));
		return profileResponse;
	}

	private static ProfileResponse resideo_charlesb3() {
		String company = "resideo";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charles.berger@yesmarketing.com");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Arrays.asList(
			getCurrentService(company, "honeywellhomeemails"),
			getCurrentService(company, "honeywellhomepresentedoffers"),
			getCurrentService(company, "honeywellhomeasks"),
			getCurrentService(company, "connectedhome")
		)));
		return profileResponse;
	}

	private static ProfileResponse resideo_error() {
		String company = "resideo";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "error@yesmail.com");
		profileFields.put("firstName", "Error");
		profileFields.put("lastName", "Profile");
		return createProfile(company, profileFields);
	}

	private static ProfileResponse usbank_acg_charlesb1() {
		String company = "usbank";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charlesb@yesmail.com");
		profileFields.put("cusAcctL4", "1234");
		profileFields.put("cusPartner", "ACG");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Arrays.asList(
			getCurrentService(company, "marketing"),
			getCurrentService(company, "service")
		)));
		return profileResponse;
	}

	private static ProfileResponse usbank_acg_charlesb2() {
		String company = "usbank";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charles.berger@yesmail.com");
		profileFields.put("cusAcctL4", "4321");
		profileFields.put("cusPartner", "ACG");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
			getCurrentService(company, "service")
		)));
		return profileResponse;
	}

	private static ProfileResponse usbank_bmw_charlesb1() {
		String company = "usbank";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charlesb@yesmail.com");
		profileFields.put("cusAcctL4", "1234");
		profileFields.put("cusPartner", "BMW");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Arrays.asList(
			getCurrentService(company, "marketing"),
			getCurrentService(company, "service")
		)));
		return profileResponse;
	}

	private static ProfileResponse usbank_bmw_charlesb2() {
		String company = "usbank";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charles.berger@yesmail.com");
		profileFields.put("cusAcctL4", "4321");
		profileFields.put("cusPartner", "BMW");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Collections.singletonList(
			getCurrentService(company, "service")
		)));
		return profileResponse;
	}

	private static ProfileResponse usbank_fid_charlesb1() {
		String company = "usbank";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charlesb@yesmail.com");
		profileFields.put("cusAcctL4", "1234");
		profileFields.put("cusPartner", "FID");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Arrays.asList(
			getCurrentService(company, "marketing"),
			getCurrentService(company, "service")
		)));
		return profileResponse;
	}

	private static ProfileResponse ymnewsolutions_charlesb1() {
		String company = "ymnewsolutions";
		Map<String, Object> profileFields = new HashMap<>();
		profileFields.put("email", "charles.berger@yesmarketing.com");
		profileFields.put("firstName", "Charles");
		profileFields.put("lastName", "Berger");
		profileFields.put("cusRewardsActive", "Y");
		profileFields.put("cusPurchaseOrderFlag", false);
		profileFields.put("cusShortNumber", (short) 0);
		ProfileResponse profileResponse = createProfile(company, profileFields);
		profileResponse.addCurrentServices(new ArrayList<>(Arrays.asList(
			getCurrentService(company, "yesMarketingAService"),
			getCurrentService(company, "yesMarketingBService")
		)));
		return profileResponse;
	}

	private static void createServices() {
		addService("cfna", "marketing", "Marketing");
		addService("cfna", "transactional", "Transactional");
		addService("dillards", "adhoc", "adhoc");
		addService("dillards", "marketing", "marketing");
		addService("dollartree", "DTmarketing", "DT Marketing");
		addService("kalmbachmedia", "hobby", "Hobby");
		addService("kalmbachmedia", "science", "Science");
		addService("kodakmoments", "marketing", "Marketing");
		addService("kodakmoments", "Member", "Member");
		addService("kodakmoments", "kmart-au", "Kmart-AU");

		addService("mcrewardspoland", "mcRewardsPoland", "MC Rewards Poland");
		addService("mcrewardsbrazil", "mastercardBrazil", "Mastercard Brazil");

		addService("resideo", "honeywellhomereminders", "Whole Home Reminders");
		addService("resideo", "honeywellhomeenergyreport", "Whole Home Energy Report");
		addService("resideo", "honeywellhomepartners", "Whole Home Partners");
		addService("resideo", "honeywellhomeemails", "Whole Home Emails");
		addService("resideo", "honeywellhomeasks", "Whole Home Feedback");
		addService("resideo", "honeywellhomepresentedoffers", "Whole Home Presented Offers");
		addService("resideo", "connectedhome", "Whole Home Support");
		addService("usbank", "marketing", "Marketing");
		addService("usbank", "service", "Service");
		addService("ymnewsolutions", "yesMarketingTransactionalA", "Transactional A");
		addService("ymnewsolutions", "yesMarketingAService", "Marketing A");
		addService("ymnewsolutions", "yesMarketingBService", "Marketing B");
	}

	private static void customerIdSetUp() {
		customerIdOrderMap.put("cfna", Collections.singletonList("email"));
		customerIdOrderMap.put("dillards", Collections.singletonList("email"));
		customerIdOrderMap.put("dollartree", Collections.singletonList("email"));
		customerIdOrderMap.put("kalmbachmedia", Collections.singletonList("email"));
		customerIdOrderMap.put("kodakmoments", Collections.singletonList("email"));

		customerIdOrderMap.put("mcrewardspoland", Collections.singletonList("email"));
		customerIdOrderMap.put("mcrewardsbrazil", Collections.singletonList("email"));

		customerIdOrderMap.put("resideo", Collections.singletonList("email"));
		customerIdOrderMap.put("usbank", Arrays.asList("email", "cusAcctL4", "cusPartner"));
		customerIdOrderMap.put("ymnewsolutions", Collections.singletonList("email"));
	}

	private static void logProfiles() {
		LOG.debug("Listing all profiles...");
		profiles.cellSet().forEach(cell -> {
			String company = cell.getRowKey();
			String customerIdHash = cell.getColumnKey();
			ProfileResponse profileResponse = cell.getValue();
			Profile profile = profileResponse.getProfile();
			String customerUUID = (String)profile.getField("cusCustomerUUID").orElse("");
			String customerId = (String) profile.getField(CUS_CUSTOMER_ID).orElse("");
			LOG.debug("company : {}, customerId: {}, customerUUID: {}, customerIdHash: {}", company, customerId, customerUUID, customerIdHash);
		});
		LOG.debug("End of list...");
	}

	private static void logProfile(ProfileResponse profileResponse) {
		LOG.debug("{}", profileResponse);
	}
}
