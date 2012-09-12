package com.conceptboard.social.provider.forcedotcom.api.response;

import java.util.Date;

public interface ChatterGroup {
	boolean getCanHaveChatterGuests();

	String getDescription();

	Integer getFileCount();

	String getId();

	Date getLastFeedItemPostDate();

	Integer getMemberCount();

	String getMyRole();

	Reference getMySubscription();

	String getName();

	UserSummary getOwner();

	Photo getPhoto();

	String getType();

	String getUrl();

	String getVisibility();

}
