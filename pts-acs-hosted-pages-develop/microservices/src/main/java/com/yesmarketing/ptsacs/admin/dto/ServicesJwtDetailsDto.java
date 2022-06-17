package com.yesmarketing.ptsacs.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class ServicesJwtDetailsDto {
    private static final long serialVersionUID = 8260323191556034194L;

    FormClaimDto claim;

    LocalDateTime notBefore;

    LocalDateTime expirationTime;

    String jti;

    Date issuedAt;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String overrideKey;
}
