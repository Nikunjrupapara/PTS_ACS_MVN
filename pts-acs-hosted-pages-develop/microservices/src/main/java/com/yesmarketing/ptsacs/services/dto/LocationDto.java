package com.yesmarketing.ptsacs.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationDto {

	String address1;

	String address2;

	String address3;

	String address4;

	String city;

	String countryCode;

	String stateCode;

	String zipCode;
}
