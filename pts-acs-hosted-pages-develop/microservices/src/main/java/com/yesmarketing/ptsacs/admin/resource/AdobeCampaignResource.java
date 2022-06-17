package com.yesmarketing.ptsacs.admin.resource;

import com.yesmarketing.acsapi.model.ResourceMetadata;
import com.yesmarketing.acsapi.sagas.MetadataSagas;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/acs")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdobeCampaignResource {

	private final MetadataSagas metadataSagas;

	public AdobeCampaignResource(MetadataSagas metadataSagas) {
		this.metadataSagas = metadataSagas;
	}

	@GetMapping(path = "/{company}/{type}", produces = { MediaType.APPLICATION_JSON_VALUE})
	ResourceMetadata getProfileMetadata(@PathVariable String company, @PathVariable String type) {
		return metadataSagas.retrieveMetadata(type, company);
	}
}
