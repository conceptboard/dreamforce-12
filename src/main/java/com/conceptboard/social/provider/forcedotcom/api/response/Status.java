package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface Status {
	@JsonProperty
	String getCreated_date();

	@JsonProperty
	String getBody();
}
