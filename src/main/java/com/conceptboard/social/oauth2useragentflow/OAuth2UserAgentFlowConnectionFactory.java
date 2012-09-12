package com.conceptboard.social.oauth2useragentflow;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

public class OAuth2UserAgentFlowConnectionFactory<T> extends OAuth2ConnectionFactory<T> {

	public OAuth2UserAgentFlowConnectionFactory(final String providerId, final OAuth2UserAgentServiceProvider<T> serviceProvider, final ApiAdapter<T> apiAdapter) {
		super(providerId, serviceProvider, apiAdapter);
	}

	public OAuthUserAgentFlowOperations getOAuthUserAgentFlowOperations() {
		return getOAuth2UserAgentFlowServiceProvider().getOAuthUserAgentFlowOperations();
	}

	private OAuth2UserAgentServiceProvider<T> getOAuth2UserAgentFlowServiceProvider() {
		return (OAuth2UserAgentServiceProvider<T>) getServiceProvider();
	}
}
