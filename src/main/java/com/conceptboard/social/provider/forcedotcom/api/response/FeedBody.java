package com.conceptboard.social.provider.forcedotcom.api.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface FeedBody {
	String getText();

	@JsonDeserialize(contentAs = Segment.class)
	List<Segment> getMessageSegments();
}
