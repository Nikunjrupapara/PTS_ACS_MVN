package com.dataaxle.pts.acscustompages.model.converter;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.ADOBE_FORMAT;

import lombok.Value;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;

@Value
public class DateTimeConverter implements AdobeConverter<String> {

	@Override
	public String convert(Object value) {
		String valueStr = value.toString();
		if (StringUtils.isEmpty(valueStr)) {
			return "";
		}
		LocalDateTime inputDate = LocalDateTime.parse(valueStr);
		return inputDate.format(ADOBE_FORMAT);
	}
}
