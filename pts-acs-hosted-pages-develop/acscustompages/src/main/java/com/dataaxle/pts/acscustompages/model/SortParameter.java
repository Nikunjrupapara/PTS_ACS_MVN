package com.dataaxle.pts.acscustompages.model;

import static com.google.common.base.Preconditions.checkArgument;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Value
@EqualsAndHashCode
@ToString
public class SortParameter implements Serializable {

	private static final long serialVersionUID = 3916423380565004405L;

	/**
	 * The name of a field on the custom resource to use for sorting the data
	 */
	String fieldName;

	/**
	 * The direction (ascending / descending) in which to sort this field
	 */
	SortDirection direction;

	private SortParameter(Builder builder) {
		this.fieldName = builder.fieldName;
		this.direction = builder.sortDirection;
	}

	public String toAcsString() { return String.format("%s%s", fieldName, direction.acsValue); }

	public static Builder builder() { return new Builder(); }

	public static class Builder {
		private Builder() {}

		private String fieldName;

		private SortDirection sortDirection;

		public SortParameter build() {
			SortParameter result = new SortParameter(this);
			validate(result);
			return result;
		}

		public Builder fieldName(String name) {
			this.fieldName = name;
			return self();
		}

		public Builder sortDirection(SortDirection sortDirection) {
			this.sortDirection = sortDirection;
			return self();
		}

		public Builder sortDirection(String name) {
			return this.sortDirection(SortDirection.of(name));
		}

		public Builder ascending() { return this.sortDirection(SortDirection.ASCENDING); }

		public Builder descending() { return this.sortDirection(SortDirection.DESCENDING); }

		private Builder self() { return this; }

		private void validate(SortParameter obj) {
			checkArgument(StringUtils.hasText(obj.fieldName), "Field name must be provided!");
			checkArgument(obj.direction != null, "Direction must be provided!");
		}
	}
}
