package com.dataaxle.pts.acscustompages.model;

import com.dataaxle.pts.acscustompages.exception.AppPageNotFoundException;
import com.dataaxle.pts.acscustompages.exception.AppPageNotFoundByTypeException;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class AppDetails implements Serializable {

	private static final long serialVersionUID = -4761767843398229779L;

	@Id
	private final AppDetailsId appId;

	private final LocalDateTime effectiveFrom;

	private final LocalDateTime effectiveTo;

	private final boolean enabled;

	private String jwt;

	private final List<AppPage> pages = new ArrayList<>();

	private final Map<String, String> customHeaders = new HashMap<>();

	private AppDetails(Builder builder) {
		this.appId = new AppDetailsId(builder.company, builder.contextPath);
		this.effectiveFrom = builder.effectiveFrom;
		this.effectiveTo = builder.effectiveTo;
		this.enabled = builder.enabled;
		this.jwt = builder.jwt;
		this.pages.addAll(builder.pages);
		this.customHeaders.putAll(builder.customHeaders);
	}

	public String getCompany() { return appId.getCompany(); }

	public String getContextPath() {
		return appId.getContextPath();
	}

	public AppPage getPage(String name) {
		return pages
			.stream()
			.filter(page -> page.getViewName().equals(name))
			.findFirst()
			.orElseThrow(() -> new AppPageNotFoundException(name));
	}

	@Deprecated
	public String getDomain() {
		return "";
	}

	public AppPage getEntryPoint() {
		return getPage(PageType.ENTRY_POINT);
	}

	public AppPage getErrorPage() {
		return getPage(PageType.ERROR);
	}

	private AppPage getPage(PageType pageType) {
		return pages
				   .stream()
				   .filter(page -> page.getPageType().equals(pageType))
				   .findFirst()
				   .orElseThrow(() -> new AppPageNotFoundByTypeException(pageType));
	}

	public AuthenticationStatus isAuthenticated() {
		if (!enabled) {
			LOG.error("AppDetails with id {} is not enabled", appId);
			return AuthenticationStatus.DISABLED;
		}
		LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(effectiveFrom)) {
			LOG.error("AppDetails with id {} is not yet effective", appId);
			return AuthenticationStatus.NOT_EFFECTIVE;
		}
		if (now.isAfter(effectiveTo)) {
			LOG.error("AppDetails with if {} is expired", appId);
			return AuthenticationStatus.EXPIRED;
		}
		return AuthenticationStatus.AUTHENTICATED;
	}

	@Value
	public static class AppDetailsId implements Serializable {

		private static final long serialVersionUID = -2089850648489894663L;

		String company;

		String contextPath;
	}

	public enum AuthenticationStatus {
		AUTHENTICATED("authenticated"),
		DISABLED("disabled"),
		NOT_EFFECTIVE("not yet online"),
		EXPIRED("expired");

		private final String name;

		AuthenticationStatus(String name) {
			this.name = name;
		}
	}

	public static Builder builder(String company, String contextPath) {
		return new Builder(company, contextPath);
	}

	public static class Builder {

		private final String company;

		private final String contextPath;

		private LocalDateTime effectiveFrom = LocalDateTime.MIN;

		private LocalDateTime effectiveTo = LocalDateTime.MAX;

		private boolean enabled = false;

		private String jwt = "";

		private final List<AppPage> pages = new ArrayList<>();

		private final Map<String, String> customHeaders = new HashMap<>();

		private Builder(String company, String contextPath) {
			this.company = company;
			this.contextPath = contextPath;
		}

		public Builder effectiveFrom(LocalDateTime effectiveFrom) {
			this.effectiveFrom = effectiveFrom;
			return this;
		}

		public Builder effectiveTo(LocalDateTime effectiveTo) {
			this.effectiveTo = effectiveTo;
			return this;
		}

		public Builder enable() {
			this.enabled = true;
			return this;
		}

		public Builder enabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public Builder jwt(String jwt) {
			this.jwt = jwt;
			return this;
		}

		public Builder pages(List<AppPage> pages) {
			this.pages.addAll(pages);
			return this;
		}

		public Builder addPage(AppPage page) {
			this.pages.add(page);
			return this;
		}

		public Builder addHeader(String name, String value) {
			this.customHeaders.put(name, value);
			return this;
		}

		public Builder addHeader(Map<String, String> customHeaders) {
			this.customHeaders.putAll(customHeaders);
			return this;
		}

		public AppDetails build() {
			return new AppDetails(this);
		}
	}
}
