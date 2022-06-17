package com.yesmarketing.ptsacs.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
public class CompositeServiceDto implements Serializable {

	private static final long serialVersionUID = 390049246597073005L;

	CreateProfileDto profile;

	SubscriptionDto services;

	Collection<CustomResourcesDto> customResources;

	Collection<EmailDto> emails;
}
