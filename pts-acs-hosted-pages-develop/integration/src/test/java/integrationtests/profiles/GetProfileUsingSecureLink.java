package integrationtests.profiles;

import static org.junit.Assert.fail;

import com.yesmarketing.acsapi.model.AdobeCampaignConstants;
import com.yesmarketing.ptsacs.model.TestResponse;
import com.yesmarketing.ptsacs.utils.JsonUtils;
import com.yesmarketing.ptsacs.utils.Utils;
import integrationtests.testdata.form.JsonWebTokenRepository;
import integrationtests.testdata.form.TokenStateException;
import io.cucumber.java8.En;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
import org.assertj.core.api.SoftAssertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetProfileUsingSecureLink implements En {

	private static final String PROFILES_URI = "/v1/services/profiles";
	private static final String PARAM_LOOKUP = "l";
	private static final String PARAM_SECURITY = "s";
	private static final String PARAM_VIEW = "view";
	private static final String PARAM_VIEW_VAL_FULL = "full";
	private static final String PARAM_VIEW_VAL_PROFILE = "profile";
	private static final String PARAM_VIEW_VAL_SERVICES = "services";

	private final Map<String, String> queryParams = new HashMap<>();

	private final String acsId = "bdb64076-2052-4762-90da-8ab56c35c30c";
	private final String cusCustomerUUID = "ece584b3-dbbf-4d99-a7be-47bcd84f0356";
	private final String zolitest202010280807_CustomerIDHash = "D2C33F0CAC4463D417EB7778FDECD08EB4FF8145EA78920862F874C74E09EB0EDE7DDF38F706E1405057D6BCA3D350D978CC10ECC820850636DE694E9D2A004E";

	private TestResponse testResponse;

	public GetProfileUsingSecureLink() {
		Given("^a secure link that contains an unrecognised URL parameter$", () -> queryParams.put("foo", "bar"));
		When("^the get profile service is called$", () -> {
			String uri = String.format("%s%s", Utils.getBaseUrl(), PROFILES_URI);

			String queryString = "";
			if (MapUtils.isNotEmpty(queryParams)) {
				queryString = queryParams
						.entrySet()
						.stream()
						.map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
						.collect(Collectors.joining("&"));
			}
			if (StringUtils.isNotEmpty(queryString)) {
				uri = String.format("%s?%s", uri, queryString);
			}
			List<Header> headersList = Arrays.asList(Utils.defaultHeaders());
			List<Header> reqHeaders = new ArrayList<>(headersList);
			String tokenState = "is valid";
			String token = JsonWebTokenRepository.getToken(tokenState)
					.orElseThrow(() -> new TokenStateException(tokenState));
			reqHeaders.add(new BasicHeader("x-services-token", token));
			testResponse = Utils.submitGetRequest(uri, reqHeaders.toArray(new Header[0]));
		});
		Then("^it returns a bad request error$", () -> SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_BAD_REQUEST);
			String respBody = testResponse.getBody();
			JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
			JsonUtils.jsonPathEquals(softly, respBody, "Check status", "$.status", 400);
			JsonUtils.jsonPathEquals(softly, respBody, "Check message", "$.message", "The request URL contained one or more invalid parameters.");
			JsonUtils.jsonPathEquals(softly, respBody, "Check moreInfo", "$.moreInfo", "Check the parameters and try again.");
		}));
		Given("^a secure link that does not contain a lookup field parameter$", () -> queryParams.put(PARAM_SECURITY, "XYZ"));
		Given("^a secure link that contains a lookup field parameter$", () -> queryParams.put(PARAM_LOOKUP, "ABC"));
		And("^the secure link does not contain a security field parameter$", () -> {
			// nothing to do here
		});
		Given("^a secure link that contains an unknown lookup field$", () -> {
			queryParams.put(PARAM_LOOKUP, "ABC");
			queryParams.put(PARAM_SECURITY, cusCustomerUUID);
		});
		Then("^it returns a resource not found error$", () -> SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(testResponse.getStatusCode()).as("Status Code").isEqualTo(HttpStatus.SC_NOT_FOUND);
			String respBody = testResponse.getBody();
			JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
			JsonUtils.jsonPathEquals(softly, respBody, "Check status", "$.status", 404);
			JsonUtils.jsonPathEquals(softly, respBody, "Check message", "$.message", "No profile was found with the supplied parameters.");
			JsonUtils.jsonPathEquals(softly, respBody, "Check moreInfo", "$.moreInfo", "Check the parameters and try again.");
		}));
		Given("^a secure link that contains a valid lookup field$", () -> queryParams.put(PARAM_LOOKUP, zolitest202010280807_CustomerIDHash));
		And("^the secure link's security field does not match the security field on the retrieved profile$", () -> queryParams.put(PARAM_SECURITY, "XYZ"));
		And("^the secure link's security field matches the profile's security field$", () -> queryParams.put(PARAM_SECURITY, cusCustomerUUID));
		And("^the secure link contains an invalid view parameter$", () -> {
			queryParams.put(PARAM_VIEW, "foo");
		});
		And("^the view parameter is not provided$", () -> {
			// nothing to do
		});
		And("^the view parameter is profile$", () -> {
			queryParams.put(PARAM_VIEW, PARAM_VIEW_VAL_PROFILE);
		});
		And("^the view parameter is services$", () -> {
			queryParams.put(PARAM_VIEW, PARAM_VIEW_VAL_SERVICES);
		});
		And("^the view parameter is full$", () -> {
			queryParams.put(PARAM_VIEW, PARAM_VIEW_VAL_FULL);
		});
		Then("^the profile response contains the profile data$", () -> {
			String respBody = testResponse.getBody();
			SoftAssertions.assertSoftly(softly -> {
				softly.assertThat(testResponse.getStatusCode()).as("Status Code").isEqualTo(HttpStatus.SC_OK);
				JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
				List<String> excludeFromAssertion = Collections.singletonList(AdobeCampaignConstants.BLACKLISTPUSHNOTIFICATIONLASTMODIFIED);
				AdobeCampaignConstants.ACS_STANDARD_PROFILE_ATTRS
						.stream()
						.filter(name -> !excludeFromAssertion.contains(name))
						.forEach(name -> JsonUtils.jsonPathNotNull(softly, respBody, String.format("%s is present", name), String.format("$.profile.%s", name)));
				List<String> customFields = Arrays.asList("cusAlternateEmail", "cusByteNumber", "cusCbCustomerProfileLink", "cusCbMappingSKProfileLink",
						"cusCbMappingTestProfileLink", "cusCbParentprofileLink", "cusCbTransactionsProfileLink", "cusCorporateInfo", "cusCustomerId", "cusCustomerIdSHA",
						"cusCustomerUUID", "cusDoubleNumber", "cusEmailSha", "cusEncryptedId", "cusFloatNumber", "cusHasRewards", "cusHomePhone",
						"cusInteger32Bit", "cusLatitude", "cusListLoadProfilelink", "cusLongNumber", "cusLongitude", "cusMemo", "cusPreferncesLastUpdated",
						"cusPrimaryStore", "cusPrimaryStorelink", "cusPromotionprofilelink", "cusPurchaseOrderFlag", "cusRewardsActive", "cusRewardsExpiration",
						"cusRewardsId", "cusRewardsPoints", "cusRewardsType", "cusRfcProfilelink", "cusSecondaryStore", "cusSecondaryStorelink", "cusShortNumber",
						"cusSuppressedDomain", "cusTransactionDetailProfilelink", "cusTransactionProfilelink");
				customFields
						.forEach(name -> JsonUtils.jsonPathNotNull(softly, respBody, String.format("%s is present", name), String.format("$.profile.customFields.%s", name)));

				JsonUtils.jsonPathEquals(softly, respBody, "Check customerId", "$.profile.customFields.cusCustomerId", "zolitest202010280807@yesmail.com");
				JsonUtils.jsonPathEquals(softly, respBody, "Check email", "$.profile.email", "zolitest202010280807@yesmail.com");
			});
		});
		And("^the profile response contains a list of current services for the profile$", () -> {
			String respBody = testResponse.getBody();
			SoftAssertions.assertSoftly(softly -> {
				JsonUtils.jsonPathNotNull(softly, respBody, "Has services", "$.currentServices");
				JsonUtils.jsonPathHasItem(softly, respBody, "yesMarketingAService", "$.currentServices[?(@.name == 'yesMarketingAService')].name", "yesMarketingAService");
			});
		});
		And("^the profile response contains the services the profile has unsubscribed from$", () -> {
			fail("Not yet implemented!");
		});
	}
}
