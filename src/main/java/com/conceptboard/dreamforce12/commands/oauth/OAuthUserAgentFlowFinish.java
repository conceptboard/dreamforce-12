package com.conceptboard.dreamforce12.commands.oauth;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import com.conceptboard.dreamforce12.AppContext;
import com.conceptboard.social.oauth2useragentflow.ConnectSupport;
import com.conceptboard.social.provider.forcedotcom.connect.ForcedotcomConnectionFactory;

@Component
public class OAuthUserAgentFlowFinish {

	@Inject
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Inject
	private UsersConnectionRepository usersConnectionRepository;

	@Inject
	private AppContext appContext;

	private final ConnectSupport connectSupport = new ConnectSupport();

	/***
	 * parses the callback url, creates a connection and stores it in the connection repository
	 * 
	 * @param oauthcallbackuri
	 * @return
	 */
	public Connection<?> run(final String oauthcallbackuri) {
		final ConnectionFactory<?> cf = connectionFactoryLocator.getConnectionFactory(ForcedotcomConnectionFactory.PROVIDER_ID);
		final Connection<?> connection = connectSupport.completeOAuthUserAgentFlowConnection(cf, oauthcallbackuri);
		usersConnectionRepository.createConnectionRepository(appContext.getUserId()).addConnection(connection);
		return connection;
	}
}
