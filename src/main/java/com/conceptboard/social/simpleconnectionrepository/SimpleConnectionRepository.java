package com.conceptboard.social.simpleconnectionrepository;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class SimpleConnectionRepository implements ConnectionRepository {

	private final Multimap<String, ConnectionData> usersConnections;
	private final String userId;
	private final Multimap<String, ConnectionData> allConnections;
	private final ConnectionFactoryLocator connectionFactoryLocator;

	public SimpleConnectionRepository(final Multimap<String, ConnectionData> connections, final String userId,
	        final ConnectionFactoryLocator connectionFactoryLocator) {
		this.usersConnections = Multimaps.filterKeys(connections, Predicates.equalTo(userId));
		this.allConnections = connections;
		this.userId = userId;
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		final MultiValueMap<String, Connection<?>> ret = toMultiValueMapPerProviderId(usersConnections.values());
		return ret;
	}

	@Override
	public List<Connection<?>> findConnections(final String providerId) {
		return Lists.newLinkedList(Iterables.transform(Iterables.filter(usersConnections.values(), new Predicate<ConnectionData>() {

			@Override
			public boolean apply(final ConnectionData input) {
				return providerId.equals(input.getProviderId());
			}
		}), toConnection()));
	}

	@Override
	public <A> List<Connection<A>> findConnections(final Class<A> apiType) {
		final String providerId = connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
		return Lists.newLinkedList(Iterables.transform(Iterables.filter(usersConnections.values(), new Predicate<ConnectionData>() {

			@Override
			public boolean apply(final ConnectionData input) {
				return input.getProviderId().equals(providerId);
			}

		}), new Function<ConnectionData, Connection<A>>() {

			@SuppressWarnings("unchecked" /* we can safely cast here, because we filtered on the providerid, associated with the apiType */)
			@Override
			public Connection<A> apply(final ConnectionData input) {
				return (Connection<A>) toConnection(input);
			}

		}));
	}

	@Override
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(final MultiValueMap<String, String> providerUserIds) {
		return toMultiValueMapPerProviderId(Iterables.filter(usersConnections.values(), new Predicate<ConnectionData>() {

			@Override
			public boolean apply(final ConnectionData input) {
				final List<String> userIds = providerUserIds.get(input.getProviderId());
				return userIds != null && userIds.contains(input.getProviderUserId());
			}

		}));
	}

	@Override
	public Connection<?> getConnection(final ConnectionKey connectionKey) {
		return toConnection(Iterables.tryFind(usersConnections.values(), new Predicate<ConnectionData>() {

			@Override
			public boolean apply(final ConnectionData input) {
				return equalByKey(connectionKey, input);
			}

		}).orNull());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A> Connection<A> getConnection(final Class<A> apiType, final String providerUserId) {
		final ConnectionFactory<A> connectionFactory = connectionFactoryLocator.getConnectionFactory(apiType);
		final String providerId = connectionFactory.getProviderId();
		final ConnectionKey connectionKey = new ConnectionKey(providerId, providerUserId);
		return (Connection<A>) toConnection(Iterables.tryFind(usersConnections.values(), new Predicate<ConnectionData>() {

			@Override
			public boolean apply(final ConnectionData input) {
				return equalByKey(connectionKey, input);
			}

		}).orNull());
	}

	@Override
	public <A> Connection<A> getPrimaryConnection(final Class<A> apiType) {
		return Iterables.getLast(findConnections(apiType));
	}

	@Override
	public <A> Connection<A> findPrimaryConnection(final Class<A> apiType) {
		return Iterables.getLast(findConnections(apiType), null);
	}

	@Override
	public void addConnection(final Connection<?> connection) {
		usersConnections.put(userId, connection.createData());
	}

	@Override
	public void updateConnection(final Connection<?> connection) {
		usersConnections.put(userId, connection.createData());
	}

	@Override
	public void removeConnections(final String providerId) {
		Iterables.removeIf(allConnections.entries(), new Predicate<Entry<String, ConnectionData>>() {

			@Override
			public boolean apply(final Entry<String, ConnectionData> input) {
				return userId.equals(input.getKey()) && providerId.equals(input.getValue().getProviderId());
			}

		});
	}

	@Override
	public void removeConnection(final ConnectionKey connectionKey) {
		Iterables.removeIf(allConnections.entries(), new Predicate<Entry<String, ConnectionData>>() {

			@Override
			public boolean apply(final Entry<String, ConnectionData> input) {
				return userId.equals(input.getKey()) && equalByKey(connectionKey, input.getValue());
			}

		});
	}

	private MultiValueMap<String, Connection<?>> toMultiValueMapPerProviderId(final Iterable<ConnectionData> values) {
		final MultiValueMap<String, Connection<?>> ret = new LinkedMultiValueMap<String, Connection<?>>();
		for (final ConnectionData c : values) {
			ret.add(c.getProviderId(), toConnection(c));
		}
		return ret;
	}

	private Connection<?> toConnection(final ConnectionData c) {
		if (c == null) {
			return null;
		}
		return connectionFactoryLocator.getConnectionFactory(c.getProviderId()).createConnection(c);
	}

	private Function<ConnectionData, Connection<?>> toConnection() {
		return new Function<ConnectionData, Connection<?>>() {
			@Override
			public Connection<?> apply(final ConnectionData input) {
				return toConnection(input);
			}
		};
	}

	static boolean equalByKey(final ConnectionKey connectionKey, final ConnectionData data) {
		return Objects.equal(data.getProviderId(), connectionKey.getProviderId()) && Objects.equal(data.getProviderUserId(), connectionKey.getProviderUserId());
	}
}
