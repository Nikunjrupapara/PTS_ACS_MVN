package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CurrentService;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.service.UpdateServicesService;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.updateServices", havingValue = "true")
public class StubbedUpdateServicesService implements UpdateServicesService {

	@Override
	public ProfileResponseWrapper updateServices(AppDetails appDetails, ProfileRequest profileRequest) {
		String company = appDetails.getCompany();
		ProfileResponse profileResponse = StubbedProfileRepository.getByCustomerIdHash(company, profileRequest.getCustomerUniqueId());
		List<CurrentService> currentServices = profileResponse.getCurrentServices();
		Map<String, ServiceAction> serviceActions = profileRequest.getServicesRequest().getServices();
		serviceActions.forEach((serviceName, serviceAction) -> {
			if (serviceAction == ServiceAction.REMOVE) {
				Optional<CurrentService> svcOpt = findCurrentService(currentServices, serviceName);
				//svcOpt.ifPresent(currentServices::remove);
				svcOpt.ifPresent(currentServices::remove);
			}
			if (serviceAction == ServiceAction.ADD) {
				Optional<CurrentService> svcOpt = findCurrentService(currentServices, serviceName);
				if (svcOpt.isEmpty()) {
					currentServices.add(StubbedProfileRepository.getCurrentService(company, serviceName));
				}
			}
		});
		ProfileResponseWrapper profileResponseWrapper = new ProfileResponseWrapper(profileRequest);
		profileResponseWrapper.setProfileResponse(profileResponse);
		profileResponseWrapper.setServicesSuccess();
		return profileResponseWrapper;
	}

	private Optional<CurrentService> findCurrentService(List<CurrentService> currentServices, String serviceName) {
		return currentServices.stream()
			.filter(cs -> cs.getName().equals(serviceName))
			.findFirst();
	}
}
