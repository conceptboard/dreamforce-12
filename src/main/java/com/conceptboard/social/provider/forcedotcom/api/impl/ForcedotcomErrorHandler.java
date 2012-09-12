package com.conceptboard.social.provider.forcedotcom.api.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.UncategorizedApiException;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.conceptboard.social.provider.forcedotcom.api.impl.RequestResponseBufferingRequestFactory.RequestResponseBufferingResponse;
import com.conceptboard.social.provider.forcedotcom.api.response.ForcedotcomErrorResponse;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

public class ForcedotcomErrorHandler extends DefaultResponseErrorHandler {

	private final ObjectMapper om;
	private final Logger log = LoggerFactory.getLogger(getClass());

	public ForcedotcomErrorHandler(final ObjectMapper om) {
		this.om = om;
	}

	@Override
	public void handleError(final ClientHttpResponse response) throws IOException {
		logErrorResponse(response);
		final List<ForcedotcomErrorResponse> errorDetails = extractErrorDetailsFromResponse(response);
		if (errorDetails == null) {
			handleUncategorizedError(response, errorDetails);
		}

		handleForcedotcomError(response.getStatusCode(), errorDetails);

		// if not otherwise handled, do default handling and wrap with UncategorizedApiException
		handleUncategorizedError(response, errorDetails);
	}

	private void logErrorResponse(final ClientHttpResponse response) {
		try {
			final String responseBody = CharStreams.toString(new InputStreamReader(response.getBody(), Charsets.UTF_8));
			if (response instanceof RequestResponseBufferingResponse) {
				final RequestResponseBufferingResponse r = (RequestResponseBufferingResponse) response;
				final String requestBody;
				if (r.getRequestBody() == null) {
					requestBody = "could not caputre requestbody";
				} else {
					requestBody = CharStreams.toString(new InputStreamReader(r.getRequestBody(), Charsets.UTF_8));
				}
				log.warn("error in response [ {} ] [ {} ] [ {} ] [ {} ] originalRequest  [ {} ] [ {} ]",
				        new Object[] { response.getStatusCode(), response.getStatusText(), response.getHeaders(), responseBody, r.getRequestHeaders(),
				                requestBody });
			} else {
				log.warn("error in response [ {} ] [ {} ] [ {} ] [ {} ]",
				        new Object[] { response.getStatusCode(), response.getStatusText(), response.getHeaders(), responseBody });
			}
		} catch (final IOException e) {
			try {
				log.warn("error on response [ {} ] [ {} ] [ {} ]", new Object[] { response.getStatusCode(), response.getStatusText(), response.getHeaders() });
			} catch (final IOException e1) {
				log.warn("error on response [ {} ]", response);
			}
		}
	}

	void handleForcedotcomError(final HttpStatus statusCode, final List<ForcedotcomErrorResponse> errorDetails) {
		if (statusCode.equals(HttpStatus.UNAUTHORIZED)) {
			if (Iterables.any(errorDetails, withErrorCode("INVALID_SESSION_ID"))) {
				throw new ExpiredAuthorizationException();
			}
		}
	}

	private Predicate<ForcedotcomErrorResponse> withErrorCode(final String errorCode) {
		return Predicates.<ForcedotcomErrorResponse, String> compose(Predicates.equalTo(errorCode), errorCode());
	}

	private void handleUncategorizedError(final ClientHttpResponse response, final List<ForcedotcomErrorResponse> errorDetails) {
		try {
			final String responseBody = CharStreams.toString(new InputStreamReader(response.getBody(), Charsets.UTF_8));
			if ("Bad_OAuth_Token".equals(responseBody)) {
				throw new ExpiredAuthorizationException();
			}
			final HttpStatus statusCode = response.getStatusCode();
			if (statusCode.equals(HttpStatus.UNAUTHORIZED)) {
				throw new ExpiredAuthorizationException();
			}
			if (statusCode.equals(HttpStatus.FORBIDDEN)) {
				throw new ExpiredAuthorizationException();
			}
			super.handleError(response);
		} catch (final Exception e) {
			if (errorDetails != null) {
				throw new UncategorizedApiException(Joiner.on(",").join(Iterables.transform(errorDetails, message())), e);
			} else {
				throw new UncategorizedApiException("No error details from Forcedotcom", e);
			}
		}
	}

	private Function<ForcedotcomErrorResponse, String> message() {
		return new Function<ForcedotcomErrorResponse, String>() {

			@Override
			public String apply(final ForcedotcomErrorResponse input) {
				return input.getMessage();
			}
		};
	}

	private Function<ForcedotcomErrorResponse, String> errorCode() {
		return new Function<ForcedotcomErrorResponse, String>() {

			@Override
			public String apply(final ForcedotcomErrorResponse input) {
				return input.getErrorCode();
			}
		};
	}

	private List<ForcedotcomErrorResponse> extractErrorDetailsFromResponse(final ClientHttpResponse response) throws IOException {
		try {
			return this.om.readValue(response.getBody(), new TypeReference<List<ForcedotcomErrorResponse>>() {

			});
		} catch (final JsonProcessingException e) {
			try {
				final LinkedList<ForcedotcomErrorResponse> newLinkedList = Lists.newLinkedList();
				newLinkedList.add(this.om.readValue(response.getBody(), ForcedotcomErrorResponse.class));
				return newLinkedList;
			} catch (final JsonProcessingException e2) {
				log.warn("error processing forcedotcom error", e);
				return null;
			}
		}
	}
}
