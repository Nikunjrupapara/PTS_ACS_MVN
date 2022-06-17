package com.dataaxle.pts.acscustompages.model.validation;

import com.dataaxle.pts.acscustompages.model.form.StringValueTypeMapper;
import com.dataaxle.pts.acscustompages.model.form.ValueTypeMapper;
import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Value
@Slf4j
public class ValidatePhoneNumber extends AbstractValidator implements Serializable {

	private static final long serialVersionUID = 7096043105506827441L;

	boolean confirmPhone;

	String confirmPhoneName;

	List<String> countries;

	ValueTypeMapper<String> valueTypeMapper = new StringValueTypeMapper();

	public ValidatePhoneNumber(String fieldName, boolean required, boolean confirmPhone, String confirmPhoneName,
							   List<String> countries) {

		super(fieldName, required, null, null);
		this.confirmPhone = confirmPhone;
		this.confirmPhoneName = confirmPhoneName;
		List<String> countriesTemp = countries;
		if (countriesTemp == null || countriesTemp.isEmpty()) {
			countriesTemp = List.of("US");
		}
		this.countries = countriesTemp;
	}

	@Override
	public ValidationResult validate(Map<String, Object> values) {
		String phone = valueTypeMapper.mapFromForm(values.get(fieldName));
		ErrorCode errorCode;

		errorCode = checkRequired(phone);
		if (!isValid(errorCode)) {
			return getResult(phone, errorCode);
		}

		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		Phonenumber.PhoneNumber number;
		try {
			// Attempt to parse the input.  Will throw an exception (caught below) if the value is invalid.
			number = phoneUtil.parse(phone, countries.get(0));
		} catch (NumberParseException e) {
			LOG.error("Error parsing phone number: {}", phone);
			return getResult(phone, ErrorCode.INVALID);
		}
		// Performs a basic check to assess if this is a valid phone number - quicker than a full check
		if (!phoneUtil.isPossibleNumber(number)) {
			return getResult(phone, ErrorCode.INVALID);
		}
		// If the basic check passes, perform a more detailed one
		if (!phoneUtil.isValidNumber(number)) {
			return getResult(phone, ErrorCode.INVALID);
		}
		if (confirmPhone) {
			String confirmationPhone = valueTypeMapper.mapFromForm(values.get(confirmPhoneName));
			if (StringUtils.isEmpty(confirmationPhone)) {
				return getResult(confirmPhoneName, confirmationPhone, ErrorCode.REQUIRED);
			}
			PhoneNumberUtil.MatchType matchType = phoneUtil.isNumberMatch(number, confirmationPhone);
			switch (matchType) {

				case NOT_A_NUMBER:
				case NO_MATCH:
					return getResult(confirmationPhone, ErrorCode.NO_MATCH);
				case SHORT_NSN_MATCH:
				case NSN_MATCH:
				case EXACT_MATCH:
					return getResult(phone, ErrorCode.VALID);
			}
		}
		return getResult(phone, ErrorCode.VALID);
	}
}
