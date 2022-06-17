package com.yesmarketing.ptsacs.common.exception;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class DuplicateFormConfigException extends RuntimeException {

	private static final long serialVersionUID = 7599317297514497805L;

	String company;

	String code;
}
