package com.dataaxle.pts.acscustompages.model.form;

import com.dataaxle.pts.acscustompages.model.Gender;
import lombok.Data;

import java.util.Map;

@Data
public class FormBean {

	Map<String, Object> customFields;

	String confirmEmail;

	String dateOfBirth;

	String email;

	String firstName;

	Gender gender;

	String lastName;

	/*public String getEmail() {
		String email = (String)fields.get("email");
		return email == null ? "" : email;
	}

	public void setEmail(String email) {
		fields.put("email", email);
	}*/
}
