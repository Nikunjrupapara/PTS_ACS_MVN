package integrationtests.profiles;

import com.yesmarketing.ptsacs.model.TestResponse;
import com.yesmarketing.ptsacs.utils.Utils;
import integrationtests.testdata.form.JsonWebTokenRepository;
import integrationtests.testdata.form.TokenStateException;
import io.cucumber.java8.En;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EmailSend implements En {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSend.class);

    private static final String EMAIL_SEND_URI = "/v1/services/email/send";
    private static String email_eventid = "";
    private static String requestBody = "";


    private List<Header> reqHeaders = new ArrayList<>();
    private TestResponse testResponse;

    public EmailSend() {
        Given("no transactional event id is provided in the request", () -> {
            email_eventid = "";
        });

        Given("no email address is provided in the transactional email request", () -> {
            email_eventid = "EVTAKTESTEVENTTRIGGER";
            requestBody = getMissingEmailJSON();
        });

        Given("no event context is provided in the transactional email request", () -> {
            email_eventid = "EVTAKTESTEVENTTRIGGER";
            requestBody = getMissingContextJSON();
        });

        Given("all validation is passed for the transactional email request", () -> {
            email_eventid = "EVTAKTESTEVENTTRIGGER";
            requestBody = getValidEmailJSON();

        });

        /**
         * WHEN the transactional email service is called
         */
        When("the transactional email service is called", () -> {
            LOG.info("TRANSACTIONAL EMAIL SEND service is called");
            String uri = String.format("%s%s", Utils.getBaseUrl(), EMAIL_SEND_URI);
            Utils.appendDefaultPutHeaders(reqHeaders);

            String tokenState = "is valid";
            String token = JsonWebTokenRepository.getToken(tokenState)
                    .orElseThrow(() -> new TokenStateException(tokenState));
            reqHeaders.add(new BasicHeader("x-services-token", token));

            testResponse = Utils.submitPostRequest(uri, reqHeaders.toArray(new Header[0]), requestBody);
        });

        Then("a bad transactional email request error is returned",()-> SoftAssertions.assertSoftly(softly ->{
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_BAD_REQUEST);
        }));

        Then("the transactional email service response has status code: 404 NOT FOUND",()->SoftAssertions.assertSoftly(softly ->{
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_NOT_FOUND);
        }));

        Then("the transactional email service response has status code: 200 OK",()->SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(org.apache.http.HttpStatus.SC_OK);
        }));



    }

    private static String getMissingEmailJSON(){
        StringBuilder sb = new StringBuilder("");
        sb.append("{    \n" +
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

    private static String getMissingContextJSON(){
        StringBuilder sb = new StringBuilder("");
        sb.append("{    \n" +
                "    \"email\": \"zsule@yesmail.com\"\n" +
                "}");

        return sb.toString();
    }

    private static String getValidEmailJSON(){
        StringBuilder sb = new StringBuilder("");
        sb.append("{    \n" +
                "    \"email\": \"zsule@yesmail.com\",\n" +
                "    \"eventId\": \"" + email_eventid + "\",\n" +
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

}
