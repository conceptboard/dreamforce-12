package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class SegmentText extends Segment {

	static final String TYPE = "Text";
	private final String text;

	public SegmentText(final String text) {
		this(TYPE, text);
	}

	@JsonCreator
	public SegmentText(@JsonProperty("type") final String type, @JsonProperty("text") final String text) {
		super(type);
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
