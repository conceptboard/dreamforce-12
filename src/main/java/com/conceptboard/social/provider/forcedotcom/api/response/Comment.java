package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface Comment {

	Attachment getAttachment();

	FeedBody getBody();

	String getId();

	String getUrl();
}
