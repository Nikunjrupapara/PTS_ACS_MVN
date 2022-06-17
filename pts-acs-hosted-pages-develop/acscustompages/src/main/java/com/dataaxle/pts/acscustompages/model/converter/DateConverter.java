package com.dataaxle.pts.acscustompages.model.converter;

import lombok.Value;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

@Value
public class DateConverter implements AdobeConverter<String> {

	DateTimeFormatter adobeFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

	@Override
	public String convert(Object value) {
		String valueStr = value.toString();
		if (StringUtils.isEmpty(valueStr)) {
			return "";
		}
		LocalDate inputDate = LocalDate.parse(valueStr);
		return inputDate.format(adobeFormat);
	}
}
