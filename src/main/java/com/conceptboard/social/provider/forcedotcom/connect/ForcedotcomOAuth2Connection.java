package com.conceptboard.social.provider.forcedotcom.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;

import com.conceptboard.social.provider.forcedotcom.api.ForcedotcomApi;

public class ForcedotcomOAuth2Connection extends OAuth2Connection<ForcedotcomApi> implements Connection<ForcedotcomApi> {

	private final ForcedotcomServiceProvider forceDotcomServiceProvider;

	public ForcedotcomOAuth2Connection(final ConnectionData data, final ForcedotcomServiceProvider serviceProvider, final ApiAdapter<ForcedotcomApi> apiAdapter) {
		super(data, serviceProvider, apiAdapter);
		this.forceDotcomServiceProvider = serviceProvider;
	}

	public ForcedotcomOAuth2Connection(final String providerId, final String providerUserId, final String accessToken, final String refreshToken,
	        final Long expireTime, final ForcedotcomServiceProvider serviceProvider, final ApiAdapter<ForcedotcomApi> apiAdapter) {
		super(providerId, providerUserId, accessToken, refreshToken, expireTime, serviceProvider, apiAdapter);
		this.forceDotcomServiceProvider = serviceProvider;
	}

	@Override
	public ConnectionData createData() {
		final ConnectionData data = super.createData();
		return new com.conceptboard.social.ConnectionData(data, createAdditionalData());
	}

	private String createAdditionalData() {
		final String instanceUrl = forceDotcomServiceProvider.getInstanceUrl();
		return ForcedotcomConnectionFactory.createAdditionalData(instanceUrl);
	}
}
