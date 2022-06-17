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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class CreateProfile implements En {


    private static final Logger LOG = LoggerFactory.getLogger(CreateProfile.class);

    private static final String PROFILES_CREATE_URI = "/v1/services/profiles/create";

    private String postEmail = "";
    private String postCusCustomerId = "";

    private List<Header> reqHeaders = new ArrayList<>();
    private JSONObject request = new JSONObject();

    private TestResponse testResponse;


    public CreateProfile() {
        Given("the request body contains no email", () -> {
            // does nothing, no email added
        });

        Given("no value is provided for email in the request", () -> {
            request.put("email","");
        });

        Given("the request body contains a valid email value", () -> {
            postEmail = "zolitest"+generateUniqueId()+"@yesmail.com";
            postCusCustomerId = "zolitest"+generateUniqueId() + "@yesmail.com";
            request.put("email",postEmail);
        });

        And("the request body contains an existing email value", () -> {
            postEmail = "zolitest1@yesmail.com";
            postCusCustomerId = "zolitest1@yesmail.com";
            request.put("email",postEmail);
        });

        And("the create profile request contains custom field cusCustomerId",()->{
            JSONObject customFields = null;
            if(request.containsKey("customFields")){
                customFields = (JSONObject) request.get("customFields");
            }else{
                customFields = new JSONObject();
            }
            customFields.put("cusCustomerId",postCusCustomerId);
            request.put("customFields",customFields);
        });

        And("the create profile request contains the correct create profile form token",()-> {
            String tokenState = "is valid";
            String token = JsonWebTokenRepository.getToken(tokenState)
                    .orElseThrow(() -> new TokenStateException(tokenState));
            reqHeaders.add(new BasicHeader("x-services-token", token));
        });

        When("the create profile service is called", () -> {
            LOG.info("CREATE PROFILE service is called");
            String uri = String.format("%s%s", Utils.getBaseUrl(), PROFILES_CREATE_URI);
            Utils.appendDefaultPostHeaders(reqHeaders);
            testResponse = Utils.submitPostRequest(uri, reqHeaders.toArray(new Header[0]), request.toJSONString());
        });

        Then("a bad create profile request error is returned",()->SoftAssertions.assertSoftly(softly ->{
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_BAD_REQUEST);
        }));

        Then("a no value was provided create profile request error is returned",()->SoftAssertions.assertSoftly(softly ->{
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_NOT_FOUND);
        }));

        Then("the create profile service response has status code: 201 CREATED",()->SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_CREATED);
        }));

        Then("^the create profile response contains the profile data$", () -> {
            String respBody = testResponse.getBody();
            SoftAssertions.assertSoftly(softly -> {
                JsonUtils.valueIsJson(softly, respBody, "Response is valid Json");
                JsonUtils.jsonPathEquals(softly, respBody, "Check customerId", "$.customFields.cusCustomerId", postCusCustomerId);
                JsonUtils.jsonPathEquals(softly, respBody, "Check email", "$.email", postEmail);
            });
        });

    }

    public String generateUniqueId(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddhhmm");
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());
        return ldt.format(dtf);
    }

}
