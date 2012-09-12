package com.conceptboard.social.oauth2useragentflow;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;


/**
 * 
 * modelled after {@linkplain org.springframework.social.connect.web.ConnectSupport} for UserAgentFlow
 * 
 * @see org.springframework.social.connect.web.ConnectSupport
 * @author christian
 * 
 */
public class ConnectSupport {

	/**
	 * create a URL to initiate the User-Agent-Flow for the given ConnectionFactory
	 * 
	 * @param connectionFactory
	 * @return
	 */
	public <A> String buildOAuthUserAgentFlowUrl(final ConnectionFactory<A> connectionFactory) {
		if (connectionFactory instanceof OAuth2UserAgentFlowConnectionFactory) {
			final OAuth2UserAgentFlowConnectionFactory<A> oAuth2ConnectionFactory = (OAuth2UserAgentFlowConnectionFactory<A>) connectionFactory;
			return buildOAuth2UserAgentFlowUrl(oAuth2ConnectionFactory);
		} else {
			throw new IllegalStateException(String.format("ConnectionFactory [ %s ] is not supported", connectionFactory.getClass()));
		}
	}

	private <A> String buildOAuth2UserAgentFlowUrl(final OAuth2UserAgentFlowConnectionFactory<A> connectionFactory) {
		final OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();
		oAuth2Parameters.setRedirectUri("https://login.salesforce.com/services/oauth2/success");
		oAuth2Parameters.setScope("chatter_api refresh_token id");
		return connectionFactory.getOAuthOperations().buildAuthorizeUrl(GrantType.IMPLICIT_GRANT, oAuth2Parameters);
	}

	/**
	 * complete a User-Agent-Flow from the given callback url
	 * 
	 * @param connectionFactory
	 * @return
	 */
	public <A> Connection<A> completeOAuthUserAgentFlowConnection(final ConnectionFactory<A> connectionFactory, final String oauthcallbackuri) {
		if (connectionFactory instanceof OAuth2UserAgentFlowConnectionFactory) {
			final OAuth2UserAgentFlowConnectionFactory<A> oAuth2ConnectionFactory = (OAuth2UserAgentFlowConnectionFactory<A>) connectionFactory;
			return completeOAuth2UserAgentFlowConnection(oAuth2ConnectionFactory, oauthcallbackuri);
		} else {
			throw new IllegalStateException(String.format("ConnectionFactory [ %s ] is not supported", connectionFactory.getClass()));
		}
	}

	private <A> Connection<A> completeOAuth2UserAgentFlowConnection(final OAuth2UserAgentFlowConnectionFactory<A> connectionFactory,
	        final String oauthcallbackuri) {
		final AccessGrant accessGrant = connectionFactory.getOAuthUserAgentFlowOperations().extractAccessGrant(oauthcallbackuri);
		final Connection<A> connection = connectionFactory.createConnection(accessGrant);
		return connection;
	}
}
