package oauth2.provider.service.profile.user.entity;

import oauth2.provider.model.user.info.entity.UserEntity;

import java.math.BigInteger;
import java.util.List;

public interface UserEntityService {
    void insertUser(UserEntity userInfo);
    void deleteUser(String id);
    void updatePassword(String keywords, String password);
    void updateLastLoginIp(String keywords, String ip);
    UserEntity findByKeywords(String keywords);
    UserEntity findByGithubId(BigInteger id);
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    List<UserEntity> findAll();
}
