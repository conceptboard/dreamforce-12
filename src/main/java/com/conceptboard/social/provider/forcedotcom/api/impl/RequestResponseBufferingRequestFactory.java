package com.conceptboard.social.provider.forcedotcom.api.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.AbstractClientHttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

public class RequestResponseBufferingRequestFactory extends org.springframework.http.client.AbstractClientHttpRequestFactoryWrapper {

	public RequestResponseBufferingRequestFactory(final ClientHttpRequestFactory requestFactory) {
		super(requestFactory);
	}

	@Override
	protected ClientHttpRequest createRequest(final URI uri, final HttpMethod httpMethod, final ClientHttpRequestFactory requestFactory) throws IOException {
		final ClientHttpRequest request = requestFactory.createRequest(uri, httpMethod);
		if (shouldBuffer(uri, httpMethod)) {
			return new RequestResponseBufferingRequest(request);
		} else {
			return request;
		}
	}

	/**
	 * Indicates whether the request/response exchange for the given URI and method should be buffered in memory.
	 * 
	 * <p>
	 * Default implementation returns {@code true} for all URIs and methods. Subclasses can override this method to change this behavior.
	 * 
	 * @param uri
	 *            the URI
	 * @param httpMethod
	 *            the method
	 * @return {@code true} if the exchange should be buffered; {@code false} otherwise
	 */
	protected boolean shouldBuffer(final URI uri, final HttpMethod httpMethod) {
		return true;
	}

	public static ClientHttpRequestFactory bufferRequests(final ClientHttpRequestFactory requestFactory) {
		return new RequestResponseBufferingRequestFactory(requestFactory);
	}

	private static abstract class AbstractBufferingClientHttpRequest extends AbstractClientHttpRequest {

		private ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream();

		@Override
		protected OutputStream getBodyInternal(final HttpHeaders headers) throws IOException {
			return this.bufferedOutput;
		}

		@Override
		protected ClientHttpResponse executeInternal(final HttpHeaders headers) throws IOException {
			final byte[] bytes = this.bufferedOutput.toByteArray();
			if (headers.getContentLength() == -1) {
				headers.setContentLength(bytes.length);
			}
			final ClientHttpResponse result = executeInternal(headers, bytes);
			this.bufferedOutput = null;
			return result;
		}

		/**
		 * Abstract template method that writes the given headers and content to the HTTP request.
		 * 
		 * @param headers
		 *            the HTTP headers
		 * @param bufferedOutput
		 *            the body content
		 * @return the response object for the executed request
		 */
		protected abstract ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException;

	}

	public static class RequestResponseBufferingRequest extends AbstractBufferingClientHttpRequest {

		private final ClientHttpRequest request;
		private byte[] _bufferedOutput;

		RequestResponseBufferingRequest(final ClientHttpRequest request) {
			Assert.notNull(request, "'request' must not be null");
			this.request = request;
		}

		@Override
		public HttpMethod getMethod() {
			return request.getMethod();
		}

		@Override
		public URI getURI() {
			return request.getURI();
		}

		@Override
		protected ClientHttpResponse executeInternal(final HttpHeaders headers, final byte[] bufferedOutput) throws IOException {
			request.getHeaders().putAll(headers);
			final OutputStream body = request.getBody();
			FileCopyUtils.copy(bufferedOutput, body);
			this._bufferedOutput = bufferedOutput;
			final ClientHttpResponse response = request.execute();
			return new RequestResponseBufferingResponse(this, response);
		}

		private InputStream getBufferedBody() {
			if (this._bufferedOutput == null) {
				return null;
			}
			return new ByteArrayInputStream(this._bufferedOutput);
		}
	}

	public static class RequestResponseBufferingResponse implements ClientHttpResponse {

		private final ClientHttpResponse response;

		private byte[] body;

		private final RequestResponseBufferingRequest request;

		RequestResponseBufferingResponse(final RequestResponseBufferingRequest request, final ClientHttpResponse response) {
			this.response = response;
			this.request = request;
		}

		@Override
		public HttpStatus getStatusCode() throws IOException {
			return response.getStatusCode();
		}

		@Override
		public String getStatusText() throws IOException {
			return response.getStatusText();
		}

		@Override
		public HttpHeaders getHeaders() {
			return response.getHeaders();
		}

		@Override
		public InputStream getBody() throws IOException {
			if (body == null) {
				body = FileCopyUtils.copyToByteArray(response.getBody());
			}
			return new ByteArrayInputStream(body);
		}

		public HttpHeaders getRequestHeaders() {
			return request.getHeaders();
		}

		public InputStream getRequestBody() {
			return request.getBufferedBody();
		}

		@Override
		public void close() {
			response.close();
		}
	}
}
