package com.yesmarketing.ptsacs.services.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
@JsonPropertyOrder(value = { "profile", "currentServices" })
public class GetProfileResponseDto implements Serializable {

    private GetProfileDto profile;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Collection<GetServiceSubscriptionDto> currentServices;
}
