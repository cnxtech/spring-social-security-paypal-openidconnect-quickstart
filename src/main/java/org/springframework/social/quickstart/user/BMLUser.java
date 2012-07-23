package org.springframework.social.quickstart.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.openidconnect.api.PayPalProfile;

public class BMLUser extends User {

	private static final long serialVersionUID = 1L;

	private final PayPalProfile payPalUser;

	/**
	 * @param username
	 * @param authorities
	 */
	public BMLUser(final PayPalProfile user, final Collection<? extends GrantedAuthority> authorities) {
		super(user.getGiven_name(), user.getPassword(), true, true, true, true, authorities == null ? null : authorities);
		this.payPalUser = user;
	}

	public String getFullName() {
		return payPalUser.getName();
	}

	public PayPalProfile getPayPalUser() {
		return payPalUser;
	}

}