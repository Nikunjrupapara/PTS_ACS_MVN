package integrationtests.profiles;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;

import java.util.stream.Collectors;

import static org.junit.Assert.fail;

public class GetProfileUsingCustomerId implements En {
    private static final Logger LOG = LoggerFactory.getLogger(GetProfileUsingCustomerId.class);

    private static final String PROFILES_URI = "/v1/services/profiles";
    private static final String PROFILES_CUSTOMER_URI = "/v1/services/profiles/customer";
    private static final String PARAM_LOOKUP = "l";
    private static final String PARAM_SECURITY = "s";
    private static final String PARAM_VIEW = "view";
    private static final String PARAM_VIEW_VAL_FULL = "full";
    private static final String PARAM_VIEW_VAL_PROFILE = "profile";
    private static final String PARAM_VIEW_VAL_SERVICES = "services";

    private final Map<String, String> queryParams = new HashMap<>();

    private final String acsId = "bdb64076-2052-4762-90da-8ab56c35c30c";
    private final String emailSha = "1FAC74C1F1A03F4B095DC7703E5BBD1096AF6B624EAC91592008F35C263DB46AEF59EB6E1FFDD1B94DD9977A2218932225EDCDE7AA6357BEC88CCF2F795BE372";

    private final String andreyAcsIs = "e80293df-43f4-4202-9c85-29f2d9f253cd";
    private final String andreySha = "EE5D05EDF0113D7A2A7279FC21F23912B081F932DFCA54E0F62EBA89C275563EBCBEBF34DB839B5D03A3677A788E8BEE1006A0B7C995AF732C6BDE4450CB4E3F";

    private String postEmail = "andreyk@yesmail.com";
    private String postBody = "";

    private TestResponse testResponse;

