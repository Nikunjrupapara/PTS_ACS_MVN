package com.dataaxle.pts.acscustompages.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile implements Serializable {

	private static final long serialVersionUID = -7388731672464951550L;

	private Map<String, Object> fields = new HashMap<>();

	public Profile(Profile original) {
		this.fields = new HashMap<>(original.fields);
	}

	public void addField(String name, Object value) {
		fields.put(name, value);
	}

	public Optional<Object> getField(String name) {
		if (hasField(name)) {
			return Optional.of(fields.get(name));
		}
		return Optional.empty();
	}

	public boolean hasField(String name) {
		return fields.containsKey(name);
	}
	/*private String dateOfBirth;

	private String email;

	private String firstName;

	private Gender gender;

	private String lastName;

	private Map<String, Object> customFields = new HashMap<>();

	public void addCustomField(Map.Entry<String, Object> entry) {
		customFields.put(entry.getKey(), entry.getValue());
	}*/
	/*Map<String, Object> fields = new HashMap<>();

	public void addField(String key, Object value) {
		fields.put(key, value);
	}

	public Optional<Object> getValue(String key) {
		if (fields.containsKey(key) && fields.get(key) != null) {
			return Optional.of(fields.get(key));
		}
		return Optional.empty();
	}*/

	/*public String getEmail() {
		return (String)fields.get("email");
	}

	public void setEmail(String email) {
		fields.put("email", email);
	}*/
}
