package com.yesmarketing.ptsacs.services.util;

import com.yesmarketing.acsapi.admin.model.CustomResourceRecord;
import com.yesmarketing.acsapi.model.Email;
import com.yesmarketing.acsapi.model.Location;
import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ProfileSubscriptionModel;
import com.yesmarketing.acsapi.model.ServiceModel;
import com.yesmarketing.ptsacs.common.util.AbstractModelMapperHelper;
import com.yesmarketing.ptsacs.services.dto.CreateProfileDto;
import com.yesmarketing.ptsacs.services.dto.CustomResourceRecordDto;
import com.yesmarketing.ptsacs.services.dto.EmailDto;
import com.yesmarketing.ptsacs.services.dto.GetProfileDto;
import com.yesmarketing.ptsacs.services.dto.GetServiceSubscriptionDto;
import com.yesmarketing.ptsacs.services.dto.LocationDto;
import org.joda.time.DateTime;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.module.jsr310.Jsr310Module;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.annotation.PostConstruct;

@Component
public class ServicesModelMapperHelper extends AbstractModelMapperHelper {

	public ServicesModelMapperHelper(ModelMapper modelMapper) {
		super(modelMapper);
	}

	@PostConstruct
	@Override
	public void initialise() { addMappings(modelMapper); }

	private static void addMappings(ModelMapper modelMapper) {
		modelMapper.registerModule(new Jsr310Module());
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.getConfiguration().setFullTypeMatchingRequired(false);

		// Convert JodaTime Datetime to Java 8 ZonedDateTime
		Converter<DateTime, ZonedDateTime> jodaToJava = ctx -> {
			DateTime dt = ctx.getSource();
			Instant instant = Instant.ofEpochMilli(dt.getMillis());
			ZoneId zoneId = ZoneId.of(dt.getZone().getID(), ZoneId.SHORT_IDS);
			return ZonedDateTime.ofInstant(instant, zoneId);
		};

		Converter<org.joda.time.DateTime, java.time.LocalDateTime> jodaToJavaLocalDateTime = new AbstractConverter<DateTime, LocalDateTime>() {
			protected java.time.LocalDateTime convert(DateTime source) {
				if(source==null)return null;
				Instant instant = Instant.ofEpochMilli(source.getMillis());
				ZoneId zoneId = ZoneId.of(source.getZone().getID(), ZoneId.SHORT_IDS);
				return LocalDateTime.ofInstant(instant, ZoneId.of(zoneId.getId()));
			}
		};
		modelMapper.addConverter(jodaToJavaLocalDateTime);


		modelMapper.typeMap(CreateProfileDto.class, ProfileModel.class).addMappings(mapper -> {
			mapper.map(CreateProfileDto::getCustomFields, ProfileModel::setCustomFields);
			mapper.skip(ProfileModel::setAttributes);
			mapper.skip(ProfileModel::setPKey);
			mapper.skip(ProfileModel::setAge);
			mapper.skip(ProfileModel::setAcsId);
			mapper.skip(ProfileModel::setCreated);
			mapper.skip(ProfileModel::setCryptedId);
			mapper.skip(ProfileModel::setCusCustomerId);
			mapper.skip(ProfileModel::setDomain);
			mapper.skip(ProfileModel::setGeoUnit);
			mapper.skip(ProfileModel::setHref);
			mapper.skip(ProfileModel::setIsExternal);
			mapper.skip(ProfileModel::setSubscriptions);
			mapper.skip(ProfileModel::setTitle);
		}).setPostConverter(context -> {
			CreateProfileDto createProfileDto = context.getSource();
			LocationDto locationDto = createProfileDto.getLocation();
			ProfileModel profileModel = context.getDestination();
			Location location = profileModel.getLocation();
			if (locationDto != null) {
				if (StringUtils.hasText(locationDto.getZipCode()) && !StringUtils.hasText(location.getZipCode())) {
					profileModel.setAttribute("location.zipCode", locationDto.getZipCode());
				}
			}
			return profileModel;

		});


		modelMapper.typeMap(ProfileModel.class, GetProfileDto.class).addMappings(mapper -> {
			mapper.map(ProfileModel::getCustomFields, GetProfileDto::setCustomFields);
		});
		modelMapper.typeMap(ProfileSubscriptionModel.class, GetServiceSubscriptionDto.class).addMappings(mapper -> {
			//mapper.map(src -> src.getService().getCreated(), GetServiceSubscriptionDto::setCreated);
			mapper.map(ProfileSubscriptionModel::getCreated, GetServiceSubscriptionDto::setCreated);
			mapper.map(src -> src.getService().getLabel(), GetServiceSubscriptionDto::setLabel);
			mapper.map(src -> src.getService().getName(), GetServiceSubscriptionDto::setName);
		});

		modelMapper.typeMap(ServiceModel.class, GetServiceSubscriptionDto.class).addMappings(mapper -> {
			mapper.map(ServiceModel::getCreated, GetServiceSubscriptionDto::setCreated);
			mapper.map(ServiceModel::getLabel, GetServiceSubscriptionDto::setLabel);
			mapper.map(ServiceModel::getName, GetServiceSubscriptionDto::setName);
		});

		modelMapper.typeMap(EmailDto.class, Email.class).addMappings(mapper -> {
			mapper.map(EmailDto::getEmail, Email::setEmail);
			mapper.map(EmailDto::getCtx, Email::setCtx);
			mapper.map(EmailDto::getExpiration, Email::setExpiration);
			mapper.map(EmailDto::getScheduled, Email::setScheduled);
		});

		modelMapper.typeMap(CustomResourceRecordDto.class, CustomResourceRecord.class).addMappings(mapper->{
			mapper.map(CustomResourceRecordDto::getFields,CustomResourceRecord::setValues);
			mapper.skip(CustomResourceRecord::setCustomResourceName);
		});

		modelMapper.typeMap(CustomResourceRecord.class,CustomResourceRecordDto.class).addMappings(mapper->{
			mapper.map(CustomResourceRecord::getValues,CustomResourceRecordDto::setFields);
		});

	}
}
