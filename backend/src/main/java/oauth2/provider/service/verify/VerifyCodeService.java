package oauth2.provider.service.verify;

import oauth2.provider.model.user.info.entity.UserEntity;

public interface VerifyCodeService {
    public void addVerifyCode(UserEntity user);
    public boolean verifyUser(String email, int code);
}
