package com.conceptboard.social.provider.forcedotcom.api.response;

public interface ForcedotcomErrorResponse {
	public String getMessage();

	public String getErrorCode();

	public String[] getFields();
}
