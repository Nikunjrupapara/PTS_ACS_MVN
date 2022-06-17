package com.yesmarketing.ptsacs.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Deprecated
@Data
@NoArgsConstructor
public class CreateFormConfigDto extends UpdateFormConfigDto implements Serializable {
	private static final long serialVersionUID = 648700194681883381L;

	private String company;
}
