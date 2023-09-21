package oauth2.provider.v2.user.repository;

import oauth2.provider.v2.model.user.info.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select t from UserEntity t where t.username = :keywords or t.email = :keywords")
    UserEntity findByKeywords(@Param("keywords") String keywords);

    @Query(value = "select t from UserEntity t where t.githubId = :id")
    UserEntity findByGithubId(@Param("id") BigInteger id);

    @Query(value = "select t from UserEntity t where t.username = :username")
    UserEntity findByUsername(@Param("username") String username);

    @Query(value = "select t from UserEntity t where t.email = :email")
    UserEntity findByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "update UserEntity t set t.password = :password where t.username = :keywords or t.email = :keywords")
    void updatePassword(@Param("keywords") String keywords, @Param("password") String password);

    @Modifying
    @Query(value = "update UserEntity t set t.lastLoginIp = :ip where t.username = :keywords or t.email = :keywords")
    void updateLoginIp(@Param("keywords") String keywords, @Param("ip") String ip);
}
