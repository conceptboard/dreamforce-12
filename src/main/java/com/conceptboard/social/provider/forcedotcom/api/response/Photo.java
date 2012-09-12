package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface Photo {
	public String getLargePhotoUrl();

	public String getPhotoVersionId();

	public String getSmallPhotoUrl();
}
