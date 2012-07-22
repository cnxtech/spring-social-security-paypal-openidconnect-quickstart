package org.springframework.social.quickstart.security.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.openidconnect.api.PayPalProfile;

public class SecurityUtil {

	public static Authentication signInUser(PayPalProfile profile) {
		BMLUser user = new BMLUser(profile, new SimpleGrantedAuthority("ROLE_USER"));
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

}
