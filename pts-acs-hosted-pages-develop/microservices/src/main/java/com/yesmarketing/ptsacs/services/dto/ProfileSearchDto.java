package com.yesmarketing.ptsacs.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSearchDto implements Serializable {

    private static final long serialVersionUID = 7649394977714405829L;

    private Map<String, String> searchFields;
}
