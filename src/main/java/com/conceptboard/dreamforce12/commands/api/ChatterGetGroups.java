package com.conceptboard.dreamforce12.commands.api;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;

import com.conceptboard.dreamforce12.AppContext;
import com.conceptboard.social.RefreshingApiCall;
import com.conceptboard.social.provider.forcedotcom.api.ForcedotcomApi;
import com.conceptboard.social.provider.forcedotcom.api.response.ChatterGroup;

@Component
public class ChatterGetGroups {

	@Inject
	private UsersConnectionRepository usersConnectionRepository;
	@Inject
	private AppContext appContext;

	/**
	 * get all chatter groups of this user
	 * 
	 * @return
	 */
	public Iterable<ChatterGroup> run() {
		final Connection<ForcedotcomApi> connection = usersConnectionRepository.createConnectionRepository(appContext.getUserId()).getPrimaryConnection(
		        ForcedotcomApi.class);

		return new RefreshingApiCall<Iterable<ChatterGroup>, ForcedotcomApi>() {

			@Override
			public Iterable<ChatterGroup> execute(final ForcedotcomApi api) {
				return connection.getApi().getChatterOperations().getUserGroups();
			}

		}.call(connection);
	}
}
