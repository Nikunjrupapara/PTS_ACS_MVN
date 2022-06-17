package integrationtests.profiles;

import com.yesmarketing.ptsacs.model.TestResponse;
import com.yesmarketing.ptsacs.utils.Utils;
import integrationtests.testdata.form.JsonWebTokenRepository;
import integrationtests.testdata.form.TokenStateException;
import io.cucumber.java8.En;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CompositeService implements En {
    private static final Logger LOG = LoggerFactory.getLogger(CompositeService.class);

    private static final String COMPOSITE_SERVICE_URI = "/v1/services/composite/";
    private static final String ZOLI_HASH = "9A353E5E15D65B26FAF2CB278C1D175F3AC0FE2F15539A58DF5952E80C3C1E6C3267D6EF26B6D79B57B7E07464D7B58C1221315BBF06C5B802C78CDD79F760CF";

    private List<Header> reqHeaders = new ArrayList<>();
    private TestResponse testResponse;

    private String requestBody = "";
    private String requestHash = "";

    public CompositeService() {
        Given("a composite profile service request", () -> {
            requestBody = getProfile();
        });
        Given("a composite profile by hash service request", () -> {
            requestBody = getProfile();
            requestHash = ZOLI_HASH;
        });

        Given("a composite profile_subscription service request", () -> {
            requestBody = getProfileSubscriptions();
        });
        Given("a composite profile_subscription by hash service request", () -> {
            requestBody = getProfileSubscriptions();
            requestHash = ZOLI_HASH;
        });

        Given("a composite profile_email service request", () -> {
            requestBody = getProfileEmail();
        });
        Given("a composite profile_email by hash service request", () -> {
            requestBody = getProfileEmail();
            requestHash = ZOLI_HASH;
        });

        Given("a composite profile_subscription_email service request", () -> {
            requestBody = getProfileSubscriptionsEmail();
        });
        Given("a composite profile_subscription_email by hash service request", () -> {
            requestBody = getProfileSubscriptionsEmailByHash();
            requestHash = ZOLI_HASH;
        });

        When("the composite service update or create request is called", () -> {
            LOG.info("COMPOSITE SERVICE service update or create is called");
            String uri = String.format("%s%s", Utils.getBaseUrl(), COMPOSITE_SERVICE_URI);
            Utils.appendDefaultPutHeaders(reqHeaders);

            String tokenState = "is valid";
            String token = JsonWebTokenRepository.getToken(tokenState)
                    .orElseThrow(() -> new TokenStateException(tokenState));
            reqHeaders.add(new BasicHeader("x-services-token", token));

            testResponse = Utils.submitPostRequest(uri, reqHeaders.toArray(new Header[0]), requestBody);
        });

        When("the composite service update by hash request is called", () -> {
            LOG.info("COMPOSITE SERVICE service update by hash is called");
            String uri = String.format("%s%s%s", Utils.getBaseUrl(), COMPOSITE_SERVICE_URI, requestHash);
            Utils.appendDefaultPutHeaders(reqHeaders);

            String tokenState = "is valid";
            String token = JsonWebTokenRepository.getToken(tokenState)
                    .orElseThrow(() -> new TokenStateException(tokenState));
            reqHeaders.add(new BasicHeader("x-services-token", token));

            testResponse = Utils.submitPostRequest(uri, reqHeaders.toArray(new Header[0]), requestBody);
        });

        Then("the composite service response has status code: 200 OK",()-> SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(org.apache.http.HttpStatus.SC_OK);
        }));

    }

    private String getProfile(){
        return "{\"profile\":" +
                "{\"email\":\"zolitest1@yesmail.com\"," +
                "\"firstName\":\"Zed1\"," +
                "\"lastName\":\"S1\"}}";

    }

    private String getProfileSubscriptions(){
        return "{\"profile\":" +
                "{\"email\":\"zolitest1@yesmail.com\"," +
                "\"firstName\":\"Zed1\"," +
                "\"lastName\":\"S1\"}," +
                "\"services\":" + getUpdateSubscriptionsJSON() +
                "}";
    }

    private String getProfileEmail(){
        return "{\"profile\":" +
                "{\"email\":\"zolitest1@yesmail.com\"," +
                "\"firstName\":\"Zed1\"," +
                "\"lastName\":\"S1\"}," +
                "\"emails\":["+ getEmailJSON()+"]}";
    }

    private String getProfileSubscriptionsEmail(){
        return "{\"profile\":" +
                "{\"email\":\"zolitest1@yesmail.com\"," +
                "\"firstName\":\"Zed1\"," +
                "\"lastName\":\"S1\"}," +
                "\"services\":" + getUpdateSubscriptionsJSON() +
                ",\"emails\":["+ getEmailJSON()+"]}";
    }

    private String getProfileSubscriptionsEmailByHash(){
        return "{\"profile\":" +
                "{\"email\":\"zolitest1@yesmail.com\"," +
                "\"firstName\":\"Zed1\"," +
                "\"lastName\":\"S1\"}," +
                "\"services\":" + getUpdateSubscriptionsJSON() +
                ",\"emails\":["+ getEmailJSON()+"]}";
    }

    private static String getEmailJSON(){
        StringBuilder sb = new StringBuilder("");
        sb.append("{    \n" +
                "    \"email\": \"zsule@yesmail.com\",\n" +
                "    \"eventId\": \"" + "EVTAKTESTEVENTTRIGGER" + "\",\n" +
                "    \"ctx\": {\n" +
                "        \"email\": \"zsule@yesmail.com\",\n" +
                "        \"fieldTwo\": \"field two string\",\n" +
                "        \"fieldOne\": \"field one string\",\n" +
                "        \"collection\": [\n" +
                "            {\n" +
                "                \"itemName\": \"item name #1\",\n" +
                "                \"ItemPrice\": 32767,\n" +
                "                \"ItemImage\": \"item image #1\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"itemName\": \"item name #2\",\n" +
                "                \"ItemPrice\": 12345,\n" +
                "                \"ItemImage\": \"item image #2\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}");

        return sb.toString();
    }

    private static String getUpdateSubscriptionsJSON(){
        StringBuilder sb = new StringBuilder("");
        sb.append("{\n" +
                " \"add\":[\"yesMarketingAService\",\"yesMarketingBService\",\"yesMarketingTransactionalA\"]\n" +
                " ,\"remove\":[\"yesMarketingAService\",\"yesMarketingBService\",\"yesMarketingTransactionalA\"]\n" +
                "}");

        return sb.toString();
    }

}
