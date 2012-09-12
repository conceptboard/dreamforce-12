package com.conceptboard.social.simpleconnectionrepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import com.google.common.base.Predicate;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.google.common.reflect.Reflection;

/**
 * simple Multimap based UsersConnectionRepository
 * 
 * @author christian
 */
public class SimpleUsersConnectionRepository implements UsersConnectionRepository {

	public class GuardingInvocationHandler implements InvocationHandler {

		private final Object proxied;
		private final Object guard;

		public GuardingInvocationHandler(final Object proxied, final Object guard) {
			this.proxied = proxied;
			this.guard = guard;
		}

		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			synchronized (guard) {
				return method.invoke(proxied, args);
			}
		}

	}

	// @GuardedBy("guard")
	protected final Multimap<String, ConnectionData> connections = LinkedHashMultimap.create();

	protected final Object guard = new Object();

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Override
	public List<String> findUserIdsWithConnection(final Connection<?> connection) {
		synchronized (guard) {
			return Lists.newLinkedList(Multimaps.filterValues(connections, new Predicate<ConnectionData>() {

				@Override
				public boolean apply(final ConnectionData input) {
					return SimpleConnectionRepository.equalByKey(connection.getKey(), input);
				}

			}).keySet());
		}
	}

	@Override
	public Set<String> findUserIdsConnectedTo(final String providerId, final Set<String> providerUserIds) {
		final Set<String> ret = Sets.newLinkedHashSet();
		synchronized (guard) {
			ret.addAll(Multimaps.filterValues(connections, new Predicate<ConnectionData>() {

				@Override
				public boolean apply(final ConnectionData input) {
					return providerId.equals(input.getProviderId()) && providerUserIds.contains(input.getProviderUserId());
				}

			}).keySet());
		}
		return ret;
	}

	@Override
	public ConnectionRepository createConnectionRepository(final String userId) {
		synchronized (guard) {
			final ConnectionRepository proxied = new SimpleConnectionRepository(connections, userId, connectionFactoryLocator);
			return Reflection.newProxy(ConnectionRepository.class, new GuardingInvocationHandler(proxied, guard));
		}
	}

}