package oauth2.provider.v2.service.verify;

import oauth2.provider.v2.deque.user.forgot.ForgotVerifyCodeDeque;
import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.service.base.email.EmailSenderService;
import oauth2.provider.v2.service.profile.user.change.ChangeUserDetailsService;
import oauth2.provider.v2.util.random.RandomPassword;
import oauth2.provider.v2.util.random.RandomVerifyCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("ForgotUserInfoVerifyCodeService")
public class ForgotUserInfoVerifyCodeServiceImpl implements VerifyCodeService {

    @Resource
    private ForgotVerifyCodeDeque deque;

    @Resource
    private RandomVerifyCode RandomVerifyCode;

    @Resource
    private RandomPassword RandomPassword;

    @Resource
    private EmailSenderService EmailSenderService;

    @Resource
    private ChangeUserDetailsService ChangeUserDetailsService;

    @Override
    public void addVerifyCode(UserEntity user) {
        String email = user.getEmail();
        int code = RandomVerifyCode.getVerifyCode();
        if(deque.insert(user, code)) {
            EmailSenderService.send(email, EmailSenderService.applicationName, "Your verification code is " + code + " .");
        }
    }

    @Override
    public boolean verifyUser(String email, int code) {
        UserEntity result = deque.find(email, code);
        if(result == null) {
            return false;
        }
        String newPassword = RandomPassword.getPassword();
        ChangeUserDetailsService.changePassword(result.getUsername(), newPassword); // Authenticate skip
        EmailSenderService.send(email, EmailSenderService.applicationName, "Your user password is: " + newPassword + " .");
        return true;
    }
}
