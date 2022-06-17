package com.dataaxle.pts.acscustompages.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UtilityFunctions {

	public static String formatParameter(Map.Entry<String, List<String>> entry) {
		String name = entry.getKey();
		return entry.getValue().stream()
			.map(value -> String.format("%s=%s", name, value))
			.collect(Collectors.joining("&"));
	}

	private UtilityFunctions() {}
}
