package com.yesmarketing.ptsacs.services.dto;

import com.yesmarketing.acsapi.model.OrgUnit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@Data
public class CreateProfileDto implements Serializable {

	private static final long serialVersionUID = 9167263967012436310L;

	private String birthDate;

	private boolean blackList;

	private String blackListAllLastModified;

	private boolean blackListEmail;

	private String blackListEmailLastModified;

	private boolean blackListFax;

	private String blackListFaxLastModified;

	private String blackListLastModified;

	private boolean blackListMobile;

	private String blackListMobileLastModified;

	private boolean blackListPhone;

	private String blackListPhoneLastModified;

	private boolean blackListPostalMail;

	private String blackListPostalMailLastModified;

	private boolean blackListPushnotification;

	private String blackListPushnotificationLastModified;

	private boolean ccpaOptOut;

	private String ccpaOptOutLastModified;

	private String email;

	public String getEmail(){
		return email==null?null:email.toLowerCase();
	}

	private String emailFormat;

	private String externalId;

	private String fax;

	private String firstName;

	private String gender;

	private String lastName;

	private LocationDto location;

	private String middleName;

	private String mobilePhone;

	private OrgUnit orgUnit;

	private String phone;

	private PostalAddressDto postalAddress;

	private String preferredLanguage;

	private String salutation;

	private String thumbnail;

	private String timeZone;

	private Map<String, Object> customFields;
}
