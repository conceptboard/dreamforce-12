package com.conceptboard.social.provider.forcedotcom.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

import com.conceptboard.social.provider.forcedotcom.api.ForcedotcomApi;
import com.conceptboard.social.provider.forcedotcom.api.response.ForcedotcomUserProfileInformation;

public class ForcedotcomAdapter implements ApiAdapter<ForcedotcomApi> {

	@Override
	public boolean test(final ForcedotcomApi api) {
		try {
			api.getUserProfile();
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	@Override
	public void setConnectionValues(final ForcedotcomApi api, final ConnectionValues values) {
		final ForcedotcomUserProfileInformation profile = api.getUserProfile();
		values.setDisplayName(profile.getName());
		values.setProfileUrl(profile.getUrl());
		values.setImageUrl(profile.getPhoto().getLargePhotoUrl());
	}

	@Override
	public UserProfile fetchUserProfile(final ForcedotcomApi api) {
		final ForcedotcomUserProfileInformation userProfile = api.getUserProfile();
		return new UserProfileBuilder().setEmail(userProfile.getEmail()).setFirstName(userProfile.getFirstName()).setLastName(userProfile.getLastName())
		        .setName(userProfile.getName()).setUsername(userProfile.getUrl()).build();
	}

	@Override
	public void updateStatus(final ForcedotcomApi api, final String message) {
		throw new UnsupportedOperationException("this method is not implemented yet");
	}
}
