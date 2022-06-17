package com.dataaxle.pts.acscustompages.model.form;

import com.dataaxle.pts.acscustompages.model.loadaction.LoadActionConfig;
import com.dataaxle.pts.acscustompages.model.loadaction.ValueProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryParameters implements Serializable, ValueProvider {

	private static final long serialVersionUID = -19874959380359562L;

	Map<String, List<String>> parameters;

	public boolean isEmpty() { return parameters.isEmpty(); }

	public Set<String> getParameterNames() { return parameters.keySet(); }

	public boolean hasParameter(String name) { return parameters.containsKey(name); }

	public List<String> getParameter(String name) {
		if (hasParameter(name)) {
			return parameters.get(name);
		}
		return Collections.emptyList();
	}

	public String getParameterFirstValue(String name) {
		if (hasParameter(name)) {
			return parameters.get(name).get(0);
		}
		return "";
	}

	public String getParameterAsString(String name) {
		if (hasParameter(name)) {
			return String.join(",", parameters.get(name));
		}
		return "";
	}

	public String buildQueryString(Set<String> parameterNames) {
		return parameters.entrySet().stream()
			.filter(entry -> parameterNames.contains(entry.getKey()))
			.sorted(Map.Entry.comparingByKey())
			.map(entry -> {
				if (entry.getValue() != null) {
					return entry.getValue().stream()
						.sorted(Comparator.naturalOrder())
						.map(val ->String.format("%s=%s", entry.getKey(), val))
						.collect(Collectors.joining("&"));
				} else {
					return entry.getKey();
				}
			})
			.collect(Collectors.joining("&"));
	}

	public String buildQueryString() {
		return buildQueryString(parameters.keySet());
	}

	public static QueryParameters of(String url) {
		if (!url.contains("?")) {
			return new QueryParameters(Collections.emptyMap());
		}
		String queryParamsStr = url.substring(url.indexOf("?") + 1);
		Map<String, List<String>> queryParams = new HashMap<>();
		List<String> queryParamList = new ArrayList<>(Arrays.asList(queryParamsStr.split("&")));
		queryParamList.forEach(qp -> {
			if (qp.contains("=")) {
				String[] values = qp.split("=");
				String paramName = values[0];
				List<String> valueList = queryParams.getOrDefault(paramName, new ArrayList<>());
				valueList.add(values.length > 1 ? values[1] : "");
				queryParams.putIfAbsent(paramName, valueList);
			} else {
				queryParams.put(qp, null);
			}
		});
		return new QueryParameters(queryParams);
	}

	@Override
	public LoadActionConfig.ParameterSource getParameterSource() { return LoadActionConfig.ParameterSource.QUERY_PARAMETER; }

	@Override
	public String provideValue(String name) { return getParameterAsString(name); }
}
