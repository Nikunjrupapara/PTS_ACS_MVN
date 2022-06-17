package com.yesmarketing.ptsacs.services.service;

import org.springframework.boot.actuate.health.Health;
import java.util.List;

public interface StatusService {

    Health getStatus(String company, String lookupValue);

    List<String> getCompanies();

}
