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

public class UpdateServices implements En {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateServices.class);

    private static final String UPDATE_SERVICES_URI = "/v1/services/profiles";
    private static final String SERVICES_ENDPOINT = "/services";
    private static String profile_pkey = "";
    private static String requestBody = "";
    private static final String valid_pkey = "@IWWqAMNLXgnnlVNfNgzZbhIfp6fWOg_tcp7ERlCk-X4uHxSRevnzBkRBHkN7RSattHh-90ywEgVgREKANAweopE1ppuaeZQmr2nbNDlum7VjvX-A";

    private static final String HASH_ZOLITEST = "17B2CB0444363DD4D409D339B76EAF144FBF980231FDA0EAC908D7A10FEEDB336DF11C0E6A84194186D2F402EE402D26AA2154F0E0E0D8719D17F90B4131C80F";


    private List<Header> reqHeaders = new ArrayList<>();
    private TestResponse testResponse;

    public UpdateServices() {

        Given("no PKey provided in the update services request uri", () -> {
            profile_pkey = "";
        });

        Given("invalid PKey provided in the update services request uri", () -> {
            profile_pkey = "zoli_invalid_pkey";
        });

        Given("valid customerIdHash provided in the update services request uri", () -> {
            profile_pkey = HASH_ZOLITEST;
        });

        Given("update services request contains no service arrays", () -> {
            requestBody = getMissingServiceArraysJSON();
        });

        Given("update services request contains no service names", () -> {
            requestBody = getMissingServiceNamesJSON();
        });

        Given("update services request contains invalid service names", () -> {
            requestBody = getInvalidServiceNamesJSON();
        });

        Given("all validation is passed for the update services request", () -> {
            requestBody = getValidUpdateServicesJSON();
        });

        When("the update services service is called", () -> {
            LOG.info("UPDATE SERVICES service is called");
            String uri = String.format("%s%s/%s%s", Utils.getBaseUrl(), UPDATE_SERVICES_URI, profile_pkey, SERVICES_ENDPOINT);
            Utils.appendDefaultPutHeaders(reqHeaders);

            String tokenState = "is valid";
            String token = JsonWebTokenRepository.getToken(tokenState)
                    .orElseThrow(() -> new TokenStateException(tokenState));
            reqHeaders.add(new BasicHeader("x-services-token", token));

            testResponse = Utils.submitPutRequest(uri, reqHeaders.toArray(new Header[0]), requestBody);
        });

        Then("a bad update services request error is returned",()-> SoftAssertions.assertSoftly(softly ->{
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_BAD_REQUEST);
        }));

        Then("the update services service response has status code: 404 NOT FOUND",()->SoftAssertions.assertSoftly(softly ->{
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(HttpStatus.SC_NOT_FOUND);
        }));

        Then("the update services service response has status code: 200 OK",()->SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(testResponse.getStatusCode()).as("Status code").isEqualTo(org.apache.http.HttpStatus.SC_OK);
        }));

    }

    private static String getMissingServiceArraysJSON(){
        StringBuilder sb = new StringBuilder("");
        sb.append("{\n" +
                "}");

        return sb.toString();
    }

    private static String getMissingServiceNamesJSON(){
        StringBuilder sb = new StringBuilder("");
        sb.append("{\n" +
                " \"add\":[]\n" +
                " ,\"remove\":[]\n" +
                "}");

        return sb.toString();
    }


    private static String getInvalidServiceNamesJSON(){
        StringBuilder sb = new StringBuilder("");
        sb.append("{\n" +
                " \"add\":[\"yesMarketingZOLIAddInvalid\"]\n" +
                " ,\"remove\":[\"yesMarketingZOLIRemoveInvalid\"]\n" +
                "}");

        return sb.toString();
    }

    private static String getValidUpdateServicesJSON(){
        StringBuilder sb = new StringBuilder("");
        sb.append("{\n" +
                " \"add\":[\"yesMarketingAService\",\"yesMarketingBService\",\"yesMarketingTransactionalA\"]\n" +
                " ,\"remove\":[\"yesMarketingAService\",\"yesMarketingBService\",\"yesMarketingTransactionalA\"]\n" +
                "}");

        return sb.toString();
    }

}
