package oauth2.provider.v2.service.base.oauth;

import oauth2.provider.v2.model.user.info.entity.OAuthEntity;

public interface OAuthEntityService {
    OAuthEntity findByClientId(String clientId);
}
