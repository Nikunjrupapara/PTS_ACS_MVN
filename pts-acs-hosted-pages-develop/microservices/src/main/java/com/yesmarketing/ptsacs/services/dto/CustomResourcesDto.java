package com.yesmarketing.ptsacs.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@Data
public class CustomResourcesDto {

    private String name;

    private Collection<CustomResourceRecordDto> records;


}
