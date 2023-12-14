package oauth2.provider.service.authentication;

import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.service.profile.user.entity.UserEntityService;
import oauth2.provider.util.checker.EmailStringChecker;
import oauth2.provider.util.checker.UsernameStringChecker;
import oauth2.provider.util.checker.PasswordStringChecker;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class UserAuthenticateServiceImpl implements UserAuthenticateService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private EmailStringChecker EmailStringChecker;

    @Resource
    private UsernameStringChecker UsernameStringChecker;

    @Resource
    private PasswordStringChecker PasswordStringChecker;

    @Resource
    private UserEntityService userEntity;

    @Override
    public boolean attemptLogin(String keywords, String password) {
        if(!PasswordStringChecker.check(password)) {
            return false;
        }

        boolean isEmail = EmailStringChecker.check(keywords);
        if(!isEmail) {
            if(!UsernameStringChecker.check(keywords)) {
                return false;
            }
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        StringEscapeUtils.escapeJava(keywords),
                        StringEscapeUtils.escapeJava(password)
                );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return !ObjectUtils.isEmpty(authentication);
    }

    @Override
    public UserEntity getLoginUserEntity(String keywords) {
        return userEntity.findByKeywords(StringEscapeUtils.escapeJava(keywords));
    }

    @Override
    public void updateLastLoginIp(String keywords, String ip) {
        userEntity.updateLastLoginIp(keywords, ip);
    }
}
