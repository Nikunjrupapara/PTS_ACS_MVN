package com.yesmarketing.ptsacs.services.service;

import com.yesmarketing.ptsacs.services.dto.CompositeServiceDto;
import com.yesmarketing.ptsacs.services.dto.CompositeServiceResponseDto;
import com.yesmarketing.ptsacs.services.model.FormConfig;

public interface CompositeService {

    public CompositeServiceResponseDto updateOrCreate(
            CompositeServiceDto compositeServiceDto, FormConfig formConfig);
    public CompositeServiceResponseDto updateByHash(
            CompositeServiceDto compositeServiceDto, String customerIdHash, FormConfig formConfig);

    public void validateUpdateOrCreate(
            CompositeServiceDto compositeServiceDto, FormConfig formConfig);
    public void validateUpdateByHash(
            CompositeServiceDto compositeServiceDto, String customerIdHash, FormConfig formConfig);


}
