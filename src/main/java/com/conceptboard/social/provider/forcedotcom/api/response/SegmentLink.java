package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class SegmentLink extends Segment {

	static final String TYPE = "Link";
	private final String url;

	public SegmentLink(final String url) {
		this(TYPE, url);
	}

	@JsonCreator
	public SegmentLink(@JsonProperty("type") final String type, @JsonProperty("url") final String url) {
		super(type);
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
