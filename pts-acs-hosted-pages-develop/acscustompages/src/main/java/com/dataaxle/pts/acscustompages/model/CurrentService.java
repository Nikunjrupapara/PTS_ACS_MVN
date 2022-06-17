package com.dataaxle.pts.acscustompages.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentService implements Serializable {

	private static final long serialVersionUID = -7822372535517135821L;

	String name;

	String label;

	LocalDateTime created;

	public CurrentService(CurrentService original) {
		this.name = original.name;
		this.label = original.label;
		this.created = original.created;
	}
}
