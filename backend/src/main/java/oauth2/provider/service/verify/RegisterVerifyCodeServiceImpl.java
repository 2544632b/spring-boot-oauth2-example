package oauth2.provider.service.verify;

import oauth2.provider.queue.user.register.RegisterVerifyCodeQueue;
import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.service.base.user.UserEntityService;
import oauth2.provider.util.random.RandomVerifyCode;
import oauth2.provider.util.checker.EmailStringChecker;
import oauth2.provider.service.base.email.EmailSenderService;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service("RegisterVerifyCodeService")
public class RegisterVerifyCodeServiceImpl implements VerifyCodeService {

    @Resource
    private RegisterVerifyCodeQueue queue;

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
        if(queue.insert(user, code)) {
            EmailSenderService.send(user.getEmail(), EmailSenderService.applicationName,"Your verification code is: " + code + " .");
        }
    }

    @Override
    public boolean verifyUser(String email, int code) {
        EmailStringChecker.check(email);

        UserEntity targetUserEntity = queue.find(email, code);
        if(targetUserEntity != null) {
            userEntity.insertUser(targetUserEntity);

            EmailSenderService.send(StringEscapeUtils.escapeJava(email), EmailSenderService.applicationName, "Your account was verified.");
            return true;
        }

        return false;
    }
}
