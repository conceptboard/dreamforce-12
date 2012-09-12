package com.conceptboard.social.oauth2useragentflow;

import org.springframework.social.oauth2.AccessGrant;

public interface OAuthUserAgentFlowOperations {

	AccessGrant extractAccessGrant(String oauthcallbackuri);
}
