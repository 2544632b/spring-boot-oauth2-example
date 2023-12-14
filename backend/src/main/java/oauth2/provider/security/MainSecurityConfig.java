package oauth2.provider.security;

import oauth2.provider.security.filter.BearerAuthenticationFilter;
import oauth2.provider.security.filter.DefaultAuthenticationFilter;
import oauth2.provider.service.authentication.ProviderUserDetailsService;
import oauth2.provider.util.functional.oidc.ServerConfiguration;
import oauth2.provider.util.functional.oidc.jwk.JWKConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MainSecurityConfig {

    @Bean
    public DefaultAuthenticationFilter onceAuthenticationFilter() {
        return new DefaultAuthenticationFilter();
    }

    @Bean
    public BearerAuthenticationFilter bearerAuthenticationFilter() {
        return new BearerAuthenticationFilter();
    }

    @Bean
    public ProviderUserDetailsService providerUserDetailsService() {
        return new ProviderUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    */

    @Bean(name = "jwkConfiguration")
    public JWKConfiguration jwkConfiguration() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new JWKConfiguration()
                .setupRSA()
                .setAlg()
                .setPublicKey()
                .setPrivateKey()
                .setSig();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(providerUserDetailsService());

        return new ProviderManager(List.of(daoAuthenticationProvider));
    }

    // .csrf().disable() was deprecated since 6.1
    @Order(1)
    @Bean
    public SecurityFilterChain oauthSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(bearerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.cors(Customizer.withDefaults());
        return http.build();
    }

    // .csrf().disable() was deprecated since 6.1
    @Order(2)
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(onceAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public ServerConfiguration serverConfiguration() {
        return new ServerConfiguration()
                .setIssuer("http://localhost:8000/")
                .setJwk("http://localhost:8000/openid/jwks")
                .setAuthorizationEndpoint("http://localhost/authorize/confirm")
                .setTokenEndpoint("http://localhost:8000/oauth/token")
                .setUserInfoEndpoint("http://localhost:8000/default/user/info")
                .setScopesSupported(List.of("openid", "profile"))
                .setClaimsSupported(List.of("sub", "username", "email"))
                .setAuthorizationGrant()
                .setCodeResponseType()
                .setResponseFormPostMode()
                .setTokenClientSecretPost()
                .setIdTokenRS256AlgSupported()
                .setSubjectPublic()
                .setPlainCodeChallenge();
    }
}
