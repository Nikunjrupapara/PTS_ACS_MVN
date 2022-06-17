package com.yesmarketing.ptsacs.services.resource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.yesmarketing.acsapi.model.Link;
import com.yesmarketing.acsapi.model.Location;
import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ProfileSubscriptionModel;
import com.yesmarketing.acsapi.model.ResponseModel;
import com.yesmarketing.ptsacs.services.dto.CreateProfileDto;
import com.yesmarketing.ptsacs.services.dto.GetProfileDto;
import com.yesmarketing.ptsacs.services.dto.GetProfileResponseDto;
import com.yesmarketing.ptsacs.services.dto.GetServiceSubscriptionDto;
import com.yesmarketing.ptsacs.services.dto.LocationDto;
import com.yesmarketing.ptsacs.services.model.GetProfileResponse;
import com.yesmarketing.ptsacs.services.util.ProfileModelTestHelper;
import com.yesmarketing.ptsacs.services.util.ServicesModelMapperHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestProfileModelMapping {

	private ServicesModelMapperHelper servicesModelMapperHelper;

	private String company;

	@BeforeEach
	void setup() throws Exception {
		servicesModelMapperHelper = new ServicesModelMapperHelper(new ModelMapper());
		servicesModelMapperHelper.initialise();
	}

	@Test
	void validateMappings() {
		assertDoesNotThrow(() -> servicesModelMapperHelper.getModelMapper().validate());
	}

	@Nested
	@DisplayName("Mapping to/from ProfileModel")
	class MappingToFromProfileModel {
		@Test
		void profileModelToGetProfileDto() {
			company = "ymnewsolutions";
			String customerId = "charlesb";
			ProfileModel model = ProfileModelTestHelper.getProfileModel(company, customerId);
			GetProfileDto profileDto = servicesModelMapperHelper.toDto(model, GetProfileDto.class);
			assertAll("", () -> {
				assertNotNull(profileDto);
				assertAll("Standard profile attributes",
						() -> assertNotNull(profileDto.getPKey()),
						() -> assertNotNull(profileDto.getCryptedId()),
						() -> assertEquals(customerId, profileDto.getCustomerId()),
						() -> assertEquals("charles.berger@yesmarketing.com", profileDto.getEmail()),
						() -> assertEquals("Charles", profileDto.getFirstName()),
						() -> assertEquals("Berger", profileDto.getLastName())
				);
				Map<String, Object> customFields = profileDto.getCustomFields();
				assertNotNull(customFields, "Custom fields map is not null");
				assertAll("Custom profile attributes",
						() -> assertTrue(customFields.containsKey("cusAlternateEmail"), "cusAlternateEmail"),
						() -> assertTrue(customFields.containsKey("cusCorporateInfo"), "cusCorporateInfo"),
						() -> {
							Link profileLink = (Link) customFields.get("cusCbCustomerProfileLink");
							assertNotNull(profileLink.getHref(), "cusCbProfileLink.href");
						},
						() -> assertEquals("53e0d8cccbea9fbd9b2e06e61c57b74874bf0ae077d3d5819a0d339016708655aefa9438fb95e776adbb1f0dd426be4069583b75f0d9988d64af5ee735b3732e",
								(String) customFields.get("cusEmailSha"), "cusEmailSha")
				);
			});
		}

		@Test
		void profileModelToGetProfileModelWithLocationAndAddress() {
			company = "ymnewsolutions";
			String customerId = "charlesb11";
			ProfileModel model = ProfileModelTestHelper.getProfileModel(company, customerId);
			GetProfileDto profileDto = servicesModelMapperHelper.toDto(model, GetProfileDto.class);
			assertAll("", () -> {
				assertNotNull(profileDto);
				assertAll("Standard profile attributes",
					() -> assertNotNull(profileDto.getPKey()),
					() -> assertNotNull(profileDto.getCryptedId()),
					() -> assertEquals(customerId, profileDto.getCustomerId()),
					() -> assertEquals("charlesb+11@yesmail.com", profileDto.getEmail()),
					() -> assertEquals("Charles", profileDto.getFirstName()),
					() -> assertEquals("Berger", profileDto.getLastName())
				);
				LocationDto location = profileDto.getLocation();
				assertNotNull(location, "Location is not null");
				assertAll("location fields",
					() -> assertEquals("Address Line 1", location.getAddress1(), "Address Line 1"),
					() -> assertEquals("Address Line 2", location.getAddress2(), "Address Line 2"),
					() -> assertEquals("Address Line 3", location.getAddress3(), "Address Line 3"),
					() -> assertEquals("Address Line 4", location.getAddress4(), "Address Line 4"),
					() -> assertEquals("A City", location.getCity(), "City"),
					() -> assertEquals("US", location.getCountryCode(), "Country"),
					() -> assertEquals("NY", location.getStateCode(), "State"),
					() -> assertEquals("12345", location.getZipCode(), "Zipcode")
				);
			});
		}

		@Test
		void createProfileModelDtoToProfileModel() {
			CreateProfileDto dto = new CreateProfileDto();
			dto.setBirthDate("1969-06-03");
			dto.setEmail("charlesb@yesmail.com");
			dto.setFirstName("Charles");
			dto.setGender("male");
			dto.setLastName("Berger");
			dto.setMobilePhone("+447904736935");
			dto.setPhone("+447904736935");
			Map<String, Object> customFields = new HashMap<>();
			customFields.put("cusAlternateEmail", "charles.berger@yesmarketing.com");
			customFields.put("cusCorporateInfo", "grunt1");
			customFields.put("cusHasRewards", Boolean.FALSE);
			customFields.put("cusMemo", "Lorem ipsum dolor sit amet");
			dto.setCustomFields(customFields);
			LocationDto locationDto = new LocationDto();
			locationDto.setAddress1("Address 1");
			locationDto.setAddress2("Address 2");
			locationDto.setAddress3("Address 3");
			locationDto.setAddress4("Address 4");
			locationDto.setCity("A City");
			locationDto.setCountryCode("US");
			locationDto.setStateCode("NY");
			locationDto.setZipCode("12345");
			dto.setLocation(locationDto);
			ProfileModel profileModel = servicesModelMapperHelper.fromDto(dto, ProfileModel.class);
			assertAll("", () -> {
				assertNotNull(profileModel);
				assertAll("Model assertions",
						() -> assertNull(profileModel.getCreated()),
						() -> assertNull(profileModel.getPKey()),
						() -> assertNull(profileModel.getCryptedId()),
						() -> assertEquals("1969-06-03", profileModel.getBirthDate()),
						() -> assertEquals("charlesb@yesmail.com", profileModel.getEmail()),
						() -> assertEquals("Charles", profileModel.getFirstName()),
						() -> assertEquals("male", profileModel.getGender()),
						() -> assertEquals("Berger", profileModel.getLastName()),
						() -> assertEquals("+447904736935", profileModel.getMobilePhone()),
						() -> assertEquals("+447904736935", profileModel.getPhone()),
						() -> assertNotNull(profileModel.getAttribute("cusAlternateEmail").orElse(null), "cusAlternateEmail"),
						() -> assertNotNull(profileModel.getAttribute("cusCorporateInfo").orElse(null), "cusCorporateInfo"),
						() -> assertFalse((Boolean) profileModel.getAttribute("cusHasRewards").orElse(true), "cusHasRewards"),
						() -> assertNotNull(profileModel.getAttribute("cusMemo").orElse(null), "cusMemo"),
					() -> assertEquals("Address 1", profileModel.getLocation().getAddress1(), "location.address1"),
					() -> assertEquals("Address 2", profileModel.getLocation().getAddress2(), "location.address2"),
					() -> assertEquals("Address 3", profileModel.getLocation().getAddress3(), "location.address3"),
					() -> assertEquals("Address 4", profileModel.getLocation().getAddress4(), "location.address4"),
					() -> assertEquals("A City", profileModel.getLocation().getCity(), "location.city"),
					() -> assertEquals("US", profileModel.getLocation().getCountryCode(), "location.countryCode"),
					() -> assertEquals("NY", profileModel.getLocation().getStateCode(), "location.stateCode"),
					() -> assertEquals("12345", profileModel.getLocation().getZipCode(), "location.zipCode")
				);
			});
		}

		@Test
		void locationDtoToLocation() {
			LocationDto locationDto = new LocationDto();
			locationDto.setAddress1("Address 1");
			locationDto.setAddress2("Address 2");
			locationDto.setAddress3("Address 3");
			locationDto.setAddress4("Address 4");
			locationDto.setCity("A City");
			locationDto.setCountryCode("US");
			locationDto.setStateCode("NY");
			locationDto.setZipCode("12345");
			Location location = servicesModelMapperHelper.fromDto(locationDto, Location.class);
			assertAll("", () -> {
				assertNotNull(location);
				assertAll("Location assertions",
					() -> assertEquals("Address 1", location.getAddress1(), "location.address1"),
					() -> assertEquals("Address 2", location.getAddress2(), "location.address2"),
					() -> assertEquals("Address 3", location.getAddress3(), "location.address3"),
					() -> assertEquals("Address 4", location.getAddress4(), "location.address4"),
					() -> assertEquals("A City", location.getCity(), "location.city"),
					() -> assertEquals("US", location.getCountryCode(), "location.countryCode"),
					() -> assertEquals("NY", location.getStateCode(), "location.stateCode"),
					() -> assertEquals("12345", location.getZipCode(), "location.zipCode")
				);
			});
		}
	}

	@Nested
	@DisplayName("ProfileServiceModel to GetServiceSubscriptionDto")
	class ProfileServiceModelToGetServiceSubscriptionDto {
		@Test
		void yesMarketingAService() {
			company = "ymnewsolutions";
			String customerId = "charlesb11";
			ResponseModel<ProfileSubscriptionModel> rm = ProfileModelTestHelper.getProfileSubscriptions(company, customerId);
			List<GetServiceSubscriptionDto> dtoList = servicesModelMapperHelper.toDto(rm.getContent(), GetServiceSubscriptionDto.class);
			assertNotNull(dtoList);
			assertTrue(dtoList.size() > 0);
			assertAll("Check Service Subscription DTO", () -> {
				GetServiceSubscriptionDto dto = dtoList.get(0);
				assertNotNull(dto.getCreated(), "Has created");
				assertEquals("Marketing A", dto.getLabel(), "Check label");
				assertEquals("yesMarketingAService", dto.getName(), "Check name");
			});
		}
	}

	@Nested
	@DisplayName("GetProfileResponse to GetProfileResponseDto")
	class GetProfileResponseMappings {
		@Test
		@DisplayName("Profile with no services or history")
		void profileNoServicesNoHistory() {
			company = "ymnewsolutions";
			ProfileModel profileModel = ProfileModelTestHelper.getProfileModel(company, "charlesb11");
			GetProfileResponse getProfileResponse = new GetProfileResponse(profileModel, Collections.emptyList(), Collections.emptyList());
			GetProfileResponseDto dto = servicesModelMapperHelper.toDto(getProfileResponse, GetProfileResponseDto.class);
			assertNotNull(dto);
			assertAll("Check response DTO", () -> {
				assertNotNull(dto.getProfile());
				GetProfileDto profileDto = dto.getProfile();
				assertAll("Profile Dto", () -> {
					assertEquals(profileDto.getPKey(), profileModel.getPKey(), "PKey");
					assertEquals(profileDto.getCustomerId(), profileModel.getCusCustomerId(), "CustomerId");
					profileDto.getCustomFields().forEach((key, value) -> {
						assertEquals(value, profileModel.getCustomFields().get(key));
					});
					//assertNotNull(profileDto.getCustomFields(), "Custom Fields map");
				});
				assertTrue(dto.getCurrentServices().isEmpty(), "Current Services is empty");
			});
		}
	}
}
