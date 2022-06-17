package com.yesmarketing.ptsacs.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GetServiceSubscriptionDto implements Serializable {

    private String name;

    private String label;

    private LocalDateTime created;
}
