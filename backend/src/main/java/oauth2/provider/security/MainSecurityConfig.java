package oauth2.provider.security;

import oauth2.provider.security.filter.BearerAuthenticationFilter;
import oauth2.provider.security.filter.DefaultAuthenticationFilter;
import oauth2.provider.service.base.user.ProviderUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
}