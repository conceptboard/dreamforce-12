package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface Urls {

	String getEnterprise();

	String getMetadata();

	String getPartner();

	String getRest();

	String getSobjets();

	String getSearch();

	String geQuery();

	String getRecent();

	String getProfile();
}
