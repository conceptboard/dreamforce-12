package com.conceptboard.social.provider.forcedotcom.api.impl;

import java.util.LinkedList;
import org.springframework.web.client.RestTemplate;

import com.conceptboard.social.provider.forcedotcom.api.ChatterOperations;
import com.conceptboard.social.provider.forcedotcom.api.request.AttachmentInput;
import com.conceptboard.social.provider.forcedotcom.api.request.FeedItemCommentInput;
import com.conceptboard.social.provider.forcedotcom.api.request.FeedItemInput;
import com.conceptboard.social.provider.forcedotcom.api.request.MessageBodyInput;
import com.conceptboard.social.provider.forcedotcom.api.response.ChatterGroup;
import com.conceptboard.social.provider.forcedotcom.api.response.ChatterGroupPage;
import com.conceptboard.social.provider.forcedotcom.api.response.Comment;
import com.conceptboard.social.provider.forcedotcom.api.response.FeedItem;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ChatterTemplate implements ChatterOperations {

	private static final String URL_USER_GROUPS = "/chatter/users/me/groups";
	private final RestTemplate restTemplate;
	private final String baseUrl;

	public ChatterTemplate(final RestTemplate restTemplate, final String baseUrl) {
		this.restTemplate = restTemplate;
		this.baseUrl = baseUrl;
	}

	@Override
	public Iterable<ChatterGroup> getUserGroups() {
		final LinkedList<ChatterGroupPage> groupPages = Lists.newLinkedList();
		String nextPageUrl = URL_USER_GROUPS;
		do {
			final ChatterGroupPage nextGroup = getRestTemplate().getForObject(baseUrl + nextPageUrl, ChatterGroupPage.class);
			groupPages.add(nextGroup);
			nextPageUrl = nextGroup == null ? null : nextGroup.getNextPageUrl();
		} while (nextPageUrl != null);

		return ImmutableList.copyOf(Iterables.concat(Iterables.transform(groupPages, new Function<ChatterGroupPage, Iterable<ChatterGroup>>() {

			@Override
			public Iterable<ChatterGroup> apply(final ChatterGroupPage input) {
				return Lists.newArrayList(input.getGroups());
			}

		})));
	}

	@Override
	public FeedItem postToChatterGroup(final String chatterGroupId, final AttachmentInput attachment, final MessageBodyInput body) {
		return getRestTemplate().postForObject(baseUrl + "/chatter/feeds/record/{chatterGroupId}/feed-items", new FeedItemInput(attachment, body),
		        FeedItem.class, chatterGroupId);
	}

	@Override
	public Comment postFeedItemComment(final String feedItemId, final MessageBodyInput body) {
		return getRestTemplate()
		        .postForObject(baseUrl + "/chatter/feed-items/{feedItemId}/comments", new FeedItemCommentInput(body), Comment.class, feedItemId);
	}

	private RestTemplate getRestTemplate() {
		return restTemplate;
	}
}
