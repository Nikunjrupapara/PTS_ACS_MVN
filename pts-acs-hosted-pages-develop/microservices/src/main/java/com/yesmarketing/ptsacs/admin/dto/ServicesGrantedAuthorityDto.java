package com.yesmarketing.ptsacs.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ServicesGrantedAuthorityDto implements Serializable {
	private static final long serialVersionUID = -7390750830588355339L;

	String authority;

	String description;
}
