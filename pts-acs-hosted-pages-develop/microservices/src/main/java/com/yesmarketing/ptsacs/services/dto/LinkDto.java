package com.yesmarketing.ptsacs.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class LinkDto implements Serializable {

	private static final long serialVersionUID = -4524835274501505183L;

	private String href;
}
