package org.springframework.social.quickstart.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.api.PayPalProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConnectionRepositorySignInService {

	@Autowired
	private UsersConnectionRepository usersConnectionRepository;

	@Transactional(readOnly = true)
	public PayPalProfile getUserProfile(String userId) {
		ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(userId);
		List<Connection<PayPal>> connections = connectionRepository.findConnections(PayPal.class);
		if (connections.size() == 1) {
			return connections.get(0).getApi().getUserProfile();
		} else {
			throw new UsernameNotFoundException(userId);
		}
	}

}
