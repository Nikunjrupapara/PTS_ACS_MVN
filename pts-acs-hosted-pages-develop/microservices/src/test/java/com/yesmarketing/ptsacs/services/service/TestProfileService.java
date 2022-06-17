package com.yesmarketing.ptsacs.services.service;

import static com.yesmarketing.ptsacs.services.util.ServicesConstants.CUSTOMER_ID_HASH;
import static com.yesmarketing.ptsacs.services.util.ServicesConstants.CUSTOMER_UUID;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmarketing.acsapi.auth.service.Authenticator;
import com.yesmarketing.acsapi.dao.ProfileMetadataDao;
import com.yesmarketing.acsapi.model.Link;
import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ProfileSubscriptionModel;
import com.yesmarketing.acsapi.sagas.SagasHelper;
import com.yesmarketing.acsapi.sagas.SubscriptionSagas;
import com.yesmarketing.ptsacs.common.repository.CustomerIdDefinitionRepository;
import com.yesmarketing.ptsacs.common.service.MockCredentialService;
import com.yesmarketing.ptsacs.services.enums.View;
import com.yesmarketing.ptsacs.services.model.GetProfileResponse;
import com.yesmarketing.ptsacs.services.service.impl.ProfileServiceImpl;
import com.yesmarketing.ptsacs.services.util.ProfileModelTestHelper;
import com.yesmarketing.ptsacs.services.util.SubscriberSagasTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.List;

public class TestProfileService {

	private static final String ACS_BASE_URL = "http://localhost";

	private WireMockServer wireMockServer;

	private SagasHelper sagasHelperSpy;

	private ProfileService profileService;

	private SubscriptionSagas subscriptionSagas;

	private CustomerIdService customerIdService;

	private CustomerIdDefinitionRepository customerIdDefinitionRepository;

	@BeforeEach
	void setup() throws Exception {
		wireMockServer = new WireMockServer(
			options()
			.dynamicPort()
			.usingFilesUnderClasspath("src/test/resources/wiremock")
			.notifier(new ConsoleNotifier(true))
		);
		wireMockServer.start();

		SagasHelper sagasHelper = new SagasHelper(new MockCredentialService(), Mockito.mock(Authenticator.class));
		sagasHelperSpy = Mockito.spy(sagasHelper);
		ProfileMetadataDao profileMetadataDao = Mockito.mock(ProfileMetadataDao.class);
		subscriptionSagas = Mockito.mock(SubscriptionSagas.class);
		profileService = new ProfileServiceImpl(sagasHelperSpy, profileMetadataDao, subscriptionSagas, customerIdService);

		Mockito
				.when(profileMetadataDao.getProfileMetadata(anyString()))
				.thenAnswer(invocation -> {
					String company = invocation.getArgument(0);
					return SubscriberSagasTestHelper.getProfileMetadata(company);
				});
	}

	@AfterEach
	void tearDown() {
		wireMockServer.stop();
	}

	@Nested
	@DisplayName("Get Profile with CustomerId Hash and acsId")
	class GetProfileByEncryptedLinkTests {

		private String company, lookupValue, securityValue;

		private View view;

		private GetProfileResponse getProfileResponse;

		private ProfileModel profileModel;

		private String getWireMockUrl(InvocationOnMock invocation) {
			String method = invocation.getArgument(1);
			int httpPort = wireMockServer.port();
			return String.format("%s:%d/%s", ACS_BASE_URL, httpPort, method);
		}

		@BeforeEach
		void setup() {
			company = "ymnewsolutions";
		}

		@Test
		@DisplayName("Get a profile using provided CustomerId Hash and verified with provided acsId")
		void getProfileOnlyWithLookupValue() throws Exception {
			Mockito
				.when(sagasHelperSpy.formUrl(anyString(), anyString()))
				.thenAnswer(this::getWireMockUrl);

			lookupValue = "1FAC74C1F1A03F4B095DC7703E5BBD1096AF6B624EAC91592008F35C263DB46AEF59EB6E1FFDD1B94DD9977A2218932225EDCDE7AA6357BEC88CCF2F795BE372";
			securityValue = "be643fa5-6459-4771-8569-f3cebdd76187";
			view = View.PROFILE;
			getProfileResponse = profileService.getProfileBySecureLink(company, lookupValue, securityValue, view);

			assertAll("Validate result",
					() -> {
						profileModel = getProfileResponse.getProfile();
						assertNotNull(profileModel);
						assertAll("Check retrieved profile",
								() -> assertNotNull(profileModel.getPKey(), "PKey is not null"),
								() -> assertEquals("charlesb+11@yesmail.com", profileModel.getEmail()),
								() -> assertEquals("Charles", profileModel.getFirstName()),
								() -> assertEquals("Berger", profileModel.getLastName()),
								() -> assertEquals(lookupValue, profileModel.getAttribute(CUSTOMER_ID_HASH).orElse("")),
								() -> assertEquals(securityValue, profileModel.getAttribute(CUSTOMER_UUID).orElse(""))
						);
					},
					() -> assertAll("Check Current Services",
							() -> {
								List<ProfileSubscriptionModel> currentServices = getProfileResponse.getCurrentServices();
								assertTrue(currentServices.isEmpty(), "currentServices not expected");
							}),
					() -> assertAll("Check Historic Services",
							() -> {
								List<Object> historicServices = getProfileResponse.getHistoricServices();
								assertTrue(historicServices.isEmpty(), "historicServices not expected");
							}
					)
			);
		}

