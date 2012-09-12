package com.conceptboard.social.provider.forcedotcom.api.response;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

public interface ChatterGroupPage {
	String getCurrentPageUrl();

	@JsonDeserialize(contentAs = ChatterGroup.class)
	List<ChatterGroup> getGroups();

	String getNextPageUrl();

	String getPreviousPageUrl();

	Integer getTotal();
}
