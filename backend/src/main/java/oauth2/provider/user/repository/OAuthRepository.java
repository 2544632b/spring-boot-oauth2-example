package oauth2.provider.user.repository;

import oauth2.provider.model.user.info.entity.OAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRepository extends JpaRepository<OAuthEntity, Long> {
    @Query(value = "select t from OAuthEntity t where t.clientId = :clientId")
    OAuthEntity findByClientId(String clientId);
}
