package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface FollowingCounts {
	public Integer getPeople();

	public Integer getRecords();

	public Integer getTotal();
}
