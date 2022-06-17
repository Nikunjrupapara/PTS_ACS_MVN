package com.dataaxle.pts.acscustompages.model;

import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProfileResponseWrapper implements Serializable {

	private static final long serialVersionUID = 4028649714935753686L;

	ProfileRequestResult profileRequestResult;

	ProfileResponse profileResponse;

	public ProfileResponseWrapper() {

	}

	public ProfileResponseWrapper(ProfileRequest profileRequest) {
		this.profileRequestResult = new ProfileRequestResult(profileRequest);
	}

	public ProfileResponseWrapper(ProfileRequest profileRequest, ProfileResponse profileResponse) {
		this.profileRequestResult = new ProfileRequestResult(profileRequest);
		this.profileResponse = profileResponse;
	}

	public ProfileResponseWrapper(GetProfileRequest getProfileRequest, ProfileResponse profileResponse) {
		this.profileRequestResult = new ProfileRequestResult(getProfileRequest, true, true);
		this.profileResponse = profileResponse;
	}

	public ProfileResponseWrapper(GetProfileRequest getProfileRequest, boolean profileLookup, boolean profileFound) {
		this.profileRequestResult = new ProfileRequestResult(getProfileRequest, profileLookup, profileFound);
	}

	public ProfileResponseWrapper(GetProfileRequest getProfileRequest, boolean profileLookup, boolean profileFound,
								  boolean profileLookupError) {
		this.profileRequestResult = new ProfileRequestResult(getProfileRequest, profileLookup, profileFound, profileLookupError);
	}

	public ProfileResponseWrapper(ProfileRequest profileRequest, TriggerEmailResponse triggerEmailResponse) {
		this.profileRequestResult = new ProfileRequestResult(profileRequest);
		this.profileRequestResult.setEmailSuccess(triggerEmailResponse.getStatus().equals("pending"));
	}

	public void setProfileSuccess() {
		profileRequestResult.setProfileSuccess(true);
	}

	public void setProfileError() {
		profileRequestResult.setProfileSuccess(false);
	}

	public void setServicesSuccess() {
		profileRequestResult.setServicesSuccess(true);
	}

	public void setServicesError() {
		profileRequestResult.setServicesSuccess(false);
	}

	public void setCustomResourcesSuccess() { profileRequestResult.setCustomResourcesSuccess(true); }

	public void setEmailSuccess() {
		profileRequestResult.setEmailSuccess(true);
	}

	public void setEmailFailure() {
		profileRequestResult.setEmailSuccess(false);
	}

	public boolean hasProfile() {
		return profileResponse != null;
	}

	public boolean isAllSuccess() { return profileRequestResult.isAllSuccess(); }

	public void setErrorMessage(String message) {
		profileRequestResult.setErrorMessage(message);
	}
}
