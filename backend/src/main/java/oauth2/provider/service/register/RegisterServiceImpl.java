package oauth2.provider.service.register;

import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.service.profile.user.entity.UserEntityService;
import oauth2.provider.service.verify.VerifyCodeService;
import oauth2.provider.service.email.EmailSenderService;
import oauth2.provider.util.checker.EmailStringChecker;
import oauth2.provider.util.checker.UsernameStringChecker;
import oauth2.provider.util.checker.PasswordStringChecker;
import oauth2.provider.util.totp.TOTPUtil;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private UserEntityService userEntity;

    @Resource
    private EmailSenderService EmailSenderService;

    @Resource
    private VerifyCodeService RegisterVerifyCodeService;

    @Resource
    private EmailStringChecker EmailStringChecker;

    @Resource
    private UsernameStringChecker UsernameStringChecker;

    @Resource
    private PasswordStringChecker PasswordStringChecker;

    @Resource
    private TOTPUtil TOTPUtil;

    public boolean doRegister(String email, String username, String password, String address) {
        if(!EmailStringChecker.check(email) || !UsernameStringChecker.check(username) || !PasswordStringChecker.check(password)) {
            return false;
        }

        if(password.length() < 6) {
            return false;
        }

        if(userEntity.findByKeywords(StringEscapeUtils.escapeJava(email)) != null || userEntity.findByKeywords(StringEscapeUtils.escapeJava(username)) != null) {
            return false;
        }

        RegisterVerifyCodeService.addVerifyCode(
                new UserEntity(
                        StringEscapeUtils.escapeJava(email),
                        StringEscapeUtils.escapeJava(username),
                        new BCryptPasswordEncoder().encode(StringEscapeUtils.escapeJava(password)),
                        address,
                        TOTPUtil.generateToken()
                )
        );

        return true;
    }

    @Override
    public boolean finalRegister(String email, int code) {
        if(!EmailStringChecker.check(email)) {
            return false;
        }
        return RegisterVerifyCodeService.verifyUser(StringEscapeUtils.escapeJava(email), code);
    }
}
