package com.conceptboard.dreamforce12.commands.api;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;

import com.conceptboard.dreamforce12.AppContext;
import com.conceptboard.social.provider.forcedotcom.api.ForcedotcomApi;
import com.conceptboard.social.provider.forcedotcom.api.request.util.MessageBodyInputBuilder;
import com.conceptboard.social.provider.forcedotcom.api.response.FeedItem;

@Component
public class PostToChatterGroup {

	@Inject
	private UsersConnectionRepository usersConnectionRepository;
	@Inject
	private AppContext appContext;

	/**
	 * post a simple text message to a chatter group
	 * 
	 * @return
	 */
	public FeedItem run(final String chatterGroupId, final String text) {
		final Connection<ForcedotcomApi> connection = usersConnectionRepository.createConnectionRepository(appContext.getUserId()).getPrimaryConnection(
		        ForcedotcomApi.class);

		return connection.getApi().getChatterOperations().postToChatterGroup(chatterGroupId, null, new MessageBodyInputBuilder().text(text).build());
	}
}
