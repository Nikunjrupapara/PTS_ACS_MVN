package com.dataaxle.pts.acscustompages.model.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryParameter {
	String name;

	List<String> values;
}
