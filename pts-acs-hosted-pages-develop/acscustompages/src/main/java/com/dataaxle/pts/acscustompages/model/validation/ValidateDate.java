package com.dataaxle.pts.acscustompages.model.validation;

import com.dataaxle.pts.acscustompages.model.form.StringValueTypeMapper;
import com.dataaxle.pts.acscustompages.model.form.ValueTypeMapper;
import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Map;

@Slf4j
@Value
@EqualsAndHashCode(callSuper = true)
public class ValidateDate extends AbstractValidator {

	private static final long serialVersionUID = -1815151649999388782L;

	String pattern;

	LocalDate minDate;

	LocalDate maxDate;

	ValueTypeMapper<String> valueTypeMapper = new StringValueTypeMapper();

	public ValidateDate(String fieldName, boolean required) {
		super(fieldName, required, null, null);
		this.minDate = null;
		this.maxDate = null;
		this.pattern = "uuuu/MM/dd";
	}


	public ValidateDate(String fieldName, boolean required, String pattern) {
		super(fieldName, required, null, null);
		this.minDate = null;
		this.maxDate = null;
		this.pattern = pattern;
	}

	public ValidateDate(String fieldName, boolean required, String pattern, String minDate, String maxDate) {
		super(fieldName, required, null, null);
		this.pattern = pattern;
		DateTimeFormatter fmt = getDateTimeFormatter(pattern);
		this.minDate = StringUtils.hasText(minDate) ? LocalDate.parse(minDate, fmt) : null;
		this.maxDate = StringUtils.hasText(maxDate) ? LocalDate.parse(maxDate, fmt) : null;
	}

	private DateTimeFormatter getDateTimeFormatter(String pattern) {
		return DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT);
	}

	@Override
	public ValidationResult validate(Map<String, Object> values) {
		String str = valueTypeMapper.mapFromForm(values.get(fieldName));
		if (str == null) {
			str = "";
		}
		ErrorCode errorCode;

		if (!required && StringUtils.isEmpty(str)) {
			return new ValidationResult(fieldName, str, true, ErrorCode.VALID);
		}

		errorCode = checkRequired(str);
		if (!isValid(errorCode)) {
			return getResult(str, errorCode);
		}

		LocalDate localDate = null;
		try {
			localDate = LocalDate.parse(str, getDateTimeFormatter(pattern));
		} catch (DateTimeParseException e) {
			LOG.error("Error parsing date string {} using pattern {}", str, pattern, e);
			return new ValidationResult(fieldName, str, false, ErrorCode.INVALID);
		}

		if (minDate != null) {
			if (localDate.isBefore(minDate)) {
				return new ValidationResult(fieldName, str, false, ErrorCode.TOO_LOW);
			}
		}

		if (maxDate != null) {
			if (localDate.isAfter(maxDate)) {
				return new ValidationResult(fieldName, str, false, ErrorCode.TOO_HIGH);
			}
		}

		return new ValidationResult(fieldName, str, true, ErrorCode.VALID);
	}
}
