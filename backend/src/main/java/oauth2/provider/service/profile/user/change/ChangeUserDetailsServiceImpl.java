package oauth2.provider.service.profile.user.change;

import oauth2.provider.service.base.user.UserEntityService;
import oauth2.provider.service.base.email.EmailSenderService;
import oauth2.provider.util.checker.PasswordStringChecker;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangeUserDetailsServiceImpl implements ChangeUserDetailsService {
    @Resource
    private UserEntityService userEntity;

    @Resource
    private PasswordStringChecker PasswordStringChecker;

    @Resource
    private EmailSenderService EmailSenderService;

    @Override
    // no changes for username.
    public boolean changeUsername(String username, String new_username) {
        return true;
    }

    @Override
    public boolean changePassword(String keywords, String new_password) {
        if(!PasswordStringChecker.check(new_password)) {
            return false;
        }

        EmailSenderService.send(userEntity.findByKeywords(keywords).getEmail(), EmailSenderService.applicationName, "Your password was changed with logged in after.");
        userEntity.updatePassword(keywords, new BCryptPasswordEncoder().encode(StringEscapeUtils.escapeJava(new_password)));
        return true;
    }
}