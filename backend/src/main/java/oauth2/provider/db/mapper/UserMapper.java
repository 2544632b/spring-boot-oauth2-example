package oauth2.provider.db.mapper;

import oauth2.provider.model.user.info.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;

// https://retheviper.github.io/posts/spring-data-jpa/

@Mapper
@Repository
public interface UserMapper {

    UserEntity findByKeywords(String keywords);

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    UserEntity findByGithubId(BigInteger id);

    void updatePassword(String keywords, String password);

    void updateLoginIp(String keywords, String ip);

    void save(UserEntity user);

}
