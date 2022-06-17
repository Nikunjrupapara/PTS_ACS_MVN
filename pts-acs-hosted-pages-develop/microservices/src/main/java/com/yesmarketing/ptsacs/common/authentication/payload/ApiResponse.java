package com.yesmarketing.ptsacs.common.authentication.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {

	private Boolean success;

	private String message;

}
