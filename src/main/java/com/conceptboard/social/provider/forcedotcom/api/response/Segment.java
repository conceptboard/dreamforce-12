package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

/**
 * A Segment is just an abstract type for the entrys of a messageBody in the Chatter API.
 * 
 * The subtypes are determined by the field type which is common for all segments.
 * 
 */
@JsonTypeInfo(use = Id.NAME, property = "type", include = As.PROPERTY)
@JsonSubTypes({ @Type(value = SegmentHashtag.class, name = SegmentHashtag.TYPE), @Type(value = SegmentLink.class, name = SegmentLink.TYPE),
        @Type(value = SegmentMention.class, name = SegmentMention.TYPE), @Type(value = SegmentText.class, name = SegmentText.TYPE) })
public abstract class Segment {
	private final String type;

	public Segment(final String type) {
		this.type = type;
	}

	@JsonIgnore
	public String getType() {
		return type;
	}
}
