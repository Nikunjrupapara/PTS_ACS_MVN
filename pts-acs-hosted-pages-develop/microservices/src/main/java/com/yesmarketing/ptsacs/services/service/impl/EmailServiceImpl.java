package com.yesmarketing.ptsacs.services.service.impl;

import com.yesmarketing.acsapi.dao.ProfileMetadataDao;
import com.yesmarketing.acsapi.exception.SagasRuntimeException;
import com.yesmarketing.acsapi.model.Email;
import com.yesmarketing.acsapi.model.EmailResponse;
import com.yesmarketing.acsapi.sagas.EmailSagasImpl;
import com.yesmarketing.acsapi.sagas.SagasHelper;
import com.yesmarketing.acsapi.sagas.SubscriptionSagas;
import com.yesmarketing.ptsacs.common.exception.BadRequestException;
import com.yesmarketing.ptsacs.services.dto.EmailDto;
import com.yesmarketing.ptsacs.services.service.CustomerIdService;
import com.yesmarketing.ptsacs.services.service.EmailService;
import com.yesmarketing.ptsacs.services.util.MessageKeys;
import com.yesmarketing.ptsacs.services.util.ServicesModelMapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Primary
@Service
public class EmailServiceImpl extends EmailSagasImpl implements EmailService {

	private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

	private final ServicesModelMapperHelper servicesModelMapperHelper;

	@Autowired
	public EmailServiceImpl(SagasHelper sagasHelper, ServicesModelMapperHelper servicesModelMapperHelper) {
		super(sagasHelper);
		this.servicesModelMapperHelper = servicesModelMapperHelper;
	}

	@Override
	public EmailResponse validateAndSendEmail(String company, EmailDto request) {
		validateSendEmail(company, request);
		return sendEmail(company, request);
	}

	@Override
	public EmailResponse sendEmail(String company, EmailDto request){
		try {
			Email emailInput = servicesModelMapperHelper.fromDto(request, Email.class);

			return super.sendEmail(company, request.eventId, emailInput);
		}catch(SagasRuntimeException exception){
			LOG.info(exception.getCause().getLocalizedMessage());
			throw new BadRequestException(exception.getMessage(),exception.getCause());
		}
	}

	@Override
	public void validateSendEmail(String company, EmailDto request) {
		if (StringUtils.isEmpty(request.getEmail())) {
			throw new BadRequestException("Missing Email address.");
		}

		if (CollectionUtils.isEmpty(request.getCtx())) {
			throw new BadRequestException("Missing transactional event context.");
		}
	}

}
