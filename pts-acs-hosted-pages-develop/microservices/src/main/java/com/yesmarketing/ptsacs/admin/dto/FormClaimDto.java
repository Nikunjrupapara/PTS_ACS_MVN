package com.yesmarketing.ptsacs.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FormClaimDto implements Serializable {

    private static final long serialVersionUID = -2755309952802146430L;

    String company;

    String uuid;
}
