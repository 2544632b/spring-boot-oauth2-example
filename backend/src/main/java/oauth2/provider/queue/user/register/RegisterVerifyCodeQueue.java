package oauth2.provider.queue.user.register;

import oauth2.provider.model.user.info.entity.UserEntity;

public interface RegisterVerifyCodeQueue {
    boolean insert(UserEntity UserEntity, int code);
    UserEntity find(String email, int code);
}
