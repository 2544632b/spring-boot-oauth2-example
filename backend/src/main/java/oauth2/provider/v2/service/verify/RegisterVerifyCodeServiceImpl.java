package oauth2.provider.v2.service.verify;

import oauth2.provider.v2.deque.user.register.RegisterVerifyCodeDeque;
import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.service.base.user.UserEntityService;
import oauth2.provider.v2.util.RandomVerifyCode;
import oauth2.provider.v2.util.checker.EmailStringChecker;
import oauth2.provider.v2.service.base.email.EmailSenderService;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service("RegisterVerifyCodeService")
public class RegisterVerifyCodeServiceImpl implements VerifyCodeService {

    @Resource
    private RegisterVerifyCodeDeque deque;

    @Resource
    private UserEntityService userEntity;

    @Resource
    private RandomVerifyCode RandomVerifyCode;

    @Resource
    private EmailStringChecker EmailStringChecker;

    @Resource
    private EmailSenderService EmailSenderService;

    @Override
    public void addVerifyCode(UserEntity user) {
        int code = RandomVerifyCode.getVerifyCode();
        if(deque.insert(user, code)) {
            EmailSenderService.send(user.getEmail(), EmailSenderService.applicationName,"Your verification code is: " + code + " .");
        }
    }

    @Override
    public boolean verifyUser(String email, int code) {
        EmailStringChecker.check(email);

        UserEntity targetUserEntity = deque.find(email, code);
        if(targetUserEntity != null) {
            userEntity.insertUser(targetUserEntity);

            EmailSenderService.send(StringEscapeUtils.escapeJava(email), EmailSenderService.applicationName, "Your account was verified.");
            return true;
        }

        return false;
    }
}
