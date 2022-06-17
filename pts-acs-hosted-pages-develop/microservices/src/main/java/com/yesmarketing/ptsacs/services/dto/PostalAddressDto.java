package com.yesmarketing.ptsacs.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostalAddressDto {

	Boolean addrDefined;

	Long addrErrorCount;

	String addrLastCheck;

	String addrQuality;

	String line1;

	String line2;

	String line3;

	String line4;

	String line5;

	String line6;

	String serialized;
}
