package com.yesmarketing.ptsacs.services.service.impl;

import com.yesmarketing.acsapi.sagas.SagasHelper;
import com.yesmarketing.ptsacs.services.service.CustomResourceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;
import java.util.List;

@Service
public class CustomResourceListServiceImpl extends CustomResourceServiceBase
        implements CustomResourceListService {

    @Autowired
    public CustomResourceListServiceImpl(SagasHelper sagasHelper){
        super(sagasHelper);
    }

    @Cacheable("associatedList")
    public List<String> getAssociatedList(String company){
        String uri = "/profileAndServicesExt";
        String getResourcesResponse = submitRequestToAdobe(company, uri, HttpMethod.GET);
        try {
            return parseCustomResources(getResourcesResponse);
        }catch(IOException e){
            throw new ServerErrorException("issue with ACS profileAndServicesExt call");
        }
    }

    @Cacheable("unassociatedList")
    public List<String> getUnassociatedList(String company){
        String uri = "/customResources";
        String getResourcesResponse = submitRequestToAdobe(company, uri, HttpMethod.GET);
        try {
            return parseCustomResources(getResourcesResponse);
        }catch(IOException e){
            throw new ServerErrorException("issue with ACS customResources call");
        }
    }


}
