package oauth2.provider.queue.user.forgot;

import oauth2.provider.model.user.info.entity.UserEntity;

public interface ForgotVerifyCodeQueue {
    boolean insert(UserEntity UserEntity, int code);
    UserEntity find(String email, int code);
}
