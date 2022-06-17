package com.yesmarketing.ptsacs.services.service;

import java.util.List;

public interface CustomResourceListService {

    List<String> getAssociatedList(String company);

    List<String> getUnassociatedList(String company);


}
