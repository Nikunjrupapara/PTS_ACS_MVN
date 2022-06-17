package com.dataaxle.pts.acscustompages.model.conditions;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;

@Deprecated
public interface ProfileCondition /*extends Condition<ProfileResponse>*/ {
	boolean evaluate(ProfileResponse value);
}
