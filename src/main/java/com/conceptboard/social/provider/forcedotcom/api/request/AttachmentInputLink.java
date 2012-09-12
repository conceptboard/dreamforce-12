package com.conceptboard.social.provider.forcedotcom.api.request;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class AttachmentInputLink extends AttachmentInput {

	private String urlName;
	private final String url;

	@JsonCreator
	public AttachmentInputLink(@JsonProperty("url") final String url) {
		this.url = url;
	}

	@JsonCreator
	public AttachmentInputLink(@JsonProperty("urlName") final String urlName, @JsonProperty("url") final String url) {
		this.urlName = urlName;
		this.url = url;
	}

	public String getUrlName() {
		return urlName;
	}

	public String getUrl() {
		return url;
	}
}
