package com.accenture.lkm.spring.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

// Authorize the security and refresh tokens
// enables the security end points and customizes them if required
// above 2 are achieved using AuthorizationServerEndpointsConfigurer

//Below 2 are achieved using ClientDetailsServiceConfigurer
// client registration and providing the client credentials: username: trusted-client, password:secret
// defines the authorization scopes of the client

//The server issuing access tokens to the client after successfully authenticating the resource owner and obtaining authorization.

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
	private static String REALM = "MY_OAUTH_REALM";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ClientDetailsService clientDetailsService;

	// Registers a client with client-id �trusted-client� and password �secret�
	// and roles & scope he is allowed for.
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("trusted-client")
				.secret("secret")
				.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
				.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
				.scopes("read", "write", "trust")
				

				// Access token is only valid for 2 minutes.
				.accessTokenValiditySeconds(120)

				// Refresh token is only valid for 10 minutes.
				.refreshTokenValiditySeconds(600);
	}

	// Configures the Auth endpoints/URLS and the userApprovalHandler
	// UserApprovalHandler controls approving or denying the access grant.
	// it is 2 types: ApprovalStoreUserApprovalHandler,TokenStoreUserApprovalHandler
	// configures the token store token store can be of three types JDBC, Inmemory
	// and JWT
	// here in our example it is inmemory TokenStore with
	// TokenStoreUserApprovalHandler
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()).userApprovalHandler(userApprovalHandler())
				.authenticationManager(authenticationManager);
	}

	public TokenStoreUserApprovalHandler userApprovalHandler() {
		TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
		handler.setTokenStore(tokenStore());
		handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
		handler.setClientDetailsService(clientDetailsService);
		return handler;
	}

	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.realm(REALM + "/client");
	}
}
