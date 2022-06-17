package com.yesmarketing.ptsacs.common.exception;

import com.yesmarketing.ptsacs.services.enums.HashingFunction;

public class UnknownHashingFunctionException extends RuntimeException {

	private static final long serialVersionUID = 5960690533575452012L;

	private final HashingFunction hashingFunction;

	public UnknownHashingFunctionException(HashingFunction function) {
		super();
		this.hashingFunction = function;
	}

	public HashingFunction getHashingFunction() {
		return hashingFunction;
	}
}
