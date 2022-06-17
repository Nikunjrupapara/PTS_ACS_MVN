package com.dataaxle.pts.acscustompages.model.form;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
@Deprecated
public class SignUpFormBean extends AbstractFormBean<ProfileDetails> {

	public SignUpFormBean(ProfileDetails profileDetails) {
		super(profileDetails);
	}
}
