package com.yesmarketing.ptsacs.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class FormConfigDto extends UpdateFormConfigDto implements Serializable {
	private static final long serialVersionUID = -3697379713145735276L;

	String uuid;

	String company;

	private boolean enabled;

	LocalDateTime created;

	LocalDateTime lastUpdated;
}
