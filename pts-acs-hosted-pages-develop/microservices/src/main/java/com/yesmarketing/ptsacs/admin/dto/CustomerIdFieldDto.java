package com.yesmarketing.ptsacs.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CustomerIdFieldDto implements Serializable {
	private static final long serialVersionUID = -63984957741664595L;

	String name;

	boolean required;
}
