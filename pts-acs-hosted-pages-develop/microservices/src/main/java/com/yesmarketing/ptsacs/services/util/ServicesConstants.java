package com.yesmarketing.ptsacs.services.util;

import com.yesmarketing.ptsacs.services.authentication.ServicesGrantedAuthority;

import java.util.Arrays;
import java.util.List;

public class ServicesConstants {

	public static final String ACS_DATETIME_PATTERN = "yyyy-MM-dd' 'HH:mm:ss.SSS'Z'";

	public static final String SERVICES_TOKEN_HDR_NAME = "x-services-token";

	public static final String CSRF_TOKEN_HDR_NAME = "X-CSRF-TOKEN";

	public static final String FORWARDED_FROM_HDR = "X-Forwarded-Host";

	public static final String REQUEST_ID_HEADER = "x-services-request-id";

	public static final String MDC_REQUEST_ID = "requestId";

	public static final String LOCALHOST_IPV4 = "127.0.0.1";
	public static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
	public static final String LOCALHOST_NAME = "localhost";

	public static final String SERVICES_AUTH_MGR = "servicesAuthenticationManager";

	public static final String CUSTOMER_ID = "cusCustomerId";
	public static final String CUSTOMER_ID_HASH = "cusCustomerIdHash";
	public static final String CUSTOMER_UUID = "cusCustomerUUID";

	public static final String GA_CHECK_TRIGGERED_EMAIL_STATUS_NM = "CHECK_TRIGGERED_EMAIL_STATUS";
	public static final String GA_CHECK_TRIGGERED_EMAIL_STATUS_DS = "Check Triggered Email Status";
	public static final String GA_COMPOSITE_SVC_NM = "COMPOSITE_SERVICE";
	public static final String GA_COMPOSITE_SVC_DS = "Composite Service";
	public static final String GA_CREATE_PROFILE_NM = "CREATE_PROFILE";
	public static final String GA_CREATE_PROFILE_DS = "Create Profile";
	public static final String GA_UPDATE_PROFILE_NM = "UPDATE_PROFILE";
	public static final String GA_UPDATE_PROFILE_DS = "Update Profile";
	public static final String GA_GET_PROFILE_SECURE_LINK_NM = "GET_PROFILE_BY_SECURE_LINK";
	public static final String GA_GET_PROFILE_SECURE_LINK_DS = "Get Profile by Secure Link";
	public static final String GA_GET_PROFILE_CUSTOMERID_NM = "GET_PROFILE_BY_CUSTOMERID";
	public static final String GA_GET_PROFILE_CUSTOMERID_DS = "Get Profile by CustomerId";
	public static final String GA_GET_PROFILE_FILTER_NM = "GET_PROFILE_BY_FILTER";
	public static final String GA_GET_PROFILE_FILTER_DS = "Get Profile by Filter";
	public static final String GA_ADD_SERVICE_NM = "ADD_SERVICE";
	public static final String GA_ADD_SERVICE_DS = "Add Service";
	public static final String GA_REMOVE_SERVICE_NM = "REMOVE_SERVICE";
	public static final String GA_REMOVE_SERVICE_DS = "Remove Service";
	public static final String GA_TRIGGER_EMAIL_NM = "TRIGGER_EMAIL";
	public static final String GA_TRIGGER_EMAIL_DS = "Trigger Email";

    public static final String GA_GET_RESOURCE_METADATA_NM = "GET_RESOURCE_METADATA";
    public static final String GA_GET_RESOURCE_METADATA_DS = "Get Resource Metadata";
    public static final String GA_GET_RESOURCE_NM = "GET_RESOURCE";
    public static final String GA_GET_RESOURCE_DS = "Get Resource";
    public static final String GA_FIND_RESOURCE_NM = "FIND_RESOURCE";
    public static final String GA_FIND_RESOURCE_DS = "Find Resource";
    public static final String GA_CREATE_RESOURCE_NM = "CREATE_RESOURCE";
    public static final String GA_CREATE_RESOURCE_DS = "Create Resource";
    public static final String GA_UPDATE_RESOURCE_NM = "UPDATE_RESOURCE";
    public static final String GA_UPDATE_RESOURCE_DS = "Update Resource";
    public static final String GA_REMOVE_RESOURCE_NM = "REMOVE_RESOURCE";
    public static final String GA_REMOVE_RESOURCE_DS = "Remove Resource";

	public static final List<ServicesGrantedAuthority> SERVICES_AUTHORITIES = Arrays.asList(
		new ServicesGrantedAuthority(GA_CHECK_TRIGGERED_EMAIL_STATUS_NM, GA_CHECK_TRIGGERED_EMAIL_STATUS_DS),
		new ServicesGrantedAuthority(GA_COMPOSITE_SVC_NM, GA_COMPOSITE_SVC_DS),
		new ServicesGrantedAuthority(GA_CREATE_PROFILE_NM, GA_CREATE_PROFILE_DS),
		new ServicesGrantedAuthority(GA_UPDATE_PROFILE_NM, GA_UPDATE_PROFILE_DS),
		new ServicesGrantedAuthority(GA_GET_PROFILE_SECURE_LINK_NM, GA_GET_PROFILE_SECURE_LINK_DS),
		new ServicesGrantedAuthority(GA_GET_PROFILE_CUSTOMERID_NM, GA_GET_PROFILE_CUSTOMERID_DS),
		new ServicesGrantedAuthority(GA_GET_PROFILE_FILTER_NM, GA_GET_PROFILE_FILTER_DS),
		new ServicesGrantedAuthority(GA_ADD_SERVICE_NM, GA_ADD_SERVICE_DS),
		new ServicesGrantedAuthority(GA_REMOVE_SERVICE_NM, GA_REMOVE_SERVICE_DS),

		new ServicesGrantedAuthority(GA_GET_RESOURCE_METADATA_NM, GA_GET_RESOURCE_METADATA_DS),
		new ServicesGrantedAuthority(GA_GET_RESOURCE_NM, GA_GET_RESOURCE_DS),
		new ServicesGrantedAuthority(GA_FIND_RESOURCE_NM, GA_FIND_RESOURCE_DS),
		new ServicesGrantedAuthority(GA_CREATE_RESOURCE_NM, GA_CREATE_RESOURCE_DS),
		new ServicesGrantedAuthority(GA_UPDATE_RESOURCE_NM, GA_UPDATE_RESOURCE_DS),
		new ServicesGrantedAuthority(GA_REMOVE_RESOURCE_NM, GA_REMOVE_RESOURCE_DS),

		new ServicesGrantedAuthority(GA_TRIGGER_EMAIL_NM, GA_TRIGGER_EMAIL_DS)
	);

	public static final String ROLE_CREATE_PROFILE = "ROLE_CREATE_PROFILE";
	public static final String ROLE_UPDATE_PROFILE = "ROLE_UPDATE_PROFILE";
	public static final String ROLE_GET_PROFILE_SECURE_LINK = "ROLE_GET_PROFILE_BY_SECURE_LINK";
	public static final String ROLE_GET_PROFILE_BY_CUSTOMERID = "ROLE_GET_PROFILE_BY_CUSTOMERID";
	public static final String ROLE_GET_PROFILE_BY_FILTER = "ROLE_GET_PROFILE_BY_FILTER";
	public static final String ROLE_ADD_SERVICE = "ROLE_ADD_SERVICE";
	public static final String ROLE_REMOVE_SERVICE = "ROLE_REMOVE_SERVICE";
	public static final String ROLE_TRIGGER_EMAIL = "ROLE_TRIGGER_EMAIL";


	private ServicesConstants() {

	}
}
