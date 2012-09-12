package com.conceptboard.dreamforce12.commands.oauth;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.net.URI;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.stereotype.Component;

import com.conceptboard.social.oauth2useragentflow.ConnectSupport;
import com.conceptboard.social.provider.forcedotcom.connect.ForcedotcomConnectionFactory;

@Component
public class OAuthUserAgentFlowStart {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private ConnectionFactoryLocator connectionFactoryLocator;

	private final ConnectSupport connectSupport = new ConnectSupport();

	/**
	 * Constructs the url for inititiating a user agent flow and opens the page in the browser.
	 * 
	 * @return the url to initiate a user agent flow
	 */
	public String run() {
		final ConnectionFactory<?> cf = connectionFactoryLocator.getConnectionFactory(ForcedotcomConnectionFactory.PROVIDER_ID);
		final String urlForUserAgentFlow = connectSupport.buildOAuthUserAgentFlowUrl(cf);

		openLocalBrowser(urlForUserAgentFlow);
		return urlForUserAgentFlow;
	}

	private void openLocalBrowser(final String urlForUserAgentFlow) {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
			try {
				log.info("Sending user to [ {} ]", urlForUserAgentFlow);
				Desktop.getDesktop().browse(new URI(urlForUserAgentFlow));
			} catch (final Exception e) {
				log.warn("Could not open browser to visit [ {} ]", urlForUserAgentFlow, e);
			}
		} else {
			log.debug("browser ");
		}
	}
}
