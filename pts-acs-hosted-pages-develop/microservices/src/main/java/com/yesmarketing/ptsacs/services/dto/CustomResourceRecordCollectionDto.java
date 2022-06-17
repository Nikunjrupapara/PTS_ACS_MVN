package com.yesmarketing.ptsacs.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yesmarketing.acsapi.admin.model.CustomResourceRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomResourceRecordCollectionDto {

	int count = 0;

	int total = 0;

	boolean moreRecords = false;

	List<CustomResourceRecord> records;

	public CustomResourceRecordCollectionDto(List<CustomResourceRecord> records) {
		this.records = records;
	}
}
