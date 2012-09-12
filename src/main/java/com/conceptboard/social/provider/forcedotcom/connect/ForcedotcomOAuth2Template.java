package com.conceptboard.social.provider.forcedotcom.connect;

import java.util.Map;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.web.client.RestTemplate;

import com.conceptboard.social.provider.forcedotcom.api.impl.ForcedotcomApiTemplate;
import com.conceptboard.social.provider.forcedotcom.api.impl.ForcedotcomErrorHandler;
import com.conceptboard.social.provider.forcedotcom.api.impl.RequestResponseBufferingRequestFactory;

public class ForcedotcomOAuth2Template extends OAuth2Template {

	private static final String FORCE_DOT_COM_AUTHORIZE_URL_TEMPLATE = "https://login.salesforce.com/services/oauth2/authorize";
	private static final String FORCE_DOT_COM_ACCESS_TOKEN_URL_TEMPLATE = "https://login.salesforce.com/services/oauth2/token";

	public ForcedotcomOAuth2Template(final String clientId, final String clientSecret) {
		super(clientId, clientSecret, FORCE_DOT_COM_AUTHORIZE_URL_TEMPLATE, FORCE_DOT_COM_ACCESS_TOKEN_URL_TEMPLATE);
	}

	@Override
	protected AccessGrant createAccessGrant(final String accessToken, final String scope, final String refreshToken, final Integer expiresIn,
	        final Map<String, Object> response) {
		final String id = (String) response.get("id");
		final String instanceUrl = (String) response.get("instance_url");
		return new ForcedotcomAccessGrant(accessToken, scope, refreshToken, expiresIn, id, instanceUrl);
	}

	@Override
	protected RestTemplate createRestTemplate() {
		final RestTemplate restTemplate = super.createRestTemplate();
		restTemplate.setRequestFactory(RequestResponseBufferingRequestFactory.bufferRequests(restTemplate.getRequestFactory()));
		restTemplate.setErrorHandler(new ForcedotcomErrorHandler(ForcedotcomApiTemplate.getObjectMapper()));
		return restTemplate;
	}
}
