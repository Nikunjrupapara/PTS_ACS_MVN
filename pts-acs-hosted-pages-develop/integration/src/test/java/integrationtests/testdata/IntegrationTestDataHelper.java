package integrationtests.testdata;

import integrationtests.testdata.credential.CredentialTestDataHelper;
import integrationtests.testdata.form.FormConfigTestDataHelper;

public class IntegrationTestDataHelper {

	public static void seedTestData() {
		CredentialTestDataHelper.createData();
		FormConfigTestDataHelper.createData();
	}

	public static void destroyTestData() {
		CredentialTestDataHelper.deleteData();
		FormConfigTestDataHelper.deleteData();
	}

}
