package com.yesmarketing.ptsacs.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yesmarketing.acsapi.admin.dto.CollectionDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

@Data
@NoArgsConstructor
public class FormConfigCollectionDto implements CollectionDto<FormConfigDto>, Serializable {

	public static final Supplier<FormConfigCollectionDto> SUPPLIER = FormConfigCollectionDto::new;

	private static final long serialVersionUID = 3983071181672428246L;

	private String company;

	private List<FormConfigDto> forms;

	@Override
	@JsonProperty(value = "forms")
	public List<FormConfigDto> getCollection() {
		return forms;
	}

	@Override
	public void setCollection(List<FormConfigDto> forms) {
		this.forms = forms;
	}
}
