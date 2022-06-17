package com.yesmarketing.ptsacs.admin.dto;

import com.yesmarketing.ptsacs.services.enums.HashingFunction;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerIdConfigDto implements Serializable {
	private static final long serialVersionUID = -6305517583265796659L;

	private List<CustomerIdFieldDto> fields;

	private String delimiter;

	private HashingFunction hashFunction;
}
