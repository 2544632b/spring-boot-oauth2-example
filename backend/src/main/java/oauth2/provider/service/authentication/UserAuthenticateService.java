package oauth2.provider.service.authentication;

import oauth2.provider.model.user.info.entity.UserEntity;

public interface UserAuthenticateService {
    boolean attemptLogin(String keywords, String password);
    UserEntity getLoginUserEntity(String keywords);
    void updateLastLoginIp(String keywords, String ip);
}
