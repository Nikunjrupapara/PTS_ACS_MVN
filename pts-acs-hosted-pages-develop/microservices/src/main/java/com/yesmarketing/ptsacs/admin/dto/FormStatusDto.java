package com.yesmarketing.ptsacs.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FormStatusDto implements Serializable {

	private static final long serialVersionUID = 1813940468306196128L;

	boolean enabled;
}
