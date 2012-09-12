package com.conceptboard.social.provider.forcedotcom.connect;

import org.springframework.social.oauth2.AccessGrant;

@SuppressWarnings("serial")
public class ForcedotcomAccessGrant extends AccessGrant {

	private final String id;
	private final String instanceUrl;

	public ForcedotcomAccessGrant(final String accessToken, final String scope, final String refreshToken, final Integer expiresIn, final String id,
	        final String instanceUrl) {
		super(accessToken, scope, refreshToken, expiresIn);
		this.id = id;
		this.instanceUrl = instanceUrl;
	}

	public String getId() {
		return id;
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}
}