		@Test
		@DisplayName("Get a profile and current services using provided CustomerId Hash and verified with provided acsId")
		void getProfileAndCurrentServicesWithLookupValue() throws Exception {
			Mockito
					.when(sagasHelperSpy.formUrl(anyString(), anyString()))
					.thenAnswer(this::getWireMockUrl);

			Mockito
					.when(subscriptionSagas.getProfileSubscriptions(anyString(), any(Link.class)))
					.thenAnswer(invocation -> {
						String company = invocation.getArgument(0);
						return ProfileModelTestHelper.getProfileSubscriptions(company, "charlesb11");
					});

			lookupValue = "1FAC74C1F1A03F4B095DC7703E5BBD1096AF6B624EAC91592008F35C263DB46AEF59EB6E1FFDD1B94DD9977A2218932225EDCDE7AA6357BEC88CCF2F795BE372";
			securityValue = "be643fa5-6459-4771-8569-f3cebdd76187";
			view = View.SERVICES;
			getProfileResponse = profileService.getProfileBySecureLink(company, lookupValue, securityValue, view);

			assertAll("Validate result",
					() -> {
						profileModel = getProfileResponse.getProfile();
						assertNotNull(profileModel);
						assertAll("Check retrieved profile",
								() -> assertNotNull(profileModel.getPKey(), "PKey is not null"),
								() -> assertEquals("charlesb+11@yesmail.com", profileModel.getEmail()),
								() -> assertEquals("Charles", profileModel.getFirstName()),
								() -> assertEquals("Berger", profileModel.getLastName()),
								() -> assertEquals(lookupValue, profileModel.getAttribute(CUSTOMER_ID_HASH).orElse("")),
								() -> assertEquals(securityValue, profileModel.getAttribute(CUSTOMER_UUID).orElse(""))
						);
					},
					() -> assertAll("Check Current Services",
							() -> {
								List<ProfileSubscriptionModel> currentServices = getProfileResponse.getCurrentServices();
								assertNotNull(currentServices, "currentServices expected");
								assertAll("currentServices", () -> {
									assertTrue(currentServices
													.stream()
													.anyMatch(svc -> svc.getService().getName().equals("yesMarketingAService")),
											"Contains yesMarketingAService");
								});
							}),
					() -> assertAll("Check Historic Services",
							() -> {
								List<Object> historicServices = getProfileResponse.getHistoricServices();
								assertTrue(historicServices.isEmpty(), "historicServices not expected");
							}
					)
			);
		}

		@Test
		@DisplayName("Provided security value does not match retrieved profile")
		void securityValueDoesNotMatch() throws Exception {
			Mockito
				.when(sagasHelperSpy.formUrl(anyString(), anyString()))
				.thenAnswer(this::getWireMockUrl);

			lookupValue = "1FAC74C1F1A03F4B095DC7703E5BBD1096AF6B624EAC91592008F35C263DB46AEF59EB6E1FFDD1B94DD9977A2218932225EDCDE7AA6357BEC88CCF2F795BE372";
			securityValue = "Any random value";
			view = View.PROFILE;
			assertThatThrownBy(() -> profileService.getProfileBySecureLink(company, lookupValue, securityValue, view))
				.isInstanceOf(ObjectNotFoundException.class);
		}

		@Test
		@DisplayName("No profile for provided CustomerId hash")
		void noProfileForLookupValue() throws Exception {
			Mockito
				.when(sagasHelperSpy.formUrl(anyString(), anyString()))
				.thenAnswer(this::getWireMockUrl);

			lookupValue = "AnyRandomValue";
			securityValue = "be643fa5-6459-4771-8569-f3cebdd76187";
			view = View.PROFILE;
			assertThatThrownBy(() -> profileService.getProfileBySecureLink(company, lookupValue, securityValue, view))
				.isInstanceOf(ObjectNotFoundException.class);
		}

	}
}
