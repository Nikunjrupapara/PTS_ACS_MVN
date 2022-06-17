package com.dataaxle.pts.acscustompages.exception;

public class UnsupportedNodeTypeException extends CustomPagesException {
	private static final long serialVersionUID = 7583775574964245343L;

	private final String name;

	public UnsupportedNodeTypeException(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getMessage() {
		return name;
	}
}
