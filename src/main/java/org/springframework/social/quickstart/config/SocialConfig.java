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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.*;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.openidconnect.PayPalConnectionFactory;
import org.springframework.social.openidconnect.PayPalConnectionFactoryBuilder;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.jdbc.OpenIdJdbcUsersConnectionRepository;
import org.springframework.social.quickstart.user.SecurityContext;
import org.springframework.social.quickstart.user.SimpleConnectionSignUp;
import org.springframework.social.quickstart.user.SimpleSignInAdapter;
import org.springframework.social.quickstart.user.User;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Spring Social Configuration.
 * 
 * @author Keith Donald, adapted to PayPal by Felipe Albertao
 */
@Configuration
public class SocialConfig {

	@Autowired
    Environment environment;


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
        //Builder to create paypal connection factory.  This was created to aid injecting different set of URLs than production.
        //Useful if you are hosting paypal access in other environments.  There are couple of other options also such as turning
        //off hostname verifier and disabling login variant "not you" page.
        ConnectionFactory<PayPal> connectionFactory = new PayPalConnectionFactoryBuilder().withAppId(environment.getProperty("paypal.appid"))
                .withAppSecret(environment.getProperty("paypal.appsecret")).withScope(environment.getProperty("paypal.scope")).build();
		registry.addConnectionFactory(connectionFactory);
		return registry;
	}

    /**
     * Singleton data access object providing access to connections across all users.
     */
    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        //InMemoryUsersConnectionRepository repository = new InMemoryUsersConnectionRepository(connectionFactoryLocator());
        OpenIdJdbcUsersConnectionRepository repository = new OpenIdJdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator(), Encryptors.noOpText());
        repository.setConnectionSignUp(new SimpleConnectionSignUp());
        return repository;
    }

	/**
	 * Request-scoped data access object providing access to the current user's connections.
	 */
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
		User user = SecurityContext.getCurrentUser();
		return usersConnectionRepository().createConnectionRepository(user.getId());
	}

	/**
	 * A proxy to a request-scoped object representing the current user's primary PayPal account.
	 * 
	 * @throws NotConnectedException
	 *             if the user is not connected to paypal.
	 */
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public PayPal paypal() {
		return connectionRepository().getPrimaryConnection(PayPal.class).getApi();
	}

    /**
     * The Spring MVC Controller that allows users to sign-in with their provider accounts.
     */
    @Bean
    public ProviderSignInController providerSignInController() {
        return new ProviderSignInController(connectionFactoryLocator(), usersConnectionRepository(),
                new SimpleSignInAdapter());
    }

}