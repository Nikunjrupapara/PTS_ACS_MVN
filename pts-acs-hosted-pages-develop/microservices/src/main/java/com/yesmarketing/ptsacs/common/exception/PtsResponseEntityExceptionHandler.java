package com.yesmarketing.ptsacs.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmail.api.common.exception.RestResponseEntityExceptionHandler;
import com.yesmail.api.common.representations.ErrorResponse;
import com.yesmail.api.common.util.CommonMessageKeys;
import com.yesmarketing.acsapi.exception.SagasUnauthorizedException;
import com.yesmarketing.acsapi.exception.SerializationUnknownFieldsException;
import com.yesmarketing.acsapi.exception.TriggeredEmailStatusNotFoundException;
import com.yesmarketing.ptsacs.admin.util.AdminMessageKeys;
import com.yesmarketing.ptsacs.services.exception.CustomerIdDefinitionMismatchException;
import com.yesmarketing.ptsacs.services.exception.MissingCustomerIdFieldValueException;
import com.yesmarketing.ptsacs.services.util.MessageKeys;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.MappingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class PtsResponseEntityExceptionHandler extends RestResponseEntityExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleObjectNotFound(ObjectNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), getMessage(e), getMoreInfo(e));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e) {
        String message = messageService.getMessage(MessageKeys.INVALID_ADMIN_CREDENTIALS_MSG);
        String moreInfo = messageService.getMessage(MessageKeys.INVALID_ADMIN_CREDENTIALS_INFO);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message, moreInfo);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = messageService.getMessage(MessageKeys.INVALID_REQUEST_PARAMETER_MSG);
        String moreInfo = messageService.getMessage(MessageKeys.INVALID_REQUEST_PARAMETER_INFO);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, moreInfo);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiKeyNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleApiKeyNotSupportedException(ApiKeyNotSupportedException ex) {
        String message = messageService.getMessage(AdminMessageKeys.API_KEY_NOT_SUPPORTED_MSG);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), message);
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoCustomerIdDefinitionException.class)
    public ResponseEntity<ErrorResponse> handleNoCustomerIdDefinitionException(NoCustomerIdDefinitionException ex) {
        String message = messageService.getMessage(AdminMessageKeys.CUSTOMER_ID_DEFINITION_NOT_FOUND_MSG, ex.getCompany());
        String moreInfo = messageService.getMessage(AdminMessageKeys.CUSTOMER_ID_DEFINITION_NOT_FOUND_INFO);
        return buildResponse(message, moreInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingCustomerIdFieldValueException.class)
    public ResponseEntity<ErrorResponse> handleMissingCustomerIdFieldValueException(MissingCustomerIdFieldValueException ex) {
        String message = messageService.getMessage(AdminMessageKeys.MISSING_CUSTOMER_ID_VALUE_MSG, ex.getCompany(), ex.getFieldName());
        String moreInfo = messageService.getMessage(AdminMessageKeys.MISSING_CUSTOMER_ID_VALUE_INFO);
        return buildResponse(message, moreInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerIdDefinitionMismatchException.class)
    public ResponseEntity<ErrorResponse> handleCustomerIdDefinitionMismatchException(CustomerIdDefinitionMismatchException ex) {
        LOG.error("Error: {}", ex.getMessage(), ex);
        String message = messageService.getMessage(AdminMessageKeys.CUSTOMER_ID_DEFINITION_MISMATCH_MSG);
        String moreInfo = messageService.getMessage(AdminMessageKeys.CUSTOMER_ID_DEFINITION_MISMATCH_INFO);
        return buildResponse(message, moreInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TriggeredEmailStatusNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTriggeredEmailStatusNotFoundException(TriggeredEmailStatusNotFoundException e) {
        String message = messageService.getMessage(AdminMessageKeys.TRIGGERED_EMAIL_STATUS_NOT_FOUND_MSG, e.getEventId(),
            e.getPkey());
        String moreInfo = messageService.getMessage(AdminMessageKeys.TRIGGERED_EMAIL_STATUS_NOT_FOUND_INFO);
        return buildResponse(message, moreInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SagasUnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleSagasUnauthorizedException(SagasUnauthorizedException e) {
        String message = messageService.getMessage(AdminMessageKeys.SAGAS_UNAUTHORIZED_EXCEPTION_MSG);
        String moreInfo = messageService.getMessage(AdminMessageKeys.SAGAS_UNAUTHORIZED_EXCEPTION_INFO);
        return buildResponse(message, moreInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        LOG.error("RuntimeException: {}", ex.getMessage(), ex); // error from ACS
        String error = ex.getMessage();
        String message = messageService.getMessage(error);
        String moreInfo = messageService.getMessage(CommonMessageKeys.SERVER_ERROR_URI);

        return buildResponse(message, moreInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HealthCheckException.class)
    public ResponseEntity<ErrorResponse> handleHealthCheckException(RuntimeException ex) {
        LOG.error("HealthCheckException: {}", ex.getMessage(), ex);
        String error = ex.getMessage();
        String message = messageService.getMessage(error);
        String moreInfo = messageService.getMessage(CommonMessageKeys.SERVER_ERROR_URI);

        return buildResponse(message, moreInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        LOG.error("JSON processing exception: {}", ex.getMessage(), ex);
        String message = messageService.getMessage(MessageKeys.JSON_SYNTAX_ERROR_MSG);
        String moreInfo = messageService.getMessage(ex.getMessage());
        return buildResponse(message, moreInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateFormConfigException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateFormConfigException(DuplicateFormConfigException ex) {
        String message = messageService.getMessage(AdminMessageKeys.DUPLICATE_FORM_CONFIG_MSG, ex.getCompany(), ex.getCode());
        String moreInfo = messageService.getMessage(AdminMessageKeys.DUPLICATE_FORM_CONFIG_INFO);
        return buildResponse(message, moreInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FormConfigNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFormConfigNotFoundException(FormConfigNotFoundException ex) {
        String message = messageService.getMessage(AdminMessageKeys.FORM_CONFIG_NOT_FOUND_MSG, ex.getUuid());
        String moreInfo = messageService.getMessage(AdminMessageKeys.FORM_CONFIG_NOT_FOUND_INFO);
        return buildResponse(message, moreInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SerializationUnknownFieldsException.class)
    public ResponseEntity<ErrorResponse> handleSerializationUnknownFieldsException(SerializationUnknownFieldsException ex) {
        String message = messageService.getMessage(MessageKeys.UNKNOWN_PROFILE_ATTRIBUTES_MSG, ex.getUnknownAttributes());
        String moreInfo = messageService.getMessage(MessageKeys.UNKNOWN_PROFILE_ATTRIBUTES_INFO);
        return buildResponse(message, moreInfo, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        LOG.error("JSON Deserialization error!", ex);
        String message = messageService.getMessage(MessageKeys.JSON_DESERIALIZATION_ERROR_MSG);
        Throwable cause = ex.getCause();
        String moreInfo;
        if (cause instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException urpe = (UnrecognizedPropertyException)cause;
            moreInfo = String.format("Unknown property name: %s", urpe.getPropertyName());
        } else {
            moreInfo = cause.getMessage();
        }
        ResponseEntity<ErrorResponse> errorResponse = buildResponse(message, moreInfo, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse.getBody(), errorResponse.getHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<ErrorResponse> handleMappingException(MappingException ex) {
        LOG.error("An exception occurred in ModelMapper: {}", ex.getMessage(), ex);
        String message = messageService.getMessage(CommonMessageKeys.SERVER_ERROR_MSG);
        String moreInfo = messageService.getMessage(CommonMessageKeys.SERVER_ERROR_URI);
        return buildResponse(message, moreInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
