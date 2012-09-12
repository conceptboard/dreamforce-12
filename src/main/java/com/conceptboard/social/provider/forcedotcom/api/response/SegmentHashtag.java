package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class SegmentHashtag extends Segment {

	static final String TYPE = "Hashtag";
	private final String tag;

	public SegmentHashtag(final String tag) {
		this(TYPE, tag);
	}

	@JsonCreator
	public SegmentHashtag(@JsonProperty("type") final String type, @JsonProperty("tag") final String tag) {
		super(type);
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}
}