    public GetProfileUsingCustomerId() {
        Given("the POST request contains a view parameter", () -> {
            System.out.println(); // TBD whether this is needed
        });

        Given("the view param is invalid", () -> {
            queryParams.put(PARAM_VIEW, "zoli_invalid");
        });

        Given("the view param is not provided", () -> {
            queryParams.remove(PARAM_VIEW);
        });

        Given("the view param is profile", () -> {
            queryParams.put(PARAM_VIEW, PARAM_VIEW_VAL_PROFILE);
        });

        Given("the view param is services", () -> {
            queryParams.put(PARAM_VIEW, PARAM_VIEW_VAL_SERVICES);
        });

        Given("the view param is full", () -> {
            queryParams.put(PARAM_VIEW, PARAM_VIEW_VAL_FULL);
        });

        When("the get profile by customer id service is called", () -> {
            String uri = String.format("%s%s", Utils.getBaseUrl(), PROFILES_CUSTOMER_URI);

            String queryString = getQueryParams();
            if (StringUtils.isNotEmpty(queryString)) {
                uri = String.format("%s?%s", uri, queryString);
            }

            List<Header> headersList = Arrays.asList(Utils.defaultPostHeaders());
            List<Header> reqHeaders = new ArrayList<>(headersList);
            reqHeaders.add(new BasicHeader("company", "ymnewsolutions"));
            String tokenState = "is valid";
            String token = JsonWebTokenRepository.getToken(tokenState)
                    .orElseThrow(() -> new TokenStateException(tokenState));
            reqHeaders.add(new BasicHeader("x-services-token", token));
            testResponse = Utils.submitPostRequest(uri, reqHeaders.toArray(new Header[0]), postBody);
        });


        Then("^a bad request error is returned$", () -> SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_BAD_REQUEST);
            String respBody = testResponse.getBody();
            JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
            JsonUtils.jsonPathEquals(softly, respBody, "Check status", "$.status", 400);
            JsonUtils.jsonPathEquals(softly, respBody, "Check message", "$.message", "The request URL contained one or more invalid parameters.");
            JsonUtils.jsonPathEquals(softly, respBody, "Check moreInfo", "$.moreInfo", "Check the parameters and try again.");
        }));

        Then("^a supplied payload mismatch error is returned$", () -> SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_BAD_REQUEST);
            String respBody = testResponse.getBody();
            JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
            JsonUtils.jsonPathEquals(softly, respBody, "Check status", "$.status", 400);
            JsonUtils.jsonPathEquals(softly, respBody, "Check message", "$.message", "The supplied payload does not match the company configuration.");
            JsonUtils.jsonPathEquals(softly, respBody, "Check moreInfo", "$.moreInfo", "Please verify the correct configuration and resubmit the request.");
        }));

        Then("^a no value supplied in payload error is returned$", () -> SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_NOT_FOUND);
            String respBody = testResponse.getBody();
            JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
            JsonUtils.jsonPathEquals(softly, respBody, "Check status", "$.status", 404);
            JsonUtils.jsonPathEquals(softly, respBody, "Check message", "$.message", "No value was provided for field 'email' but it is a required component for the customer Id for company 'ymnewsolutions'.");
            JsonUtils.jsonPathEquals(softly, respBody, "Check moreInfo", "$.moreInfo", "Please correct the request to provide a non empty value for the absent field.");
        }));

        Given("the POST request contains an empty list of search fields", () -> {
            postBody = getPostJSONEmpty();
        });

        Given("the size of the search fields list does not match that of the customer definition", () -> {
            postEmail = "andreyk@yesmail.com";
            postBody = getPostJSONSizeDoesNotMatch(postEmail);
            
        });

        Given("the search fields list has one or more fields that are not matched to a customer definition field", () -> {
            postEmail = "andreyk@yesmail.com";
            postBody = getPostJSONSizeDoesNotMatch(postEmail);
            
        });

        Given("the search fields list contains required fields without values", () -> {
            postBody = getPostJSONNoValues("andreyk@yesmail.com");
            
        });

        Given("the search fields list contains and matches all required fields with values", () -> {
            postEmail = "andreyk@yesmail.com";
            postBody = getPostJSON(postEmail);
            
        });

        Given("the search fields list does not match a profile", () -> {
            postEmail = "zoli_foo@yesmail.com";
            postBody = getPostJSON(postEmail);
        });

        Then("^a resource not found error is returned$", () -> SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(testResponse.getStatusCode()).as("Status Code").isEqualTo(HttpStatus.SC_NOT_FOUND);
            String respBody = testResponse.getBody();
            JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
            JsonUtils.jsonPathEquals(softly, respBody, "Check status", "$.status", 404);
            JsonUtils.jsonPathEquals(softly, respBody, "Check message", "$.message", "No profile was found with the supplied parameters.");
            JsonUtils.jsonPathEquals(softly, respBody, "Check moreInfo", "$.moreInfo", "Check the parameters and try again.");
        }));

        Given("the search fields list matches a profile", () -> {
            System.out.println();
            
        });

        Then("the profile response has the profile data", () -> {
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

                JsonUtils.jsonPathEquals(softly, respBody, "Check customerId", "$.profile.customFields.cusCustomerId", "andreyk@yesmail.com");
                JsonUtils.jsonPathEquals(softly, respBody, "Check email", "$.profile.email", "andreyk@yesmail.com");
            });

        });

        Then("the profile response has a list of current services for the profile", () -> {
            System.out.println();
        });

        Then("the profile response has the services the profile has unsubscribed from", () -> {

        });

    }

    private String getQueryParams(){
        String queryString = "";
        if (MapUtils.isNotEmpty(queryParams)) {
            queryString = queryParams
                    .entrySet()
                    .stream()
                    .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining("&"));
        }
        return queryString;
    }

    public static String getPostJSON(String email) {
        StringBuilder sb = new StringBuilder("");
        sb.append("{ \"searchFields\": {\"email\":");
        sb.append("\"").append(email).append("\"");
        sb.append(" } }");

        return sb.toString();
    }

    public static String getPostJSONNoValues(String email) {
        StringBuilder sb = new StringBuilder("");
        sb.append("{ \"searchFields\": {\"email\":");
        sb.append("\"").append("\"");
        sb.append(" } }");

        return sb.toString();
    }

    public static String getPostJSONEmpty() {
        StringBuilder sb = new StringBuilder("");
        sb.append("{ \"searchFields\": {");
        sb.append("}  }");

        return sb.toString();
    }

    public static String getPostJSONSizeDoesNotMatch(String email) {
        StringBuilder sb = new StringBuilder("");
        sb.append("{ \"searchFields\": {\"email\":");
        sb.append("\"").append(email).append("\"");
        sb.append(", \"id\":\"zoliid\"  } }");

        return sb.toString();
    }

}
