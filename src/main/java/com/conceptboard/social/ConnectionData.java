package com.conceptboard.social;

@SuppressWarnings("serial")
/**
 * extended version of ConnectionData to store additionalData on a connection
 * 
 * <strong>Be advised</strong>, that the connectionRepository you are using supports storing this additional data
 * 
 * @author christian
 */
public class ConnectionData extends org.springframework.social.connect.ConnectionData {

	private final String additionalData;

	public ConnectionData(final String providerId, final String providerUserId, final String displayName, final String profileUrl, final String imageUrl,
	        final String accessToken, final String secret, final String refreshToken, final Long expireTime, final String additionalData) {
		super(providerId, providerUserId, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime);
		this.additionalData = additionalData;
	}

	public ConnectionData(final org.springframework.social.connect.ConnectionData data, final String additionalData) {
		super(data.getProviderId(), data.getProviderUserId(), data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), data.getAccessToken(), data
		        .getSecret(), data.getRefreshToken(), data.getExpireTime());
		this.additionalData = additionalData;
	}

	public ConnectionData(final org.springframework.social.connect.ConnectionData data) {
		super(data.getProviderId(), data.getProviderUserId(), data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), data.getAccessToken(), data
		        .getSecret(), data.getRefreshToken(), data.getExpireTime());
		if (data instanceof ConnectionData) {
			this.additionalData = ((ConnectionData) data).getAdditionalData();
		} else {
			this.additionalData = null;
		}
	}

	public String getAdditionalData() {
		return additionalData;
	}
}
