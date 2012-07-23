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
package org.springframework.social.quickstart.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.openidconnect.PayPalConnectionFactory;
import org.springframework.social.quickstart.user.SimpleConnectionSignUp;

/**
 * Spring Social Configuration.
 * 
 * @author Keith Donald, adapted to PayPal by Felipe Albertao
 */
@Configuration
public class SocialConfig {

	@Value("${paypal.appId}")
	private String paypalAppId;

	@Value("${paypal.appSecret}")
	private String paypalAppSecret;

	@Inject
	private DataSource dataSource;

	/**
	 * When a new provider is added to the app, register its {@link ConnectionFactory} here.
	 * 
	 * @see PayPalConnectionFactory
	 */
	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new PayPalConnectionFactory(paypalAppId, paypalAppSecret));
		return registry;
	}

	/**
	 * Singleton data access object providing access to connections across all users.
	 */
	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), Encryptors.noOpText());
		repository.setConnectionSignUp(new SimpleConnectionSignUp());
		return repository;
	}

}