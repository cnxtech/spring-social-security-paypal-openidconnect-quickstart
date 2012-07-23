/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.quickstart.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.openidconnect.api.PayPalProfile;
import org.springframework.social.quickstart.authorities.UserAuthoritiesService;
import org.springframework.social.quickstart.user.BMLUser;
import org.springframework.social.quickstart.user.OpenIdConnectAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * @author Michael Lavelle
 */
@Service
public class SpringSocialSecurityAuthenticationFactory {

	@Autowired
	private UserAuthoritiesService userAuthoritiesService;

	private boolean includeExpiredConnectionsInAuthorisations = true;

	public void setUserAuthoritiesService(UserAuthoritiesService userAuthoritiesService) {
		this.userAuthoritiesService = userAuthoritiesService;
	}

	protected Authentication createNewAuthentication(UserDetails user, Collection<? extends GrantedAuthority> authorities) {
		return new OpenIdConnectAuthenticationToken(user, authorities, user.getUsername());
	}

	public Authentication createAuthenticationForAllConnections(PayPalProfile payPalProfile, List<Connection<?>> connections) {
		List<GrantedAuthority> authoritiesForUser = userAuthoritiesService.getAuthoritiesForUser(
				toConnectionKeySet(connections, includeExpiredConnectionsInAuthorisations), payPalProfile.getUser_id());
		BMLUser bmlUser = new BMLUser(payPalProfile, authoritiesForUser);
		return createNewAuthentication(bmlUser, authoritiesForUser);
	}

	public Authentication updateAuthenticationForNewConnection(Authentication existingAuthentication, Connection<?> connection) {
		return createNewAuthentication((BMLUser) existingAuthentication.getPrincipal(),
				addAuthority(existingAuthentication, userAuthoritiesService.getProviderAuthority(connection.getKey())));
	}

	public Authentication createAuthenticationFromUserDetails(UserDetails userDetails) {
		return createNewAuthentication(userDetails, userDetails.getAuthorities());
	}

	private Set<ConnectionKey> toConnectionKeySet(List<Connection<?>> connections, boolean includeExpiredConnections) {
		Set<ConnectionKey> connectionKeys = new HashSet<ConnectionKey>();
		for (Connection<?> connection : connections) {
			if (includeExpiredConnections || !connection.hasExpired()) {

				ConnectionData connectionData = connection.createData();
				connectionKeys.add(new ConnectionKey(connectionData.getProviderId(), connectionData.getProviderUserId()));
			}
		}
		return connectionKeys;
	}

	private Collection<? extends GrantedAuthority> addAuthority(Authentication authentication, GrantedAuthority newAuthority) {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.addAll(authentication.getAuthorities());
		if (newAuthority != null) {
			if (!authorities.contains(newAuthority)) {
				authorities.add(newAuthority);
			}
		}

		return authorities;
	}

	public void setIncludeExpiredConnectionsInAuthorisations(boolean includeExpiredConnectionsInAuthorisations) {
		this.includeExpiredConnectionsInAuthorisations = includeExpiredConnectionsInAuthorisations;
	}

}
