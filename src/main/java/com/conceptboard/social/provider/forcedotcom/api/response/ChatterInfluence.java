package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface ChatterInfluence {
	public String getPercentile();

	public Integer getRank();
}
