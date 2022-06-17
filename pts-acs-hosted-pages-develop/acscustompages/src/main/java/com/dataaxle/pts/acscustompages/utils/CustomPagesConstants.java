package com.dataaxle.pts.acscustompages.utils;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;

public class CustomPagesConstants {

	public static final String SERVICES_TOKEN_HDR = "x-services-token";

	public static final String FORWARDED_HDR = "x-forwarded-from";
	public static final String FORWARDED_FOR_HDR = "x-forwarded-for";

	public static final String RECAPTCHA_RESPONSE_NM = "g-recaptcha-response";

	public static final String REQUEST_ID_HEADER = "x-services-request-id";

	public static final String MDC_REQUEST_ID = "requestId";
	public static final String LOOKUP_PARAMETER = "l";
	public static final String SECURITY_PARAMETER = "s";
	public static final String VIEW_PARAMETER = "view";

	public static final String ACS_ID = "acsId";
	public static final String CUSTOMER_ID = "customerId";
	public static final String CUS_CUSTOMER_ID = "cusCustomerId";
	public static final String CUSTOMER_ID_HASH = "customerIdHash";
	public static final String CUS_CUSTOMER_ID_HASH = "cusCustomerIdHash";
	public static final String CUS_CUSTOMER_UUID = "cusCustomerUUID";
	public static final String EMAIL = "email";

	public static final String ACS_DATE_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	public static final String ACS_DATETIME_REGEX = String.format("%s [0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z", ACS_DATE_REGEX);

	public static final DateTimeFormatter ADOBE_FORMAT = new DateTimeFormatterBuilder()
										.appendPattern("uuuu-MM-dd' 'HH:mm:ss")
										.appendPattern(".")
										.appendValue(ChronoField.MILLI_OF_SECOND, 3)
										.appendLiteral("Z")
										.toFormatter()
										.withResolverStyle(ResolverStyle.STRICT);

	public static final String PKEY = "PKey";

	private CustomPagesConstants() {

	}
}
