package com.conceptboard.social.provider.forcedotcom.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

import com.conceptboard.social.oauth2useragentflow.OAuth2UserAgentServiceProvider;
import com.conceptboard.social.oauth2useragentflow.OAuthUserAgentFlowOperations;
import com.conceptboard.social.provider.forcedotcom.api.ForcedotcomApi;
import com.conceptboard.social.provider.forcedotcom.api.impl.ForcedotcomApiTemplate;

public class ForcedotcomServiceProvider extends AbstractOAuth2ServiceProvider<ForcedotcomApi> implements OAuth2UserAgentServiceProvider<ForcedotcomApi> {

	private final String instanceUrl;
	private final ForcedotcomOAuth2UserAgentFlowTemplate oauthUserAgentFlowOperations;

	public ForcedotcomServiceProvider(final String clientId, final String clientSecret, final String instanceUrl) {
		super(new ForcedotcomOAuth2Template(clientId, clientSecret));
		this.oauthUserAgentFlowOperations = new ForcedotcomOAuth2UserAgentFlowTemplate(clientSecret);
		this.instanceUrl = instanceUrl;
	}

	@Override
	public ForcedotcomApi getApi(final String accessToken) {
		return new ForcedotcomApiTemplate(accessToken, getInstanceUrl());
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}

	@Override
	public OAuthUserAgentFlowOperations getOAuthUserAgentFlowOperations() {
		return oauthUserAgentFlowOperations;
	}
}
