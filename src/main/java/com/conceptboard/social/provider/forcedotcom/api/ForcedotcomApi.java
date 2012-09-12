package com.conceptboard.social.provider.forcedotcom.api;

import com.conceptboard.social.provider.forcedotcom.api.response.ForcedotcomUserIdentity;
import com.conceptboard.social.provider.forcedotcom.api.response.ForcedotcomUserProfileInformation;

public interface ForcedotcomApi {

	ForcedotcomUserIdentity getUserIdentity(String forcedotcomId);

	ForcedotcomUserProfileInformation getUserProfile();

	ChatterOperations getChatterOperations();

}
