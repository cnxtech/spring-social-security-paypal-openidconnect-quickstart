package org.springframework.social.quickstart.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.openidconnect.api.PayPalProfile;
import org.springframework.social.quickstart.filter.SpringSocialSecurityAuthenticationFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

@Repository
@Service
@Transactional(readOnly = true)
@Qualifier("springSocialSecurityUserDetailsService")
public class PayPalUserDetailsService implements UserDetailsService {

	@Autowired
	private UsersConnectionRepository usersConnectionRepository;

	@Autowired
	private SpringSocialSecurityAuthenticationFactory authenticationFactory;

	@Autowired
	private ConnectionRepositorySignInService signInService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(userName);
		PayPalProfile payPalProfile = signInService.getUserProfile(userName);
		List<Connection<?>> allConnections = getConnections(connectionRepository, userName);
		if (allConnections.size() > 0) {

			Authentication authentication = authenticationFactory.createAuthenticationForAllConnections(userName, payPalProfile.getPassword(),
					allConnections);
			return new BMLUser(payPalProfile, authentication.getAuthorities());

		} else {
			throw new UsernameNotFoundException(userName);
		}

	}

	private List<Connection<?>> getConnections(ConnectionRepository connectionRepository, String userName) {
		MultiValueMap<String, Connection<?>> connections = connectionRepository.findAllConnections();
		List<Connection<?>> allConnections = new ArrayList<Connection<?>>();
		if (connections.size() > 0) {
			for (List<Connection<?>> connectionList : connections.values()) {
				for (Connection<?> connection : connectionList) {
					allConnections.add(connection);
				}
			}
		}
		return allConnections;
	}

	public void setUsersConnectionRepository(UsersConnectionRepository usersConnectionRepository) {
		this.usersConnectionRepository = usersConnectionRepository;
	}

}
