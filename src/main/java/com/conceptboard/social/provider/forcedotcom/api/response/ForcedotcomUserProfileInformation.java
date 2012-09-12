package com.conceptboard.social.provider.forcedotcom.api.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface ForcedotcomUserProfileInformation {

	public Address getAddress();

	public String getAboutMe();

	public ChatterActivity getChatterActivity();

	public ChatterInfluence getChatterInfluence();

	public String getCompanyName();

	public String getEmail();

	public String getFirstName();

	public Integer getFollowersCount();

	public FollowingCounts getFollowingCounts();

	public Integer getGroupCount();

	public String getId();

	public boolean getIsActive();

	public boolean getIsChatterGuest();

	public String getLastName();

	public String getManagerId();

	public String getManagerName();

	// public Object getMySubscription();

	public String getName();

	@JsonDeserialize(contentAs = PhoneNumber.class)
	public List<PhoneNumber> getPhoneNumbers();

	public Photo getPhoto();

	public String getTitle();

	public String getType();

	public String getUrl();

	public String getUsername();

}
