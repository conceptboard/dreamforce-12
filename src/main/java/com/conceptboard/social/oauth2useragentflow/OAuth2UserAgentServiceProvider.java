package com.conceptboard.social.oauth2useragentflow;

import org.springframework.social.oauth2.OAuth2ServiceProvider;

public interface OAuth2UserAgentServiceProvider<T> extends OAuth2ServiceProvider<T> {

	OAuthUserAgentFlowOperations getOAuthUserAgentFlowOperations();
}
