package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface Address {
	public String getCity();

	public String getCountry();

	public String getState();

	public String getStreet();

	public String getZip();

	public String getFormattedAddress();
}
