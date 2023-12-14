package oauth2.provider.service.oauth.server.entity;

import oauth2.provider.model.user.info.entity.OAuthEntity;

public interface OAuthEntityService {
    OAuthEntity findByClientId(String clientId);
}
