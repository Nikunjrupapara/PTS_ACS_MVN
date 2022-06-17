package com.yesmarketing.ptsacs.services.dto;

import com.yesmarketing.acsapi.model.EmailResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
public class CompositeServiceResponseDto implements Serializable {

	private static final long serialVersionUID = -6262063125718540105L;

	boolean profileSuccess;
	boolean servicesSuccess;
	boolean customResourcesSuccess;
	boolean emailsSuccess;

	String errorMessage;

	GetProfileResponseDto profile;

	Collection<CustomResourcesDto> customResources;

	Collection<EmailResponse> emails;
}
