package com.yesmarketing.ptsacs.services.util;

import static com.yesmarketing.ptsacs.services.util.ServicesConstants.CUSTOMER_ID;
import static com.yesmarketing.ptsacs.services.util.ServicesConstants.CUSTOMER_ID_HASH;
import static com.yesmarketing.ptsacs.services.util.ServicesConstants.CUSTOMER_UUID;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmail.api.common.util.CommonMessageKeys;
import com.yesmarketing.acsapi.model.GeoUnit;
import com.yesmarketing.acsapi.model.Link;
import com.yesmarketing.acsapi.model.Location;
import com.yesmarketing.acsapi.model.OrgUnit;
import com.yesmarketing.acsapi.model.PostalAddress;
import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ProfileSubscriptionModel;
import com.yesmarketing.acsapi.model.ResponseModel;
import com.yesmarketing.acsapi.model.ServiceModel;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileModelTestHelper {

	private static final Table<String, String, ProfileModel> profileModels = HashBasedTable.create();

	private static final Table<String, String, List<ProfileSubscriptionModel>> profileSubscriptionModels = HashBasedTable.create();

	static {
		addProfileModel("ymnewsolutions", profileModel_ymnewsolutions_charlesb());
		addProfileModel("ymnewsolutions", profileModel_ymnewsolutions_cbym());
		addProfileModel("ymnewsolutions", profileModel_ymnewsolutions_charlesb11());
		addProfileModel("ymnewsolutions", profileModel_ymnewsolutions_andreyk());
		addProfileModel("musicnotes", profileModel_musicnotes_cbym());
		addProfileSubscriptionModel("ymnewsolutions", "charlesb11",
				Collections.singletonMap("yesMarketingAService", "Marketing A"));
	}

	public static ProfileModel getProfileModel(String company, String customerId) {
		if (profileModels.contains(company, customerId)) {
			return profileModels.get(company, customerId);
		}
		throw new ObjectNotFoundException(CommonMessageKeys.RESOURCE_NOT_FOUND_MESSAGE, new Object[]{company,
				customerId}, CommonMessageKeys.RESOURCE_NOT_FOUND_INFO, new Object[]{});
	}

	public static ProfileModel getProfileModelByCustomerIdHash(String company, String customerIdHash) {
		return profileModels
				.row(company)
				.values()
				.stream()
				.filter(pm -> pm.getAttribute(CUSTOMER_ID_HASH).orElse("").equals(customerIdHash))
				.findFirst()
				.orElseThrow(() -> new ObjectNotFoundException(CommonMessageKeys.RESOURCE_NOT_FOUND_MESSAGE, new Object[]{company,
						customerIdHash}, CommonMessageKeys.RESOURCE_NOT_FOUND_INFO, new Object[]{}));
	}

	public static ResponseModel<ProfileSubscriptionModel> getProfileSubscriptions(String company, String customerId) {
		ResponseModel<ProfileSubscriptionModel> rm = new ResponseModel<>();
		List<ProfileSubscriptionModel> psmList = profileSubscriptionModels.get(company, customerId);
		if (psmList == null) {
			psmList = Collections.emptyList();
		}
		rm.setContent(psmList);
		return rm;
	}

	private static void addProfileModel(String company, ProfileModel model) {
		profileModels.put(company, model.getCusCustomerId(), model);
	}

	private static void addProfileSubscriptionModel(String company, String customerId, Map<String, String> services) {
		services
				.entrySet()
				.stream()
				.map(entry -> createProfileSubscriptionModel(company, customerId, entry))
				.forEach(item -> {
					List<ProfileSubscriptionModel> psmList = profileSubscriptionModels.get(company, customerId);
					if (psmList == null) {
						psmList = new ArrayList<>();
					}
					psmList.add(item);
					profileSubscriptionModels.put(company, customerId, psmList);
				});
	}

	private static ProfileSubscriptionModel createProfileSubscriptionModel(String company, String customerId, Map.Entry<String, String> service) {
		ProfileSubscriptionModel psm = new ProfileSubscriptionModel();
		psm.setCreated(DateTime.now());
		ServiceModel sm = new ServiceModel();
		sm.setLabel(service.getValue());
		sm.setName(service.getKey());
		psm.setService(sm);
		psm.setSubscriber(getProfileModel(company, customerId));
		return psm;
	}

	private static ProfileModel profileModel_ymnewsolutions_andreyk() {
		ProfileModel profileModel = new ProfileModel();
		profileModel.setAttribute("PKey", "@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRkFJueVs494FajCOy2oWVmYEf-7efmTpzGDTwK0kM_nDnL_sHP");
		profileModel.setAttribute("acsId", "e80293df-43f4-4202-9c85-29f2d9f253cd");
		profileModel.setAttribute("age", 0);
		profileModel.setAttribute("birthDate", "");
		profileModel.setAttribute("blackList", false);
		profileModel.setAttribute("blackListAllLastModified", "");
		profileModel.setAttribute("blackListEmail", false);
		profileModel.setAttribute("blackListEmailLastModified", "");
		profileModel.setAttribute("blackListFax", false);
		profileModel.setAttribute("blackListFaxLastModified", "");
		profileModel.setAttribute("blackListLastModified", "");
		profileModel.setAttribute("blackListMobile", false);
		profileModel.setAttribute("blackListMobileLastModified", "");
		profileModel.setAttribute("blackListPhone", false);
		profileModel.setAttribute("blackListPhoneLastModified", "");
		profileModel.setAttribute("blackListPostalMail", false);
		profileModel.setAttribute("blackListPostalMailLastModified", "");
		profileModel.setAttribute("blackListPushnotification", false);
		profileModel.setAttribute("ccpaOptOut", false);
		profileModel.setAttribute("ccpaOptOutLastModified", "");
		profileModel.setAttribute("created", "2020-07-31 11:19:00.304Z");
		profileModel.setAttribute("cryptedId", "0t2lRBOrBEzjrDzGYZPN4TzQ78wt08aaUKwRIDOyxJy1ESpDpy5xyHRQEHkIuXjC6V+lwg==");
		profileModel.setAttribute("cusAlternateEmail", "");
		profileModel.setAttribute("cusByteNumber", 0);
		Link link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/cusCbCustomerProfileLink/");
		profileModel.setAttribute("cusCbCustomerProfileLink", link);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/cusCbMappingSKProfileLink/");
		profileModel.setAttribute("cusCbMappingSKProfileLink", link);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/cusCbMappingTestProfileLink/");
		profileModel.setAttribute("cusCbMappingTestProfileLink", link);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/cusCbParentprofileLink/");
		profileModel.setAttribute("cusCbParentprofileLink", link);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/cusCbTransactionsProfileLink/");
		profileModel.setAttribute("cusCbTransactionsProfileLink", link);
		profileModel.setAttribute("cusCorporateInfo", "test1");
		profileModel.setAttribute(CUSTOMER_ID, "andreyk@yesmail.com");
		profileModel.setAttribute(CUSTOMER_ID_HASH, "");
		profileModel.setAttribute(CUSTOMER_UUID, "");
		profileModel.setAttribute("cusDoubleNumber", 0);
		profileModel.setAttribute("cusEmailSha", "");
		profileModel.setAttribute("cusEncryptedId", "");
		profileModel.setAttribute("cusFloatNumber", 0);
		profileModel.setAttribute("cusHasRewards", false);
		profileModel.setAttribute("cusHomePhone", "");
		profileModel.setAttribute("cusInteger32Bit", 0);
		profileModel.setAttribute("cusLatitude", 0);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/cusListLoadProfilelink/");
		profileModel.setAttribute("cusListLoadProfilelink", link);
		profileModel.setAttribute("cusLongNumber", 0);
		profileModel.setAttribute("cusLongitude", 0);
		profileModel.setAttribute("cusMemo", "");
		profileModel.setAttribute("cusPreferncesLastUpdated", "");
		profileModel.setAttribute("cusPrimaryStore", "");
		profileModel.setAttribute("cusPrimaryStorelink", null);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/cusPromotionprofilelink/");
		profileModel.setAttribute("cusPromotionprofilelink", link);
		profileModel.setAttribute("cusPurchaseOrderFlag", false);
		profileModel.setAttribute("cusRewardsActive", "Y");
		profileModel.setAttribute("cusRewardsExpiration", "");
		profileModel.setAttribute("cusRewardsId", "");
		profileModel.setAttribute("cusRewardsPoints", "");
		profileModel.setAttribute("cusRewardsType", "");
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/cusRfcProfilelink/");
		profileModel.setAttribute("cusRfcProfilelink", link);
		profileModel.setAttribute("cusSecondaryStore", "");
		profileModel.setAttribute("cusSecondaryStorelink", null);
		profileModel.setAttribute("cusShortNumber", 0);
		profileModel.setAttribute("cusSuppressedDomain", false);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/cusTransactionDetailProfilelink/");
		profileModel.setAttribute("cusTransactionDetailProfilelink", link);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/cusTransactionProfilelink/");
		profileModel.setAttribute("cusTransactionProfilelink", link);
		profileModel.setAttribute("domain", "yesmail.com");
		profileModel.setAttribute("email", "andreyk@yesmail.com");
		profileModel.setAttribute("emailFormat", "unknown");
		profileModel.setAttribute("externalId", "");
		profileModel.setAttribute("fax", "");
		profileModel.setAttribute("firstName", "Andrey");
		profileModel.setAttribute("gender", "unknown");
		profileModel.setAttribute("geoUnit", null);
		profileModel.setAttribute("href", "https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRkFJueVs494FajCOy2oWVmYEf-7efmTpzGDTwK0kM_nDnL_sHP");
		profileModel.setAttribute("isExternal", false);
		profileModel.setAttribute("lastModified", "2020-07-31 11:19:00.302Z");
		profileModel.setAttribute("lastName", "Kolesnik");
		profileModel.setAttribute("location.address1", "");
		profileModel.setAttribute("location.address2", "");
		profileModel.setAttribute("location.address3", "");
		profileModel.setAttribute("location.address4", "");
		profileModel.setAttribute("location.city", "");
		profileModel.setAttribute("location.countryCode", "");
		profileModel.setAttribute("location.stateCode", "");
		profileModel.setAttribute("location.zipCode", "");
		profileModel.setAttribute("middleName", "");
		profileModel.setAttribute("mobilePhone", "");
		profileModel.setAttribute("orgUnit", null);
		profileModel.setAttribute("phone", "");
		profileModel.setAttribute("postalAddress.addrDefined", false);
		profileModel.setAttribute("postalAddress.addrErrorCount", 0);
		profileModel.setAttribute("postalAddress.addrLastCheck", "");
		profileModel.setAttribute("postalAddress.addrQuality", "0");
		profileModel.setAttribute("postalAddress.line1", "Andrey KOLESNIK");
		profileModel.setAttribute("postalAddress.line2", "");
		profileModel.setAttribute("postalAddress.line3", "");
		profileModel.setAttribute("postalAddress.line4", "");
		profileModel.setAttribute("postalAddress.line5", "");
		profileModel.setAttribute("postalAddress.line6", "");
		profileModel.setAttribute("postalAddress.serialized", "Andrey KOLESNIK\n\n\n\n\n");
		profileModel.setAttribute("preferredLanguage", "none");
		profileModel.setAttribute("salutation", "");
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRk_dbeBjtl9_KGFnqHaVuEsJtBZPY35aUuhhi4q752yReqaLS8k7Ln0IjymB4CFhULRNZxDQ/subscriptions/");
		profileModel.setAttribute("subscriptions", link);
		profileModel.setAttribute("thumbnail", "/nl/img/thumbnails/defaultProfil.png");
		profileModel.setAttribute("timeZone", "none");
		profileModel.setAttribute("title", "Andrey Kolesnik (andreyk@yesmail.com)");
		return profileModel;
	}

	private static ProfileModel profileModel_ymnewsolutions_charlesb() {
		ProfileModel model = new ProfileModel();
		model.setPKey("@BDzdYCvKKe8k2sI4lX1xqriQZssYktVTZY7fhqiwhjyc3Kt5G84Qf4g2zCZiATMLhmd1mphoPV8sgG_h0wwbTVKGNtA");
		model.setAcsId("b9c5ac88-6c3f-4d5b-a107-50f3d652703e");
		model.setAge(51);
		model.setBirthDate("1969-06-03");
		model.setBlackList(false);
		model.setBlackListAllLastModified("");
		model.setBlackListEmail(false);
		model.setBlackListEmailLastModified("");
		model.setBlackListFax(false);
		model.setBlackListFaxLastModified("");
		model.setBlackListLastModified("");
		model.setBlackListMobile(false);
		model.setBlackListMobileLastModified("");
		model.setBlackListPhone(false);
		model.setBlackListPhoneLastModified("");
		model.setBlackListPostalMail(false);
		model.setBlackListPostalMailLastModified("");
		model.setBlackListPushnotification(false);
		model.setCcpaOptOut(false);
		model.setCcpaOptOutLastModified("");
		model.setCreated("2020-02-14 09:47:29.833Z");
		model.setCryptedId("0xMj6xCSnPjUE64kKsSncQItHvVfwtHPq0Oteql8sfOGb5ISa7HWzuwzJDyVoAuyTzQXmg==");
		model.setCusCustomerId("charlesb");
		model.setAttribute("cusAlternateEmail", "charlesb@yesmail.com");
		model.setAttribute(CUSTOMER_ID_HASH, "B4B7556A4E1F782C7988E734295E0BE15FC8532E84188359C8E29375DAE1641250F2630BBCFF801BD78F09E5150B4F0FF65C94A85476D4E9B56F98BD8C829273");
		model.setAttribute(CUSTOMER_UUID, "b9c5ac88-6c3f-4d5b-a107-50f3d652703e");
		Link profileLink = new Link();
		profileLink.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqriQZssYktVTZY7fhqiwhjy0BfppLI6Kkw2LYU-_yaQ6mw1bdJg9tV1snPekX-bNvZy7zTt1WQGOkMRsUGZWahvGdhka/cusCbCustomerProfileLink/");
		model.setAttribute("cusCbCustomerProfileLink", profileLink);
		model.setAttribute("cusCorporateInfo", "grunt01");
		model.setAttribute("cusEmailSha", "53e0d8cccbea9fbd9b2e06e61c57b74874bf0ae077d3d5819a0d339016708655aefa9438fb95e776adbb1f0dd426be4069583b75f0d9988d64af5ee735b3732e");
		model.setDomain("yesmarketing.com");
		model.setEmail("charles.berger@yesmarketing.com");
		model.setEmailFormat("html");
		model.setExternalId("");
		model.setFax("");
		model.setFirstName("Charles");
		model.setGender("male");
		model.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqriQZssYktVTZY7fhqiwhjyc3Kt5G84Qf4g2zCZiATMLhmd1mphoPV8sgG_h0wwbTVKGNtA");
		model.setIsExternal(false);
		model.setLastModified("2020-04-03 10:18:20.543Z");
		model.setLastName("Berger");
		Location location = new Location();
		location.setAddress1("Address Line 1");
		location.setAddress2("Address Line 2");
		location.setAddress3("Address Line 3");
		location.setAddress4("Address Line 4");
		location.setCity("City");
		location.setCountryCode("US");
		location.setStateCode("NY");
		location.setZipCode("54321");
		model.setLocation(location);
		model.setMiddleName("");
		model.setMobilePhone("+447904736935");
		model.setPhone("");
		PostalAddress postalAddress = new PostalAddress();
		postalAddress.setAddrDefined(false);
		postalAddress.setAddrErrorCount(0L);
		postalAddress.setAddrLastCheck("");
		postalAddress.setAddrQuality("0");
		postalAddress.setLine1("Mr Charles BERGER");
		postalAddress.setLine2("ADDRESS LINE 1");
		postalAddress.setLine3("ADDRESS LINE 2");
		postalAddress.setLine4("ADDRESS LINE 3");
		postalAddress.setLine5("ADDRESS LINE 4");
		postalAddress.setLine6("54321 CITY");
		postalAddress.setSerialized("Mr Charles BERGER\nADDRESS LINE 1\nADDRESS LINE 2\nADDRESS LINE 3\nADDRESS LINE 4\n54321 CITY");
		model.setPostalAddress(postalAddress);
		model.setPreferredLanguage("en_gb");
		model.setSalutation("Mr");
		Link subscriptions = new Link();
		subscriptions.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqriQZssYktVTZY7fhqiwhjy1TTqm1-jq608qPlLGPALPrfFM1rTcx4UXCFclssH0eX-2MnI/subscriptions/");
		model.setSubscriptions(subscriptions);
		model.setThumbnail("/nl/img/thumbnails/defaultProfil.png");
		model.setTimeZone("Europe_London");
		model.setTitle("Charles Berger (charles.berger@yesmarketing.com)");
		return model;
	}

	private static ProfileModel profileModel_ymnewsolutions_cbym() {
		ProfileModel model = new ProfileModel();
		model.setPKey("@LZdQdXuVFw8sTQMSeVWXyq5QcpO75KRHQUsSrr7POBBEn1UN4jdexWCo_FGdbI49vRVlc46wRGjgUfEHmtJMGUvYnO8");
		model.setAcsId("");
		model.setAge(0);
		model.setBirthDate("");
		model.setBlackList(false);
		model.setBlackListAllLastModified("");
		model.setBlackListEmail(false);
		model.setBlackListEmailLastModified("");
		model.setBlackListFax(false);
		model.setBlackListFaxLastModified("");
		model.setBlackListLastModified("");
		model.setBlackListMobile(false);
		model.setBlackListMobileLastModified("");
		model.setBlackListPhone(false);
		model.setBlackListPhoneLastModified("");
		model.setBlackListPostalMail(false);
		model.setBlackListPostalMailLastModified("");
		model.setBlackListPushnotification(false);
		model.setCcpaOptOut(false);
		model.setCcpaOptOutLastModified("");
		model.setCreated("2020-04-14 15:39:56.030Z");
		model.setCryptedId("eUI2yx/8GaWIFNcLqiV4253fVZmLoUlvd04jlvb1RG/zogYeY0nxuNn9X7IiOEKuoWL0aw==");
		model.setCusCustomerId("cb-ym");
		model.setAttribute("cusEmailSha", "f04ec5bc24213c2073adbeb1d00668185044d57973cdf6b24bec1b19fe3918b5e25c0a8b9fff43f6ffd31d3b3304d504b44c2986e1f2aa8c8c94efb6dd810094");
		model.setAttribute(CUSTOMER_ID_HASH, "98EF3F27ADBC25E092DFFEC625E7C679FF30707914125BE9CAC9A8C96C3EF5AE89B8DC7C6F73BEB5EB75134085A062FCF893B13522DBFFBE6FC2EB76B6057367");
		model.setDomain("yesmail.com");
		model.setEmail("charlesb@yesmail.com");
		model.setEmailFormat("unknown");
		model.setExternalId("");
		model.setFax("");
		model.setFirstName("Charles");
		model.setGender("unknown");
		model.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@LZdQdXuVFw8sTQMSeVWXyq5QcpO75KRHQUsSrr7POBBEn1UN4jdexWCo_FGdbI49vRVlc46wRGjgUfEHmtJMGUvYnO8");
		model.setIsExternal(false);
		model.setLastModified("2020-04-14 15:39:56.029Z");
		model.setLastName("Berger (Ym)");
		model.setLocation(new Location());
		model.setMiddleName("");
		model.setMobilePhone("");
		model.setPhone("");
		model.setAttribute("postalAddress.addrDefined", false);
		model.setAttribute("postalAddress.addrErrorCount", 0L);
		model.setAttribute("postalAddress.addrLastCheck", "");
		model.setAttribute("postalAddress.addrQuality", "0");
		model.setAttribute("postalAddress.line1", "Charles BERGER (YM)");
		model.setAttribute("postalAddress.line2", "");
		model.setAttribute("postalAddress.line3", "");
		model.setAttribute("postalAddress.line4", "");
		model.setAttribute("postalAddress.line5", "");
		model.setAttribute("postalAddress.line6", "");
		model.setAttribute("postalAddress.serialized", "Charles BERGER (YM)\n\n\n\n\n");
		model.setPreferredLanguage("none");
		model.setSalutation("");
		Link subscriptions = new Link();
		subscriptions.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@LZdQdXuVFw8sTQMSeVWXyq5QcpO75KRHQUsSrr7POBBfBCGof_c2qjOnSaUvq9vuMWMQ6TCp8IC6by125xyRCFrIaRU/subscriptions/");
		model.setSubscriptions(subscriptions);
		model.setThumbnail("/nl/img/thumbnails/defaultProfil.png");
		model.setTimeZone("none");
		model.setTitle("Charles Berger (Ym) (charlesb@yesmail.com)");
		return model;
	}

	private static ProfileModel profileModel_ymnewsolutions_charlesb11() {
		ProfileModel model = new ProfileModel();
		model.setAttribute("PKey", "@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrJXsr5ah0mOof77TSw7aMXY3cjN2JPQXq04oqH4IQvOgUD9uYM");
		model.setAttribute("acsId", "bdb64076-2052-4762-90da-8ab56c35c30c");
		model.setAttribute("age", 0);
		model.setAttribute("birthDate", "");
		model.setAttribute("blackList", false);
		model.setAttribute("blackListAllLastModified", "");
		model.setAttribute("blackListEmail", false);
		model.setAttribute("blackListEmailLastModified", "");
		model.setAttribute("blackListFax", false);
		model.setAttribute("blackListFaxLastModified", "");
		model.setAttribute("blackListLastModified", "");
		model.setAttribute("blackListMobile", false);
		model.setAttribute("blackListMobileLastModified", "");
		model.setAttribute("blackListPhone", false);
		model.setAttribute("blackListPhoneLastModified", "");
		model.setAttribute("blackListPostalMail", false);
		model.setAttribute("blackListPostalMailLastModified", "");
		model.setAttribute("blackListPushnotification", false);
		model.setAttribute("ccpaOptOut", false);
		model.setAttribute("ccpaOptOutLastModified", "");
		model.setAttribute("created", "2020-06-17 11:41:11.516Z");
		model.setAttribute("cryptedId", "7aPRlNuXxc8pDtlroJirHmgKULjbGrpw0/IBb+M05zPi5AszdxvaDpRfea29goGDJcVeCw==");
		model.setAttribute("cusAlternateEmail", "");
		model.setAttribute("cusByteNumber", 0);
		Link link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIaUmn_x_NkP_TJhnJj6z6R_A38TusTuOTA0SvuWV5ofDMqjM2svIu61JnZfYtrWtjU3dZa/cusCbCustomerProfileLink/");
		model.setAttribute("cusCbCustomerProfileLink", link);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIaUmn_x_NkP_TJhnJj6z6R_A38TusTuOTA0SvuWV5ofDMqjM2svIu61JnZfYtrWtjU3dZa/cusCbMappingSKProfileLink/");
		model.setAttribute("cusCbMappingSKProfileLink", link);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIaUmn_x_NkP_TJhnJj6z6R_A38TusTuOTA0SvuWV5ofDMqjM2svIu61JnZfYtrWtjU3dZa/cusCbMappingTestProfileLink/");
		model.setAttribute("cusCbMappingTestProfileLink", link);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIaUmn_x_NkP_TJhnJj6z6R_A38TusTuOTA0SvuWV5ofDMqjM2svIu61JnZfYtrWtjU3dZa/cusCbParentprofileLink/");
		model.setAttribute("cusCbParentprofileLink", link);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIaUmn_x_NkP_TJhnJj6z6R_A38TusTuOTA0SvuWV5ofDMqjM2svIu61JnZfYtrWtjU3dZa/cusCbTransactionsProfileLink/");
		model.setAttribute("cusCbTransactionsProfileLink", link);
		model.setAttribute("cusCorporateInfo", "test1");
		model.setAttribute(CUSTOMER_ID, "charlesb11");
		model.setAttribute(CUSTOMER_ID_HASH, "1FAC74C1F1A03F4B095DC7703E5BBD1096AF6B624EAC91592008F35C263DB46AEF59EB6E1FFDD1B94DD9977A2218932225EDCDE7AA6357BEC88CCF2F795BE372");
		model.setAttribute(CUSTOMER_UUID, "be643fa5-6459-4771-8569-f3cebdd76187");
		model.setAttribute("cusDoubleNumber", 123.45);
		model.setAttribute("cusEmailSha", "06699fa26cd3a69589c537c63d6e0f706c73453fb3c021199a9e2b6d8f359f58dab2ab7aae148a8b9cc02954deb3f8a8c5851dc98b7847483b27b9efe25eea9c");
		model.setAttribute("cusEncryptedId", "d4e47993904271effc8cfb8bfc8ac2be");
		model.setAttribute("cusFloatNumber", 0);
		model.setAttribute("cusHasRewards", false);
		model.setAttribute("cusHomePhone", "");
		model.setAttribute("cusInteger32Bit", 0);
		model.setAttribute("cusLatitude", 0);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIaUmn_x_NkP_TJhnJj6z6R_A38TusTuOTA0SvuWV5ofDMqjM2svIu61JnZfYtrWtjU3dZa/cusListLoadProfilelink/");
		model.setAttribute("cusListLoadProfilelink", link);
		model.setAttribute("cusLongNumber", 0);
		model.setAttribute("cusLongitude", 0);
		model.setAttribute("cusMemo", "");
		model.setAttribute("cusPreferncesLastUpdated", "");
		model.setAttribute("cusPrimaryStore", "");
		model.setAttribute("cusPrimaryStorelink", null);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIaUmn_x_NkP_TJhnJj6z6R_A38TusTuOTA0SvuWV5ofDMqjM2svIu61JnZfYtrWtjU3dZa/cusPromotionprofilelink/");
		model.setAttribute("cusPromotionprofilelink", link);
		model.setAttribute("cusPurchaseOrderFlag", false);
		model.setAttribute("cusRewardsActive", "Y");
		model.setAttribute("cusRewardsExpiration", "");
		model.setAttribute("cusRewardsId", "");
		model.setAttribute("cusRewardsPoints", "");
		model.setAttribute("cusRewardsType", "");
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIaUmn_x_NkP_TJhnJj6z6R_A38TusTuOTA0SvuWV5ofDMqjM2svIu61JnZfYtrWtjU3dZa/cusRfcProfilelink/");
		model.setAttribute("cusRfcProfilelink", link);
		model.setAttribute("cusSecondaryStore", "");
		model.setAttribute("cusSecondaryStorelink", null);
		model.setAttribute("cusShortNumber", 0);
		model.setAttribute("cusSuppressedDomain", false);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIaUmn_x_NkP_TJhnJj6z6R_A38TusTuOTA0SvuWV5ofDMqjM2svIu61JnZfYtrWtjU3dZa/cusTransactionDetailProfilelink/");
		model.setAttribute("cusTransactionDetailProfilelink", link);
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIaUmn_x_NkP_TJhnJj6z6R_A38TusTuOTA0SvuWV5ofDMqjM2svIu61JnZfYtrWtjU3dZa/cusTransactionProfilelink/");
		model.setAttribute("cusTransactionProfilelink", link);
		model.setAttribute("domain", "yesmail.com");
		model.setAttribute("email", "charlesb+11@yesmail.com");
		model.setAttribute("emailFormat", "unknown");
		model.setAttribute("externalId", "");
		model.setAttribute("fax", "");
		model.setAttribute("firstName", "Charles");
		model.setAttribute("gender", "unknown");
		GeoUnit geoUnit = new GeoUnit();
		geoUnit.setPKey("@YZzzuch1JK5Zj-fNf1SnP1G5XobfqULPSJEv1b45KGleqfW4kHjO1nQYo3FeDezK59zUDsA4bvxigEHGaBC2dtJ5P_E");
		geoUnit.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/geoUnitBase/@YZzzuch1JK5Zj-fNf1SnP1G5XobfqULPSJEv1b45KGleqfW4kHjO1nQYo3FeDezK59zUDsA4bvxigEHGaBC2dtJ5P_E");
		geoUnit.setTitle("All (all)");
		model.setGeoUnit(geoUnit);
		model.setAttribute("href", "https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrJXsr5ah0mOof77TSw7aMXY3cjN2JPQXq04oqH4IQvOgUD9uYM");
		model.setAttribute("isExternal", false);
		model.setAttribute("lastModified", "2020-06-25 11:50:01.770Z");
		model.setAttribute("lastName", "Berger");
		Map<String, Object> location = new HashMap<>();
		location.put("location.address1", "Address Line 1");
		location.put("location.address2", "Address Line 2");
		location.put("location.address3", "Address Line 3");
		location.put("location.address4", "Address Line 4");
		location.put("location.city", "A City");
		location.put("location.countryCode", "US");
		location.put("location.stateCode", "NY");
		location.put("location.zipCode", "12345");
		model.setAttributes(location);
		model.setAttribute("middleName", "");
		model.setAttribute("mobilePhone", "");
		OrgUnit orgUnit = new OrgUnit();
		orgUnit.setPKey("@6pKu78FAsqsPi8U2qt7EKQ7gg76f5Kw-VNuqXLrIv0_b5xpR-Yl7SBOggdZpZdwCUrzEFAulyqzOnWnndmzitgKBxLY");
		orgUnit.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/orgUnitBase/@6pKu78FAsqsPi8U2qt7EKQ7gg76f5Kw-VNuqXLrIv0_b5xpR-Yl7SBOggdZpZdwCUrzEFAulyqzOnWnndmzitgKBxLY");
		orgUnit.setTitle("All (all)");
		model.setOrgUnit(orgUnit);
		model.setAttribute("phone", "");
		Map<String, Object> postalAddress = new HashMap<>();
		postalAddress.put("postalAddress.addrDefined", false);
		postalAddress.put("postalAddress.addrErrorCount", 0L);
		postalAddress.put("postalAddress.addrLastCheck", "");
		postalAddress.put("postalAddress.addrQuality", "0");
		postalAddress.put("postalAddress.line1", "Charles BERGER");
		postalAddress.put("postalAddress.line2", "ADDRESS LINE 1");
		postalAddress.put("postalAddress.line3", "ADDRESS LINE 2");
		postalAddress.put("postalAddress.line4", "ADDRESS LINE 3");
		postalAddress.put("postalAddress.line5", "ADDRESS LINE 4");
		postalAddress.put("postalAddress.line6", "12345 A CITY");
		postalAddress.put("postalAddress.serialized", "Charles BERGER\nADDRESS LINE 1\nADDRESS LINE 2\nADDRESS LINE 3\nADDRESS LINE 4\n12345 A CITY");
		model.setAttributes(postalAddress);
		model.setAttribute("preferredLanguage", "none");
		model.setAttribute("salutation", "");
		link = new Link();
		link.setHref("https://mc.adobe.io/yesmarketing-mkt-stage3/campaign/profileAndServicesExt/profile/@BDzdYCvKKe8k2sI4lX1xqmKeLDClKjDnhHU12bzlDrIbVRxWjLnUjmWq6831vZiNJiwq0_tD_7QVOPUHDBrTrNKKr6yvWxOweNUHOoGtYIbGw1BO/subscriptions/");
		model.setAttribute("subscriptions", link);
		model.setAttribute("thumbnail", "/nl/img/thumbnails/defaultProfil.png");
		model.setAttribute("timeZone", "none");
		model.setAttribute("title", "Charles Berger (charlesb+11@yesmail.com)");
		return model;
	}

	private static ProfileModel profileModel_musicnotes_cbym() {
		ProfileModel profileModel = new ProfileModel();
		{
			profileModel.setPKey("@OD9_4o3XPv2pdeKtJHeMpgEyWJD6w9gu2TVtlztkfDFgj3FdC1Ok7tXHDgF5tEaK3ycJ32yk4JYnPAKh5NVV1h5oQv0");
			profileModel.setAge(0);
			profileModel.setBirthDate("");
			profileModel.setBlackList(false);
			profileModel.setBlackListAllLastModified("");
			profileModel.setBlackListEmail(false);
			profileModel.setBlackListEmailLastModified("");
			profileModel.setBlackListFax(false);
			profileModel.setBlackListFaxLastModified("");
			profileModel.setBlackListLastModified("");
			profileModel.setBlackListMobile(false);
			profileModel.setBlackListMobileLastModified("");
			profileModel.setBlackListPhone(false);
			profileModel.setBlackListPhoneLastModified("");
			profileModel.setBlackListPostalMail(false);
			profileModel.setBlackListPostalMailLastModified("");
			profileModel.setBlackListPushnotification(false);
			profileModel.setCreated("2020-04-17 08:49:29.059Z");
			profileModel.setCryptedId("m+JhpNysubVobE0g/UiT263ehYXrCYbVKT8SxjbaVgcS63U+PfndHiN54DeAGKlFHWHoNw==");
			profileModel.setCusCustomerId("cb-ym");
			profileModel.setDomain("yesmail.com");
			profileModel.setEmail("charlesb@yesmail.com");
			profileModel.setEmailFormat("unknown");
			profileModel.setExternalId("");
			profileModel.setFax("");
			profileModel.setFirstName("Charles");
			profileModel.setGender("unknown");
			profileModel.setHref("https://mc.adobe.io/musicnotes-mkt-stage1/campaign/profileAndServicesExt/profile/@OD9_4o3XPv2pdeKtJHeMpgEyWJD6w9gu2TVtlztkfDFgj3FdC1Ok7tXHDgF5tEaK3ycJ32yk4JYnPAKh5NVV1h5oQv0");
			profileModel.setIsExternal(false);
			profileModel.setLastModified("2020-04-17 08:49:29.059Z");
			profileModel.setLastName("Berger (Ym)");
			profileModel.setMiddleName("");
			profileModel.setMobilePhone("");
			profileModel.setPhone("");
			profileModel.setPreferredLanguage("none");
			profileModel.setSalutation("");
			Link subscriptions = new Link();
			subscriptions.setHref("https://mc.adobe.io/musicnotes-mkt-stage1/campaign/profileAndServicesExt/profile/@OD9_4o3XPv2pdeKtJHeMpgEyWJD6w9gu2TVtlztkfDFpnSXWOIyb95bEwKdJzuEOUmYDc9N4_UFZHSO7Opj3ZjqblJ8/subscriptions/");
			profileModel.setSubscriptions(subscriptions);
			profileModel.setThumbnail("/nl/img/thumbnails/defaultProfil.png");
			profileModel.setTimeZone("none");
			profileModel.setTitle("Charles Berger (Ym) (charlesb@yesmail.com)");
			return profileModel;
		}
	}
}
