package oauth2.provider.v2.deque.user.register;

import oauth2.provider.v2.model.user.info.entity.UserEntity;

public interface RegisterVerifyCodeDeque {
    boolean insert(UserEntity UserEntity, int code);
    UserEntity find(String email, int code);
}
