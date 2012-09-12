package com.conceptboard.social;

import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.connect.Connection;

public abstract class RefreshingApiCall<T, A> {
	abstract public T execute(A api);

	public final T call(final Connection<A> connection) {
		return _withRefresh(connection);
	}

	private T _withRefresh(final Connection<A> connection) {
		try {
			return this.execute(connection.getApi());
		} catch (final ExpiredAuthorizationException e) {
			connection.refresh();
			return this.execute(connection.getApi());
		}
	}
}