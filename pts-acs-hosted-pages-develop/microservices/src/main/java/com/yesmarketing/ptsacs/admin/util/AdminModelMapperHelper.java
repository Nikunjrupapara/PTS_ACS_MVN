package com.yesmarketing.ptsacs.admin.util;

import com.yesmarketing.acsapi.auth.dto.CreateCredentialModelDto;
import com.yesmarketing.acsapi.auth.dto.UpdateCredentialModelDto;
import com.yesmarketing.acsapi.auth.model.CredentialModel;
import com.yesmarketing.ptsacs.admin.dto.CustomerIdConfigDto;
import com.yesmarketing.ptsacs.common.util.AbstractModelMapperHelper;
import com.yesmarketing.ptsacs.services.model.CustomerIdDefinition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.annotation.PostConstruct;

@Component
public class AdminModelMapperHelper extends AbstractModelMapperHelper {

	public AdminModelMapperHelper(ModelMapper modelMapper) {
		super(modelMapper);
	}

	@PostConstruct
	@Override
	public void initialise() { addMappings(modelMapper); }

	private static void addMappings(ModelMapper modelMapper) {
		modelMapper.addMappings(new PropertyMap<CreateCredentialModelDto, CredentialModel>() {
			@Override
			protected void configure() {
				skip(destination.isEnabled());
				skip(destination.getCreated());
				skip(destination.getLastUpdated());
				skip(destination.getApiKeys());
			}
		});
		modelMapper.addMappings(new PropertyMap<UpdateCredentialModelDto, CredentialModel>() {
			@Override
			protected void configure() {
				skip(destination.getCompany());
				skip(destination.isEnabled());
				skip(destination.getCreated());
				skip(destination.getLastUpdated());
				skip(destination.getApiKeys());
			}
		});
		modelMapper.typeMap(CustomerIdDefinition.class, CustomerIdConfigDto.class).addMappings(mapper -> {
			mapper.map(CustomerIdDefinition::getFunction, CustomerIdConfigDto::setHashFunction);
		});
		modelMapper.typeMap(CustomerIdConfigDto.class, CustomerIdDefinition.class).addMappings(mapper -> {
			mapper.map(CustomerIdConfigDto::getHashFunction, CustomerIdDefinition::setFunction);
		});

		Converter<Instant, LocalDateTime> instantToLdt = context -> LocalDateTime.ofInstant(context.getSource(), ZoneId.of("UTC"));

		modelMapper.addConverter(instantToLdt);
	}
}
