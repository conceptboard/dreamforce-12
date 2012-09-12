package com.conceptboard.social.provider.forcedotcom.connect;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.social.oauth2.AccessGrant;

import com.conceptboard.social.oauth2useragentflow.OAuthUserAgentFlowOperations;
import com.conceptboard.social.oauth2useragentflow.SigningSupport;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

public class ForcedotcomOAuth2UserAgentFlowTemplate implements OAuthUserAgentFlowOperations {

	public static final String HMAC_SHA256_MAC_NAME = "HmacSHA256";
	private final String clientSecret;

	public ForcedotcomOAuth2UserAgentFlowTemplate(final String clientSecret) {
		this.clientSecret = clientSecret;
	}

	@Override
	public AccessGrant extractAccessGrant(final String oauthcallbackuri) {
		final String fragment = getFragment(oauthcallbackuri);
		final Map<String, String> arguments = Splitter.on('&').withKeyValueSeparator(Splitter.on('=').limit(2)).split(fragment);

		final String accessToken = arguments.get("access_token");
		final String refreshToken = arguments.get("refresh_token");
		final Integer expiresIn = null;
		final String id = arguments.get("id");
		final String instanceUrl = arguments.get("instance_url");
		final String signature = arguments.get("signature");
		checkSignature(id, arguments.get("issued_at"), signature);
		return new ForcedotcomAccessGrant(accessToken, null, refreshToken, expiresIn, id, instanceUrl);
	}

	private void checkSignature(final String id, final String issuedAt, final String signature) {
		final String signatureBaseString = id + issuedAt;

		final byte[] calculatedSignature = SigningSupport.calculateSignature(signatureBaseString, clientSecret, HMAC_SHA256_MAC_NAME);
		final byte[] givenSignature = SigningSupport.base64Decode(signature);

		if (Arrays.equals(calculatedSignature, givenSignature)) {
			return;
		} else {
			throw new IllegalArgumentException("the signature does not match the id and instanceurl given");
		}
	}

	private String getFragment(final String oauthcallbackuri) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(oauthcallbackuri));
		try {
			URI uri;
			uri = new URI(oauthcallbackuri);
			return uri.getFragment();
		} catch (final URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
