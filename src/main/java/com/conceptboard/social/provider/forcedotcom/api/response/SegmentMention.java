package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class SegmentMention extends Segment {

	static final String TYPE = "Mention";
	private final String id;

	public SegmentMention(final String id) {
		this(TYPE, id);
	}

	@JsonCreator
	public SegmentMention(@JsonProperty("type") final String type, @JsonProperty("id") final String id) {
		super(type);
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
