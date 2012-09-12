package com.conceptboard.social.provider.forcedotcom.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface ForcedotcomUserIdentity {

	String getId();

	boolean getAsserted_user();

	String getUser_id();

	String getOrganization_id();

	String getUsername();

	String getNick_name();

	String getDisplay_name();

	String getEmail();

	Status getStatus();

	Photos getPhotos();

	Urls getUrls();

	boolean getActive();

	String getUser_type();

	String getLanguage();

	String getLocale();

	Integer getOffset();

	String getLast_modified_date();

}
