package com.conceptboard.social.provider.forcedotcom.api.impl;

import static com.google.common.base.Preconditions.checkArgument;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.mrbean.MrBeanModule;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.web.util.UriTemplate;

import com.conceptboard.social.provider.forcedotcom.api.ChatterOperations;
import com.conceptboard.social.provider.forcedotcom.api.ForcedotcomApi;
import com.conceptboard.social.provider.forcedotcom.api.response.ForcedotcomUserIdentity;
import com.conceptboard.social.provider.forcedotcom.api.response.ForcedotcomUserProfileInformation;
import com.google.common.base.Strings;

public class ForcedotcomApiTemplate extends AbstractOAuth2ApiBinding implements ForcedotcomApi {

	private static final String VERSION = "25.0";

	private final String ME_USER_PROFILE_URL_TEMPLATE = "/chatter/users/me";

	private final UriTemplate BASE_URI_TEMPLATE = new UriTemplate("{instanceName}/services/data/v{version}");

	private final String baseUrl;

	private final ChatterOperations chatterOperations;

	public ForcedotcomApiTemplate(final String accessToken, final String instanceName) {
		super(accessToken);
		initialize();
		checkArgument(!Strings.isNullOrEmpty(instanceName));
		this.baseUrl = BASE_URI_TEMPLATE.expand(instanceName, VERSION).toString();

		chatterOperations = new ChatterTemplate(getRestTemplate(), baseUrl);
	}

	private void initialize() {
		super.setRequestFactory(RequestResponseBufferingRequestFactory.bufferRequests(getRestTemplate().getRequestFactory()));
	}

	@Override
	public ForcedotcomUserIdentity getUserIdentity(final String forcedotcomId) {
		return getRestTemplate().getForObject(forcedotcomId, ForcedotcomUserIdentity.class);
	}

	@Override
	protected MappingJacksonHttpMessageConverter getJsonMessageConverter() {
		final MappingJacksonHttpMessageConverter ret = new MappingJacksonHttpMessageConverter();
		ret.setObjectMapper(getObjectMapper());
		return ret;
	}

	@Override
	public ChatterOperations getChatterOperations() {
		return this.chatterOperations;
	}

	@Override
	public ForcedotcomUserProfileInformation getUserProfile() {
		return getRestTemplate().getForObject(baseUrl + ME_USER_PROFILE_URL_TEMPLATE, ForcedotcomUserProfileInformation.class);
	}

	@Override
	protected void configureRestTemplate(final org.springframework.web.client.RestTemplate restTemplate) {
		restTemplate.setErrorHandler(new ForcedotcomErrorHandler(getObjectMapper()));
	}

	public static ObjectMapper getObjectMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, false);
		objectMapper.registerModule(new MrBeanModule());
		return objectMapper;
	}
}
