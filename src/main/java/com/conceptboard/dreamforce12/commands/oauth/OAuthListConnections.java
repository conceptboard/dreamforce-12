package com.conceptboard.dreamforce12.commands.oauth;

import java.util.List;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;

import com.conceptboard.dreamforce12.AppContext;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

@Component
public class OAuthListConnections {

	@Inject
	private UsersConnectionRepository usersConnectionRepository;
	@Inject
	private AppContext appContext;

	public List<Connection<?>> run() {
		return ImmutableList
		        .copyOf(Iterables.concat(usersConnectionRepository.createConnectionRepository(appContext.getUserId()).findAllConnections().values()));
	}
}
