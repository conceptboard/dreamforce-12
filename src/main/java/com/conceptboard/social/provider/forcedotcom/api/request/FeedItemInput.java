package com.conceptboard.social.provider.forcedotcom.api.request;

public class FeedItemInput {

	private final MessageBodyInput body;
	private final Boolean isBookmarkedByCurrentUser;
	private final String originalFeedItemId;

	public FeedItemInput(final MessageBodyInput body) {
		this(null, body);
	}

	public FeedItemInput(final AttachmentInput attachment, final MessageBodyInput body) {
		this(attachment, body, null, null);
	}

	public FeedItemInput(final AttachmentInput attachment, final MessageBodyInput body, final Boolean isBookmarkedByCurrentUser, final String originalFeedItemId) {
		this.body = body;
		this.isBookmarkedByCurrentUser = isBookmarkedByCurrentUser;
		this.originalFeedItemId = originalFeedItemId;
	}

	public MessageBodyInput getBody() {
		return this.body;
	}

	public Boolean getIsBookmarkedByCurrentUser() {
		return this.isBookmarkedByCurrentUser;
	}

	public String getOriginalFeedItemId() {
		return originalFeedItemId;
	}

}
