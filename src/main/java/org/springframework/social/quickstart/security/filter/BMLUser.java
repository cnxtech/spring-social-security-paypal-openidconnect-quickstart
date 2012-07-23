package org.springframework.social.quickstart.security.filter;

import java.util.Arrays;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.openidconnect.api.PayPalProfile;

/**
 * Wrapper to hold {@code PayPalProfile} while extending Spring Security User.
 * 
 * @author abprabhakar@paypal.com
 * 
 */
public class BMLUser extends User {

	private static final long serialVersionUID = 1L;

	public static final String NOT_APPLICABLE = "N/A";

	private final PayPalProfile payPalUser;

	/**
	 * @param username
	 * @param authorities
	 */
	public BMLUser(final PayPalProfile user, final GrantedAuthority... authorities) {
		super(user.getUser_id(), user.getPassword(), true, true, true, true, authorities == null ? null : Arrays.asList(authorities));
		this.payPalUser = user;
	}

	public String getFullName() {
		return payPalUser.getName();
	}

	public PayPalProfile getPayPalUser() {
		return payPalUser;
	}

}
