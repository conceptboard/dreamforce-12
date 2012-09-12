package com.conceptboard.social.provider.forcedotcom.connect;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.oauth2.AccessGrant;

import com.conceptboard.social.oauth2useragentflow.OAuth2UserAgentFlowConnectionFactory;
import com.conceptboard.social.provider.forcedotcom.api.ForcedotcomApi;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

public class ForcedotcomConnectionFactory extends OAuth2UserAgentFlowConnectionFactory<ForcedotcomApi> {

	public static final String PROVIDER_ID = "forcedotcom";
	private final String clientSecret;
	private final String clientId;
	private final String defaultInstanceUrl;
	private final Logger log = LoggerFactory.getLogger(getClass());

	public ForcedotcomConnectionFactory(final String clientId, final String clientSecret, final String instanceUrl) {
		super(PROVIDER_ID, new ForcedotcomServiceProvider(clientId, clientSecret, instanceUrl), new ForcedotcomAdapter());
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.defaultInstanceUrl = instanceUrl;
	}

	/**
	 * Hook for extracting the providerUserId from the returned {@link AccessGrant}, if it is available.
	 * Default implementation returns null, indicating it is not exposed and another remote API call will be required to obtain it.
	 * Subclasses may override.
	 */
	@Override
	protected String extractProviderUserId(final AccessGrant accessGrant) {
		if (!(accessGrant instanceof ForcedotcomAccessGrant)) {
			throw new IllegalStateException("accessGrant has to be of type ForcedotcomAccessGrant");
		}
		return ((ForcedotcomAccessGrant) accessGrant).getId();
	}

	@Override
	public Connection<ForcedotcomApi> createConnection(final AccessGrant _accessGrant) {
		if (!(_accessGrant instanceof ForcedotcomAccessGrant)) {
			throw new IllegalStateException("accessGrant has to be of type ForcedotcomAccessGrant");
		}
		final ForcedotcomAccessGrant accessGrant = (ForcedotcomAccessGrant) _accessGrant;
		return new ForcedotcomOAuth2Connection(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
		        accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(accessGrant.getInstanceUrl()), getApiAdapter());
	}

	@Override
	public Connection<ForcedotcomApi> createConnection(final ConnectionData data) {
		final String instanceUrl = getInstanceUrlFromData(data);
		return new ForcedotcomOAuth2Connection(data, getOAuth2ServiceProvider(instanceUrl), getApiAdapter());
	}

	private String getInstanceUrlFromData(final ConnectionData data) {
		final String additionalData = getAdditionalDataFromConnectionData(data);
		String instanceUrl = getInstanceUrlFromAdditionalData(additionalData);
		if (Strings.isNullOrEmpty(instanceUrl)) {
			log.warn("no instanceUrl for connection [ {} ][ {} ] using default [ {} ]", new Object[] { data.getProviderId(), data.getProviderUserId(),
			        defaultInstanceUrl });
			instanceUrl = defaultInstanceUrl;
		}
		return instanceUrl;
	}

	private String getAdditionalDataFromConnectionData(final ConnectionData data) {
		String additionalData;
		if (data instanceof com.conceptboard.social.ConnectionData) {
			additionalData = ((com.conceptboard.social.ConnectionData) data).getAdditionalData();
		} else {
			throw new IllegalStateException(String.format("ConnectionData has to be of type [ %s ]", com.conceptboard.social.ConnectionData.class.getName()));
		}
		return additionalData;
	}

	static String getInstanceUrlFromAdditionalData(final String additionalData) {
		try {
			if (Strings.isNullOrEmpty(additionalData)) {
				return null;
			}
			final JsonNode jsonNode = new JsonFactory(new ObjectMapper()).createJsonParser(additionalData).readValueAsTree().get("instanceUrl");
			if (jsonNode == null) {
				return null;
			}
			return jsonNode.asText();
		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	private ForcedotcomServiceProvider getOAuth2ServiceProvider(final String instanceUrl) {
		return new ForcedotcomServiceProvider(clientId, clientSecret, instanceUrl);
	}

	static String createAdditionalData(final String instanceUrl) {
		final StringWriter sw = new StringWriter();

		JsonGenerator createJsonGenerator;
		try {
			createJsonGenerator = new JsonFactory(new ObjectMapper()).createJsonGenerator(sw);

			createJsonGenerator.writeStartObject();
			createJsonGenerator.writeObjectField("instanceUrl", instanceUrl);
			createJsonGenerator.writeEndObject();
			createJsonGenerator.close();
		} catch (final IOException e) {
			throw Throwables.propagate(e);
		}
		return sw.toString();
	}
}
