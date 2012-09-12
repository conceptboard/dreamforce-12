package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface UserSummary {

	String getCompanyName();

	String getFirstName();

	String getId();

	boolean getIsActive();

	boolean getIsChatterGuest();

	String getLastName();

	Reference getMySubscription();

	String getName();

	Photo getPhoto();

	String getTitle();

	String getType();

	String getUrl();
}
