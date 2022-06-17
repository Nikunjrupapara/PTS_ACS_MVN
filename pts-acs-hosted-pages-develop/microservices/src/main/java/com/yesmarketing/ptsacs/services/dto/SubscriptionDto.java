package com.yesmarketing.ptsacs.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto implements Serializable {
    private static final long serialVersionUID = 1619191917111415121L;
    List<String> add;
    List<String> remove;
}
