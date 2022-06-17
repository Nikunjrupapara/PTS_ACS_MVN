package integrationtests.testdata.form;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
@AllArgsConstructor
public class TokenStateException extends RuntimeException {
	private static final long serialVersionUID = 5359654573756557930L;

	String tokenState;

	@Override
	public String getMessage() {
		return String.format("No token found for tokenState '%s'", tokenState);
	}
}
