package com.conceptboard.social.provider.forcedotcom.api.request;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;

import com.conceptboard.social.provider.forcedotcom.api.response.Segment;

/*
 * 
 */
public class MessageBodyInput {

	private final List<Segment> messageSegments;

	@JsonCreator
	public MessageBodyInput(
	        @JsonProperty("messageSegments") @JsonDeserialize(contentAs = Segment.class) @JsonSerialize(typing = Typing.DYNAMIC) final List<Segment> messageSegments) {
		this.messageSegments = messageSegments;
	}

	public List<Segment> getMessageSegments() {
		return this.messageSegments;
	}
}
