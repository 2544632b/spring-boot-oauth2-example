package oauth2.provider.service.verify;

import oauth2.provider.queue.user.forgot.ForgotVerifyCodeQueue;
import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.service.email.EmailSenderService;
import oauth2.provider.service.profile.user.change.ChangeUserDetailsService;
import oauth2.provider.util.random.RandomPassword;
import oauth2.provider.util.random.RandomVerifyCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("ForgotUserInfoVerifyCodeService")
public class ForgotUserInfoVerifyCodeServiceImpl implements VerifyCodeService {

    @Resource
    private ForgotVerifyCodeQueue queue;

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

        if(queue.insert(user, code)) {
            EmailSenderService.send(email, EmailSenderService.applicationName, "Your verification code is " + code + " .");
        }
    }

    @Override
    public boolean verifyUser(String email, int code) {
        UserEntity result = queue.find(email, code);
        if(result == null) {
            return false;
        }
        String newPassword = RandomPassword.getPassword();
        ChangeUserDetailsService.changePassword(result.getUsername(), newPassword); // Authenticate skip
        EmailSenderService.send(email, EmailSenderService.applicationName, "Your user password is: " + newPassword + " .");
        return true;
    }
}
