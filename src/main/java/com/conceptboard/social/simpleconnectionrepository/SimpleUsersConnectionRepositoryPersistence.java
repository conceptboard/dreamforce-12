package com.conceptboard.social.simpleconnectionrepository;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.mrbean.MrBeanModule;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.social.connect.ConnectionRepository;

import com.conceptboard.social.ConnectionData;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.reflect.Reflection;

/**
 * quick and dirty persistence for the userconnections
 * 
 * @author christian
 * 
 */
public class SimpleUsersConnectionRepositoryPersistence extends SimpleUsersConnectionRepository {

	private Resource dbFile;

	@PostConstruct
	public void init() {
		try {
			dbFile.getFile().createNewFile();
			synchronizeRepositoryFromFile();
		} catch (final IOException e) {
			throw Throwables.propagate(e);
		}
	}

	@Required
	public void setDbFile(final Resource dbFile) {
		this.dbFile = dbFile;
	}

	@Override
	public ConnectionRepository createConnectionRepository(final String userId) {
		return Reflection.newProxy(ConnectionRepository.class, new SavingInvocationHandler(super.createConnectionRepository(userId)));
	}

	private void synchronizeRepositoryFromFile() {
		if (!dbFile.exists()) {
			return;
		}
		final ObjectMapper objectMapper = getObjectMapper();
		try {
			final ObjectReader reader = objectMapper.reader(new TypeReference<List<SimpleUsersConnectionRepositoryPersistence.SimpleConnectionEntry>>() {
			});
			final List<SimpleUsersConnectionRepositoryPersistence.SimpleConnectionEntry> data = reader.readValue(dbFile.getFile());

			synchronized (guard) {
				for (final SimpleUsersConnectionRepositoryPersistence.SimpleConnectionEntry d : data) {
					this.connections.put(d.userId, d.connectionData);
				}
			}
			log.info("successfully loaded [ {} ] entries", this.connections.size());
		} catch (final Exception e) {
			log.warn("could not load userconnectionrepository", e);
		}
	}

	private void synchronizeRepositoryToFile() {
		if (!dbFile.exists()) {
			return;
		}
		final ObjectMapper objectMapper = getObjectMapper();
		final List<SimpleUsersConnectionRepositoryPersistence.SimpleConnectionEntry> data;
		synchronized (guard) {
			data = Lists
			        .newArrayList(Collections2.transform(
			                connections.entries(),
			                new Function<Entry<String, org.springframework.social.connect.ConnectionData>, SimpleUsersConnectionRepositoryPersistence.SimpleConnectionEntry>() {

				                @Override
				                public SimpleConnectionEntry apply(final Entry<String, org.springframework.social.connect.ConnectionData> input) {
					                return new SimpleConnectionEntry(input.getKey(), input.getValue().getProviderId(), new ConnectionData(input.getValue()));
				                }
			                }));
		}
		try {
			objectMapper.writer().writeValue(dbFile.getFile(), data);
		} catch (final Exception e) {
			log.warn("could not save userconnectionrepository", e);
		}

	}

	private class SavingInvocationHandler implements InvocationHandler {
		private final ConnectionRepository proxied;

		protected SavingInvocationHandler(final ConnectionRepository proxied) {
			this.proxied = proxied;
		}

		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			synchronized (guard) {
				try {
					return method.invoke(proxied, args);
				} finally {
					synchronizeRepositoryToFile();
				}
			}
		}
	}

	private ObjectMapper getObjectMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new SimpleUsersConnectionRepositoryPersistence.ConnectionDataModule());
		objectMapper.registerModule(new MrBeanModule());
		return objectMapper;
	}

	public static class ConnectionDataModule extends Module {

		@JsonIgnoreProperties
		public abstract static class ConnectionDataMixin {
			@JsonCreator
			public ConnectionDataMixin(@JsonProperty("providerId") final String providerId, @JsonProperty("providerUserId") final String providerUserId,
			        @JsonProperty("displayName") final String displayName, @JsonProperty("profileUrl") final String profileUrl,
			        @JsonProperty("imageUrl") final String imageUrl, @JsonProperty("accessToken") final String accessToken,
			        @JsonProperty("secret") final String secret, @JsonProperty("refreshToken") final String refreshToken,
			        @JsonProperty("expireTime") final Long expireTime, @JsonProperty("additionalData") final String additionalData) {
			}
		}

		@Override
		public String getModuleName() {
			return getClass().getSimpleName();
		}

		@Override
		public Version version() {
			return Version.unknownVersion();
		}

		@Override
		public void setupModule(final SetupContext context) {
			context.setMixInAnnotations(ConnectionData.class, ConnectionDataMixin.class);
		}

	}

	public static class SimpleConnectionEntry {
		public String userId;
		public String providerId;
		public ConnectionData connectionData;

		@JsonCreator
		public SimpleConnectionEntry(@JsonProperty("userId") final String userId, @JsonProperty("providerId") final String providerId,
		        @JsonProperty("connectionData") final ConnectionData connectionData) {
			this.userId = userId;
			this.providerId = providerId;
			this.connectionData = connectionData;
		}
	}
}
