package com.dataaxle.pts.acscustompages.model.validation;

import com.dataaxle.pts.acscustompages.model.form.ProfileDetails;
import com.dataaxle.pts.acscustompages.model.form.StringValueTypeMapper;
import com.dataaxle.pts.acscustompages.model.form.ValueTypeMapper;
import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import com.dataaxle.pts.acscustompages.utils.MessageCodes;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Value
public class ValidateEmail extends AbstractValidator implements Serializable {

	private static final long serialVersionUID = -7022684805517277922L;

	static String FIELD_NAME = "email";

	// TODO: need to make this a parameter to the constructor and have a separate String
	// validator for the confirmEmail field
	static String CONFIRM_EMAIL = "confirmEmail";

	boolean validateFormat;

	boolean confirmEmail;

	String confirmEmailName;

	ValueTypeMapper<String> valueTypeMapper = new StringValueTypeMapper();

	public ValidateEmail(String fieldName, boolean required, boolean validateFormat, boolean confirmEmail,
						 Integer minSize, Integer maxSize) {
		this(fieldName, required, validateFormat, confirmEmail, minSize, maxSize, "confirmEmail");
	}

	public ValidateEmail(String fieldName, boolean required, boolean validateFormat, boolean confirmEmail,
						 Integer minSize, Integer maxSize, String confirmEmailName) {
		super(fieldName, required, minSize, maxSize);
		this.confirmEmail = confirmEmail;
		this.validateFormat = validateFormat;
		this.confirmEmailName = confirmEmailName;
	}


	//@Override
	public boolean supports(Class<?> aClass) {
 		return ProfileDetails.class.isAssignableFrom(aClass);
	}

	//@Override
	public void validate(Object target, Errors errors) {
		//ProfileDetails profileDetails = (ProfileDetails) target;
		Map<String, Object> fields = (Map<String, Object>)target;
		String email = (String) fields.get(FIELD_NAME);
		/*if (required) {
			//ValidationUtils.rejectIfEmpty(errors, FIELD_NAME, MessageCodes.EMAIL_IS_REQUIRED);
			if (StringUtils.isEmpty(email)) {
				errors.rejectValue(FIELD_NAME, "isRequired");
			}
		}*/
		checkRequired(errors, email);
		if (errors.hasErrors()) {
			return;
		}
		//String email = profileDetails.getEmail();
		/*if (minSize != null && minSize > 0) {
			if (email.length() < emailMinSize) {
				errors.rejectValue(FIELD_NAME, MessageCodes.FIELD_TOO_SHORT);
			}
		}*/
		checkMinSize(errors, email);
		/*if (emailMaxSize != null && emailMaxSize > 0) {
			if (email.length() > emailMaxSize) {
				errors.rejectValue(FIELD_NAME, MessageCodes.FIELD_TOO_LONG);
			}
		}*/
		checkMaxSize(errors, email);
		if (validateFormat) {
			EmailValidator emailValidator = EmailValidator.getInstance(false, true);
			if (!emailValidator.isValid(/*profileDetails.getEmail()*/email)) {
				errors.rejectValue(FIELD_NAME, MessageCodes.FIELD_IS_INVALID);
			}
		}
		if (confirmEmail) {
			//ValidationUtils.rejectIfEmpty(errors, CONFIRM_EMAIL, MessageCodes.CONFIRMATION_EMAIL_IS_REQUIRED);
			//String confirmationEmail = profileDetails.getConfirmationEmail();
			String confirmationEmail = (String) fields.get(CONFIRM_EMAIL);
			if (StringUtils.isEmpty(confirmationEmail)) {
				errors.rejectValue(CONFIRM_EMAIL, "isRequired");
				return;
			}
			if (!confirmationEmail.equals(/*profileDetails.getEmail()*/email)) {
				errors.rejectValue(CONFIRM_EMAIL, MessageCodes.CONFIRMATION_EMAIL_NOT_MATCH);
			}
		}
	}

	@Override
	public ValidationResult validate(Map<String, Object> values) {
		String email = valueTypeMapper.mapFromForm(values.get(fieldName));
		ErrorCode errorCode;

		errorCode = checkRequired(email);
		if (!isValid(errorCode)) {
			return getResult(email, errorCode);
		}
		errorCode = checkMinSize(email);
		if (!isValid(errorCode)) {
			return getResult(email, errorCode);
		}
		errorCode = checkMaxSize(email);
		if (!isValid(errorCode)) {
			return getResult(email, errorCode);
		}

		boolean allowEmptyEmail = !required && StringUtils.isEmpty(email);

		if (validateFormat) {
			if (!allowEmptyEmail) {
				EmailValidator emailValidator = EmailValidator.getInstance(false, true);
				if (!emailValidator.isValid(email)) {
					return getResult(email, ErrorCode.INVALID);
				}
			}
		}
		if (confirmEmail) {
			String confirmationEmail = valueTypeMapper.mapFromForm(values.get(confirmEmailName));
			if (!allowEmptyEmail) {
				if (StringUtils.isEmpty(confirmationEmail)) {
					return getResult(confirmEmailName, confirmationEmail, ErrorCode.REQUIRED);
				}
			}
			if (!confirmationEmail.equals(email)) {
				return getResult(confirmEmailName, confirmationEmail, ErrorCode.NO_MATCH);
			}
		}
		return getResult(email, ErrorCode.VALID);
	}
}
