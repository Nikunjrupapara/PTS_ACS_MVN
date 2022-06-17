package com.dataaxle.pts.acscustompages.model.validation;

import com.dataaxle.pts.acscustompages.model.form.ProfileDetails;
import com.dataaxle.pts.acscustompages.model.form.StringValueTypeMapper;
import com.dataaxle.pts.acscustompages.model.form.ValueTypeMapper;
import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import com.dataaxle.pts.acscustompages.utils.MessageCodes;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Pattern;

@EqualsAndHashCode(callSuper = true)
@Value
public class ValidateString extends AbstractValidator implements Serializable {

	private static final long serialVersionUID = 7205967096373941474L;

	String regex;

	ValueTypeMapper<String> valueTypeMapper = new StringValueTypeMapper();

	public ValidateString(String fieldName, boolean required, Integer minSize, Integer maxSize, String regex) {
		super(fieldName, required, minSize, maxSize);
		String tmpRegex = regex;
		if (tmpRegex.contains(":min")) {
			tmpRegex = tmpRegex.replace(":min", minSize.toString());
		}
		if (tmpRegex.contains(":max")) {
			tmpRegex = tmpRegex.replace(":max", maxSize.toString());
		}
		this.regex = tmpRegex;
	}


	//@Override
	public boolean supports(Class<?> aClass) {
		return Map.class.isAssignableFrom(aClass);
	}

	//@Override
	public void validate(Object target, Errors errors) {
		//ProfileDetails profileDetails = (ProfileDetails)target;
		Map<String, Object> fields = (Map<String, Object>)target;
		String str = (String)fields.get(fieldName);//profileDetails.getFirstName();
		/*if (required) {
			//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", messageCode("IsRequired"));
			if (StringUtils.isEmpty(str)) {
				recordError(errors, MessageCodes.FIELD_IS_REQUIRED);
			}
		}*/
		checkRequired(errors, str);
		if (errors.hasErrors()) {
			return;
		}
		checkMinSize(errors, str);
		/*if (minSize != null && minSize > 0) {
			if (str.length() < minSize) {
				recordError(errors, MessageCodes.FIELD_TOO_SHORT);
			}
		}*/
		checkMaxSize(errors, str);
		/*if (maxSize != null && maxSize > 0) {
			if (str.length() > maxSize) {
				recordError(errors, MessageCodes.FIELD_TOO_LONG);
			}
		}*/
		if (StringUtils.hasText(regex)) {
			Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
			if (!pattern.matcher(str).matches()) {
				recordError(errors, MessageCodes.FIELD_HAS_INVALID_FORMAT);
			}
		}
	}

	@Override
	public ValidationResult validate(Map<String, Object> values) {
		String str = valueTypeMapper.mapFromForm(values.get(fieldName));
		if (str == null) {
			str = "";
		}
		ErrorCode errorCode;

		errorCode = checkRequired(str);
		if (!isValid(errorCode)) {
			return getResult(str, errorCode);
		}
		errorCode = checkMinSize(str);
		if (!isValid(errorCode)) {
			return getResult(str, errorCode);
		}
		errorCode = checkMaxSize(str);
		if (!isValid(errorCode)) {
			return getResult(str, errorCode);
		}
		if (StringUtils.hasText(regex)) {
			Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
			if (!pattern.matcher(str).matches()) {
				return getResult(str, ErrorCode.INVALID);
			}
		}
		return getResult(str, ErrorCode.VALID);
	}
}
