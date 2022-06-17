package com.dataaxle.pts.acscustompages.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCustomResourceResponse implements CustomResourceResponse, Serializable {

	private static final long serialVersionUID = -8968838051262747205L;

	CustomResourceRecord record;

	public GetCustomResourceResponse(CustomResourceRecord customResourceRecord) {
		this.record = customResourceRecord;
	}

	public Object getField(String name) {
		return record.getValue(name).orElse("");
	}

	@Override
	public CustomResourceRecord getRecord(int index) { return record; }
}
