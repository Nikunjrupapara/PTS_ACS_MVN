package com.yesmarketing.ptsacs.services.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class MissingCustomerIdFieldValueException extends RuntimeException {

	private static final long serialVersionUID = -5533197639012005869L;

	String company;

	String fieldName;
}
