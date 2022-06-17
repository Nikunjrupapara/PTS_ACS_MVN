package com.yesmarketing.ptsacs.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ServicesTokenDto implements Serializable {
    private static final long serialVersionUID = -490018737186376092L;

    String token;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String overrideKey;
}
