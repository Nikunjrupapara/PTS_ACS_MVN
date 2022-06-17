package com.dataaxle.pts.acscustompages.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
@AllArgsConstructor
public class HiddenService implements Serializable {

	private static final long serialVersionUID = 2839702104063514208L;

	String name;

	ServiceAction serviceAction;
}
