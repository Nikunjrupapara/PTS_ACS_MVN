package com.yesmarketing.ptsacs.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateFormConfigDto implements Serializable {

	private static final long serialVersionUID = 8848735891941215619L;

	private String code;

	private String description;

	private LocalDateTime effectiveFrom;

	private LocalDateTime effectiveTo;

	private List<String> domains;

	private List<ServicesGrantedAuthorityDto> authorities;

	private List<FormJsonWebTokenDto> jwts;

}
