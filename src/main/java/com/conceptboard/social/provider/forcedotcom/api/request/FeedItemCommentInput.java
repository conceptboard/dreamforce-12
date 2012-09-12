package com.conceptboard.social.provider.forcedotcom.api.request;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedItemCommentInput {
	private final MessageBodyInput body;

	@JsonCreator
	public FeedItemCommentInput(@JsonProperty("body") final MessageBodyInput body) {
		this.body = body;
	}

	public MessageBodyInput getBody() {
		return body;
	}
}
