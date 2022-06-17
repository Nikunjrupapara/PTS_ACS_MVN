package integrationtests;

import integrationtests.testdata.IntegrationTestDataHelper;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(Cucumber.class)
@CucumberOptions(strict = true)
public class RunCucumberTest {
	@BeforeClass
	public static void setup() throws InterruptedException {

		IntegrationTestDataHelper.seedTestData();
	}

	@AfterClass
	public static void tearDown() {
		IntegrationTestDataHelper.destroyTestData();
	}
}
