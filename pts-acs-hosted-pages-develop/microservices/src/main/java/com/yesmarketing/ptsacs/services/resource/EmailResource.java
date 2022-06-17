package com.yesmarketing.ptsacs.services.resource;

import com.yesmarketing.acsapi.model.EmailResponse;
import com.yesmarketing.ptsacs.services.authentication.CurrentForm;
import com.yesmarketing.ptsacs.services.dto.EmailDto;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.service.EmailService;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import com.yesmarketing.ptsacs.services.util.ServicesModelMapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/services/email")
public class EmailResource {

	private static final Logger LOG = LoggerFactory.getLogger(EmailResource.class);

	private final EmailService emailService;
	private final ServicesModelMapperHelper servicesModelMapperHelper;

	@Autowired
	public EmailResource(EmailService emailService, ServicesModelMapperHelper servicesModelMapperHelper) {
		this.emailService = emailService;
		this.servicesModelMapperHelper = servicesModelMapperHelper;
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PostMapping(value="/send",consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_TRIGGER_EMAIL_NM + "')")
	public EmailResponse emailSentNoTransactionalApi(@RequestBody EmailDto email,
								   @CurrentForm FormConfig formConfig
	) {
		return emailService.validateAndSendEmail(formConfig.getCompany(),email);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value = "/{eventId}/status/{PKey}")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_CHECK_TRIGGERED_EMAIL_STATUS_NM + "')")
	public EmailResponse checkTriggeredEmailStatus(@CurrentForm FormConfig formConfig,
												   @PathVariable(name = "eventId") String eventId,
												   @PathVariable(name = "PKey") String PKey) {
		return emailService.getEmailStatus(formConfig.getCompany(), eventId, PKey);
	}

}
