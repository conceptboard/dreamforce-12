package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface FeedItem {

	String getId();

	String getUrl();

	FeedBody getBody();

}
