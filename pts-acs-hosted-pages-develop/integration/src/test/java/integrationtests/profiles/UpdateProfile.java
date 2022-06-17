package integrationtests.profiles;

import com.yesmarketing.ptsacs.model.TestResponse;
import com.yesmarketing.ptsacs.utils.JsonUtils;
import com.yesmarketing.ptsacs.utils.Utils;
import integrationtests.testdata.form.JsonWebTokenRepository;
import integrationtests.testdata.form.TokenStateException;
import io.cucumber.java8.En;
import net.minidev.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class UpdateProfile implements En {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateProfile.class);

    private static Double getRandomDouble(){
        Double rand = Math.random()*100;
        return Math.floor(rand)/10;
    }

    private static final String PROFILES_URI = "/v1/services/profiles";
    private static final String PKEY_CASE_A_B = "@HHw_0GdbesPMwrYx0ifAu7MKedU-QS9O19IiwiFhBu6QznvCRyoLZe_vkLriTnRkFJueVs494FajCOy2oWVmYEf-7efmTpzGDTwK0kM_nDnL_sHP";
    private static final String PKEY_THAT_DOES_NOT_EXIST = "@IhFGrTRZNi5b1TuPWb8Wr2Qh-tRTuw0RQHSCI8IgyrcO5dptt09pPReytRlISV_hGKPtl_RJ7ub2chGFmR71UYxlu1WpQRvTYe6J7CXE1qdF5rQi";

    private static final String HASH_ZOLITEST = "17B2CB0444363DD4D409D339B76EAF144FBF980231FDA0EAC908D7A10FEEDB336DF11C0E6A84194186D2F402EE402D26AA2154F0E0E0D8719D17F90B4131C80F";

    private String firstNameUpdateForCaseA = "Acasefirstname";
    private String firstNameUpdateForCaseB = "Bcasefirstname";
    private String lastNameUpdateForCaseA = "Acaselastname";
    private String lastNameUpdateForCaseB = "Bcaselastname";

    private Double cusFloatNumberB = getRandomDouble();

    private List<Header> reqHeaders = new ArrayList<>();
    private JSONObject request = new JSONObject();

    private TestResponse testResponse;
    private String updateLink;

    public UpdateProfile() {

        Given("an update request link that returns a profile object", () -> { updateLink=PROFILES_URI+"/update/%s"; });
        Given("an update request link that does not return a profile object", () -> { updateLink=PROFILES_URI+"/updateonly/%s"; });

        And("the link contains a valid customerid hash",()->{ updateLink = String.format(updateLink,HASH_ZOLITEST); });
        And("the link contains an invalid PKey",()->{ updateLink = String.format(updateLink,PKEY_THAT_DOES_NOT_EXIST); });

        And("the update profile request contains the correct update profile form token",()-> {
            String tokenState = "is valid";
            String token = JsonWebTokenRepository.getToken(tokenState)
                    .orElseThrow(() -> new TokenStateException(tokenState));
            reqHeaders.add(new BasicHeader("x-services-token", token));
        });

        And("the request contains a token that does not authorize the update",()->reqHeaders.add(new BasicHeader("x-services-token","ZOLI_INVALID_TOKEN")));

        And("the request contains an update to first name for case A",()->request.put("firstName",firstNameUpdateForCaseA));
        And("the request contains an update to first name for case B",()->request.put("firstName",firstNameUpdateForCaseB));
        And("the request contains an update to last name for case A",()->request.put("lastName",lastNameUpdateForCaseA));
        And("the request contains an update to last name for case B",()->request.put("lastName",lastNameUpdateForCaseB));
        And("the request contains a non existing attribute",()->request.put("someBadAttribute","test"));

        And("the request contains an update to cusFloatNumber for case B",()->{
            JSONObject customFields = null;
            if(request.containsKey("customFields")){
                customFields = (JSONObject) request.get("customFields");
            }else{
                customFields = new JSONObject();
            }
            customFields.put("cusFloatNumber",cusFloatNumberB);
            request.put("customFields",customFields);
        });


        And("request contains empty value for first name",()->request.put("firstName",""));
        And("request contains empty value for custom field cusFloatNumber",()->{
            JSONObject customFields = null;
            if(request.containsKey("customFields")){
                customFields = (JSONObject) request.get("customFields");
            }else{
                customFields = new JSONObject();
            }
            customFields.put("cusFloatNumber","");
            request.put("customFields",customFields);
        });


        /**
         * WHEN update profile service is called
         */
        When("the update profile service is called",()->{
            LOG.info("UPDATE PROFILE service is called");
            String uri = String.format("%s%s", Utils.getBaseUrl(), updateLink);
            Utils.appendDefaultPutHeaders(reqHeaders);
            testResponse = Utils.submitPutRequest(uri, reqHeaders.toArray(new Header[0]), request.toJSONString());
        });

        Then("the update profile service response has status code: 200 OK",()->SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(org.apache.http.HttpStatus.SC_OK);
        }));

        Then("the update profile service response has status code: 404 NOT FOUND",()->SoftAssertions.assertSoftly(softly ->{
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_NOT_FOUND);
        }));

        Then("update request fails authorization",()->SoftAssertions.assertSoftly(softly ->{
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_UNAUTHORIZED);
        }));

        Then("update profile service returns response with a status code for a bad request",()->SoftAssertions.assertSoftly(softly ->{
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_BAD_REQUEST);
        }));

        And("response contains PKey in the body",()->SoftAssertions.assertSoftly(softly -> {
            String respBody = testResponse.getBody();
            JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
            JsonUtils.jsonPathNotNull(softly, respBody, "Check PKey in response", "$.PKey");
        }));

        And("response contains updated first name for case B",()->SoftAssertions.assertSoftly(softly -> {
            String respBody = testResponse.getBody();
            JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
            JsonUtils.jsonPathEquals(softly, respBody, "Check firstName for B", "$.firstName", firstNameUpdateForCaseB);
        }));

        And("response contains updated last name for case B",()->SoftAssertions.assertSoftly(softly -> {
            String respBody = testResponse.getBody();
            JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
            JsonUtils.jsonPathEquals(softly, respBody, "Check lastName for B", "$.lastName", lastNameUpdateForCaseB);
        }));

        And("response contains updated cusFloatNumber for case B",()->SoftAssertions.assertSoftly(softly -> {
            String respBody = testResponse.getBody();
            JsonUtils.jsonPathEquals(softly, respBody, "Check cusFloatNumber for B", "$.cusFloatNumber", cusFloatNumberB+"");
        }));

        And("response contains empty first name",()->SoftAssertions.assertSoftly(softly -> {
            String respBody = testResponse.getBody();
            JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
            JsonUtils.jsonPathEquals(softly, respBody, "Check empty first name", "$.firstName", "");
        }));

        And("response contains empty custom field cusFloatNumber",()->SoftAssertions.assertSoftly(softly -> {
            String respBody = testResponse.getBody();
            JsonUtils.jsonPathEquals(softly, respBody, "Check empty cusFloatNumber", "$.cusFloatNumber", "");
        }));

        And("update profile response contains a proper error message for a non existing attribute",()->SoftAssertions.assertSoftly(softly -> {
            System.out.println();
        }));

    }

}
