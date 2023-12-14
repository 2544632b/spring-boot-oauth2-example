package oauth2.provider.service.authentication;

import jakarta.annotation.Resource;
import oauth2.provider.service.profile.user.entity.UserEntityService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;

@Service
public class ProviderUserDetailsService implements UserDetailsService {

    @Resource
    private UserEntityService userEntity;

    @Override
    public UserDetails loadUserByUsername(@NotNull String username) throws BadCredentialsException {
        UserDetails ud = userEntity.findByKeywords(username);
        if(ud == null) {
            throw new BadCredentialsException("");
        }
        return ud;
    }
}