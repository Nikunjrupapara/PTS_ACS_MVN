package com.dataaxle.pts.acscustompages.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Domain implements Serializable {

	private static final long serialVersionUID = -828239332836889165L;

	@Id
	String name;

	String company;

	String brand;
}
