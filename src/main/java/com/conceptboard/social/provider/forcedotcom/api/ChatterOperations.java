package com.conceptboard.social.provider.forcedotcom.api;

import com.conceptboard.social.provider.forcedotcom.api.request.AttachmentInput;
import com.conceptboard.social.provider.forcedotcom.api.request.MessageBodyInput;
import com.conceptboard.social.provider.forcedotcom.api.response.ChatterGroup;
import com.conceptboard.social.provider.forcedotcom.api.response.Comment;
import com.conceptboard.social.provider.forcedotcom.api.response.FeedItem;

public interface ChatterOperations {

	Iterable<ChatterGroup> getUserGroups();

	FeedItem postToChatterGroup(String chatterGroupId, AttachmentInput attachment, MessageBodyInput body);

	Comment postFeedItemComment(String feedItemId, MessageBodyInput body);
}
