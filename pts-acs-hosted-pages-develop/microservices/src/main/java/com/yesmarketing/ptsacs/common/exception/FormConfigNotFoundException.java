package com.yesmarketing.ptsacs.common.exception;

import lombok.Value;

@Value
public class FormConfigNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5457641586658094047L;

	String uuid;
}
