package oauth2.provider.v2.user.mapper;

import oauth2.provider.v2.model.user.info.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

// It's surprises for me about the same level scan with the bean scan
// https://retheviper.github.io/posts/spring-data-jpa/
// It's none different with `JDBC Template`

// PHP E.G.:
// $result = DataSheet()->find->columnName("username")->where()->columnName("abc")->or->anotherColumnName("def");
// $result = Db::tableName("users")->select()->all();
// return $result;

//
// :(
//

@Mapper
@Repository
public interface UserMapper {
    UserEntity findByKeywords(String keywords);
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    void updatePassword(String keywords, String password);
    void updateLoginIp(String keywords, String ip);
    void insertUser(UserEntity user);
}
