package com.dataaxle.pts.acscustompages.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@AllArgsConstructor
public class ListCustomResourceResponse implements CustomResourceResponse, Serializable {

	private static final long serialVersionUID = 9043748153270468768L;

	int pageNumber;

	int count;

	int totalRecords;

	boolean moreRecords;

	List<CustomResourceRecord> records;

	@Override
	public CustomResourceRecord getRecord() { return getRecord(0); }

	@Override
	public CustomResourceRecord getRecord(int index) {
		return records.get(index);
	}
}
