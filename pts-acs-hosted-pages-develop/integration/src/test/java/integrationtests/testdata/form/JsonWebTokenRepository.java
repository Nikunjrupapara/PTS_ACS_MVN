package integrationtests.testdata.form;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonWebTokenRepository {

	private static final Map<String, String> tokenMap = new HashMap<>();


	public static Optional<String> getToken(String tokenState) {
		if (!tokenMap.containsKey(tokenState)) {
			return Optional.empty();
		}
		return Optional.of(tokenMap.get(tokenState));
	}

	public static void addToken(String tokenState, String token) {
		tokenMap.putIfAbsent(tokenState, token);
	}

	private JsonWebTokenRepository() {

	}
}
