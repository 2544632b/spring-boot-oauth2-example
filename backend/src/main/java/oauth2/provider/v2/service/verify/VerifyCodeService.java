package oauth2.provider.v2.service.verify;

import oauth2.provider.v2.model.user.info.entity.UserEntity;

public interface VerifyCodeService {
    public void addVerifyCode(UserEntity user);
    public boolean verifyUser(String email, int code);
}
