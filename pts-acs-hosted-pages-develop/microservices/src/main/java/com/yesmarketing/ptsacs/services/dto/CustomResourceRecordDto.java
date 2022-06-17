package com.yesmarketing.ptsacs.services.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonDeserialize(using = CustomResourceRecordDtoDeserializer.class)
public class CustomResourceRecordDto implements Serializable {
	private static final long serialVersionUID = 6169293937012436319L;
	Map<String, Object> fields;
//	String customResourceName;
}
