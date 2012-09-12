package com.conceptboard.social.provider.forcedotcom.api.request.util;

import java.util.List;

import com.conceptboard.social.provider.forcedotcom.api.request.MessageBodyInput;
import com.conceptboard.social.provider.forcedotcom.api.response.Segment;
import com.conceptboard.social.provider.forcedotcom.api.response.SegmentHashtag;
import com.conceptboard.social.provider.forcedotcom.api.response.SegmentLink;
import com.conceptboard.social.provider.forcedotcom.api.response.SegmentMention;
import com.conceptboard.social.provider.forcedotcom.api.response.SegmentText;
import com.google.common.collect.Lists;

/**
 * Basic Builder to create a List of MessageSegments made from various segments
 * 
 * 
 * @author christian
 */
public class MessageBodyInputBuilder {

	private final List<Segment> segments = Lists.newLinkedList();

	public static MessageBodyInputBuilder builder() {
		return new MessageBodyInputBuilder();
	}

	public MessageBodyInput build() {
		return new MessageBodyInput(Lists.newLinkedList(segments));
	}

	public MessageBodyInputBuilder link(final String url) {
		segments.add(new SegmentLink(url));
		return this;
	}

	public MessageBodyInputBuilder hashtag(final String tag) {
		segments.add(new SegmentHashtag(tag));
		return this;
	}

	public MessageBodyInputBuilder text(final String text) {
		segments.add(new SegmentText(text));
		return this;
	}

	public MessageBodyInputBuilder mention(final String id) {
		segments.add(new SegmentMention(id));
		return this;
	}

	public MessageBodyInputBuilder text(final char c) {
		return this.text(String.valueOf(c));
	}
}
