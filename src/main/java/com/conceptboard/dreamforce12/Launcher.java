package com.conceptboard.dreamforce12;

import static com.conceptboard.social.Util.getKey;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.mrbean.MrBeanModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.social.connect.Connection;

import com.conceptboard.dreamforce12.commands.api.ChatterGetGroups;
import com.conceptboard.dreamforce12.commands.api.PostToChatterGroup;
import com.conceptboard.dreamforce12.commands.oauth.OAuthListConnections;
import com.conceptboard.dreamforce12.commands.oauth.OAuthUserAgentFlowFinish;
import com.conceptboard.dreamforce12.commands.oauth.OAuthUserAgentFlowStart;
import com.conceptboard.social.Util;
import com.conceptboard.social.provider.forcedotcom.api.response.ChatterGroup;
import com.conceptboard.social.provider.forcedotcom.api.response.FeedItem;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.ShellFactory;

/**
 * Simple cliché based Launcher. Complex objects will be converted to json before printing them.
 * 
 * @author christian
 */
public class Launcher {

	private final AnnotationConfigApplicationContext applicationContext;
	private final JsonFactory jsonFactory;
	private static final Logger log = LoggerFactory.getLogger(Launcher.class);

	public Launcher(final Properties credentialsProperties) {
		applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.getEnvironment().getPropertySources().addFirst(new PropertiesPropertySource("credentials", credentialsProperties));
		applicationContext.register(Config.class);
		applicationContext.refresh();
		jsonFactory = createJsonFactory();
	}

	@Command(name = "oauth-start")
	public Object oauthStart() {
		return applicationContext.getBean(OAuthUserAgentFlowStart.class).run();
	}

	@Command(name = "oauth-finish")
	public Object oauthFinish(
	        @Param(name = "OAuthCallbackUrl", description = "the callback url from the oauth-start command; please put it in single quotes") final String oAuthCallbackUrl) {
		return applicationContext.getBean(OAuthUserAgentFlowFinish.class).run(oAuthCallbackUrl).getKey();
	}

	@Command(name = "oauth-list-connections")
	public Object oauthListConnections() {
		final List<Connection<?>> connectionList = applicationContext.getBean(OAuthListConnections.class).run();
		return Iterables.transform(connectionList, getKey());
	}

	@Command(name = "oauth-list-connections")
	public Object oauthListConnections(@Param(name = "fulldetails") final boolean fulldetails) {
		if (!fulldetails) {
			return oauthListConnections();
		}
		final List<Connection<?>> connectionList = applicationContext.getBean(OAuthListConnections.class).run();
		return toJson(Lists.transform(connectionList, Util.createData()));
	}

	@Command(name = "chatter-get-groups")
	public String chatterGetGroups() {
		final Iterable<ChatterGroup> result = applicationContext.getBean(ChatterGetGroups.class).run();
		return toJson(result);
	}

	@Command(name = "chatter-post-to-group")
	public String chatterPostToGroupGroup(@Param(name = "groupId", description = "the chatter group id the text should be posted to") final String groupId,
	        @Param(name = "text", description = "the text to post to the chatter group") final String text) {
		final FeedItem result = applicationContext.getBean(PostToChatterGroup.class).run(groupId, text);
		return toJson(result);
	}

	private String toJson(final Object o) {
		final StringWriter writer = new StringWriter();
		try {
			jsonFactory.createJsonGenerator(writer).useDefaultPrettyPrinter().writeObject(o);
		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
		return writer.toString();
	}

	private JsonFactory createJsonFactory() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new MrBeanModule());
		objectMapper.setVisibility(JsonMethod.FIELD, Visibility.NONE);// ignore all fields; we use getters, but MrBeanModule introduces duplicate fields
		objectMapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
		return new JsonFactory(objectMapper);
	}

	public static void main(final String[] args) throws IOException {

		final Properties credentialsProperties = getCredentialsProperties(args);
		if (credentialsProperties == null) {
			log.error("credentials missing from command line");
			return;
		}

		ShellFactory.createConsoleShell("df12", "Dreamforce 2012 - Chatter Api with Spring examples", new Launcher(credentialsProperties)).commandLoop();
	}

	private static Properties getCredentialsProperties(final String[] args) {
		final Properties credentialsProperties = new Properties();
		final PropertySource<?> ps = new SimpleCommandLinePropertySource(args);
		final String credentials = (String) ps.getProperty("credentials");
		if (Strings.isNullOrEmpty(credentials)) {
			return null;
		}
		try {
			credentialsProperties.load(new FileReader(credentials));
		} catch (final FileNotFoundException e) {
			log.warn("could not load credentials from file {}: {}", credentials, e.getLocalizedMessage());
			return null;
		} catch (final IOException e) {
			log.warn("could not load credentials from file {}: {}", new Object[] { credentials, e.getLocalizedMessage(), e });
			return null;
		}
		return credentialsProperties;
	}
}
