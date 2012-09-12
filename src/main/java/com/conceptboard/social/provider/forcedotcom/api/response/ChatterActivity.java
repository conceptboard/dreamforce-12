package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface ChatterActivity {
	public Integer getCommentCount();

	public Integer getCommentReceivedCount();

	public Integer getLikeReceivedCount();

	public Integer getPostCount();
}
