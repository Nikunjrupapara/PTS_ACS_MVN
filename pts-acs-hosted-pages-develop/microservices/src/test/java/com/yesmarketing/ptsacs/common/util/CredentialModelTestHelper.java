package com.yesmarketing.ptsacs.common.util;

import com.yesmarketing.acsapi.auth.model.CredentialModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CredentialModelTestHelper {

	private static final Logger LOG = LoggerFactory.getLogger(CredentialModelTestHelper.class);

	private static final Map<String, CredentialModel> authenticationTestCredentials = new HashMap<>();

	private static final String PROJECT_ROOT = ".";

	private static final String FIXTURE_ROOT = String.format("%s%s%s", PROJECT_ROOT,
		File.separator, "src/test/resources/json/credential");

	static {
		LocalDateTime now = LocalDateTime.now();
		String company;
		CredentialModel ymStage3 = getCredentialModel(now);
		company = "ymnewsolutions";
		ymStage3.setCompany(company);
		ymStage3.setOrganization(company);
		authenticationTestCredentials.put(company, ymStage3);
		CredentialModel musicnotes = getCredentialModel(now);
		company = "musicnotes";
		musicnotes.setCompany(company);
		musicnotes.setOrganization(company);
		authenticationTestCredentials.put(company, musicnotes);
	}

	public static CredentialModel getCredentialModelFromDto() {
		CredentialModel credentialModel = new CredentialModel();
		credentialModel.setClientId("clientId");
		credentialModel.setOrganizationId("organizationId");
		credentialModel.setPrivateKey("PrivateKey");
		credentialModel.setSecret("secret");
		credentialModel.setTransactionalApi("transactionalApi");
		credentialModel.setUsername("username");
		return credentialModel;
	}

	public static CredentialModel getCredentialModel(LocalDateTime now) {
		CredentialModel credentialModel = getCredentialModelFromDto();
		credentialModel.setCompany("jefftronics3");
		credentialModel.setCreated(now);
		credentialModel.setEnabled(true);
		credentialModel.setLastUpdated(now);
		return credentialModel;
	}

	public static String getPayloadFromFile(String requestName) throws IOException {
		String fileName = String.format("%s.json", requestName);
		String filePath = String.format("%s%s%s", FIXTURE_ROOT, File.separator, fileName);
		String payload;
		try {
			payload = new String(Files.readAllBytes(Paths.get(filePath)));
			return payload;
		} catch (IOException e) {
			LOG.error("Error opening file {}", fileName, e);
			throw e;
		}
	}

	public static CredentialModel getCredential(String company) {
		return authenticationTestCredentials.get(company);
	}
}
