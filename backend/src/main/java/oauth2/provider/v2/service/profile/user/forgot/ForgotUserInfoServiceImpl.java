package oauth2.provider.v2.service.profile.user.forgot;

import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.service.base.user.UserEntityService;
import oauth2.provider.v2.service.verify.VerifyCodeService;
import oauth2.provider.v2.util.checker.EmailStringChecker;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service
public class ForgotUserInfoServiceImpl implements ForgotUserInfoService {

    @Resource
    private EmailStringChecker EmailStringChecker;
    @Resource
    private UserEntityService userEntity;
    @Resource
    private VerifyCodeService ForgotUserInfoVerifyCodeService;

    @Override
    public void addVerifyCodeFromEmail(String email) {
        if(!EmailStringChecker.check(StringEscapeUtils.escapeJava(email))) {
            return;
        }
        UserEntity userInfo = userEntity.findByEmail(StringEscapeUtils.escapeJava(email));
        ForgotUserInfoVerifyCodeService.addVerifyCode(userInfo);
    }

    @Override
    public boolean checkVerifyCodeFromEmail(String email, int code) {
        if(!EmailStringChecker.check(StringEscapeUtils.escapeJava(email))) {
            return false;
        }
        return ForgotUserInfoVerifyCodeService.verifyUser(StringEscapeUtils.escapeJava(email), code);
    }
}
