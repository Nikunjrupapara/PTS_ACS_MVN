package com.yesmarketing.ptsacs.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSearchByFilterDto extends ProfileSearchDto {

    private String filterName;

}
