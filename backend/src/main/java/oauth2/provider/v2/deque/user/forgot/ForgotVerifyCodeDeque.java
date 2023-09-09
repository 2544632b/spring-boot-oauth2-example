package oauth2.provider.v2.deque.user.forgot;

import oauth2.provider.v2.model.user.info.entity.UserEntity;

public interface ForgotVerifyCodeDeque {
    boolean insert(UserEntity UserEntity, int code);
    UserEntity find(String email, int code);
}
