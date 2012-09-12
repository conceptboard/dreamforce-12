package com.conceptboard.social;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import com.google.common.base.Function;

public class Util {
	private Util() {
	}

	public static Function<Connection<?>, ConnectionKey> getKey() {
		return new Function<Connection<?>, ConnectionKey>() {

			@Override
			public ConnectionKey apply(final Connection<?> input) {
				return input.getKey();
			}

		};
	}

	public static Function<? super Connection<?>, ? extends org.springframework.social.connect.ConnectionData> createData() {
		return new Function<Connection<?>, org.springframework.social.connect.ConnectionData>() {

			@Override
			public org.springframework.social.connect.ConnectionData apply(final Connection<?> input) {
				return input.createData();
			}

		};
	}
}
