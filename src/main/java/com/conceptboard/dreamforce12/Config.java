package com.conceptboard.dreamforce12;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;

import com.conceptboard.social.provider.forcedotcom.connect.ForcedotcomConnectionFactory;
import com.conceptboard.social.simpleconnectionrepository.SimpleUsersConnectionRepositoryPersistence;
import com.google.common.base.Preconditions;

@Configuration
@ComponentScan("com.conceptboard.dreamforce12.commands")
public class Config {

	@Inject
	Environment environment;

	@Bean
	public AppContext getAppContext() {
		final AppContext appContext = new AppContext();
		appContext.setUserId("user");
		return appContext;
	}

	@Bean
	public org.springframework.social.connect.ConnectionFactoryLocator getConnectionFactoryLocator() {
		final ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();
		connectionFactoryRegistry.addConnectionFactory(getForcedotcomConnectionFactory());
		return connectionFactoryRegistry;
	}

	private ConnectionFactory<?> getForcedotcomConnectionFactory() {
		final String clientId = getAndCheckProperty("forcedotcom.oauth.client.id");
		final String clientSecret = getAndCheckProperty("forcedotcom.oauth.client.secret");
		final String instanceurl = getAndCheckProperty("forcedotcom.oauth.client.instanceurl");
		return new ForcedotcomConnectionFactory(clientId, clientSecret, instanceurl);
	}

	@Bean
	public UsersConnectionRepository getUsersConnectionFactory(final ResourceLoader resourceLoader) {
		final SimpleUsersConnectionRepositoryPersistence ret = new SimpleUsersConnectionRepositoryPersistence();
		final String workingDirectory = environment.getProperty("user.dir");
		ret.setDbFile(resourceLoader.getResource("file://" + workingDirectory + "/SimpleUsersConnectionRepository.db.json"));
		return ret;
	}

	private String getAndCheckProperty(final String propertyName) {
		final String v = environment.getProperty(propertyName);
		Preconditions.checkNotNull(v, "%s is missing from configuration", propertyName);
		return v;
	}
}
