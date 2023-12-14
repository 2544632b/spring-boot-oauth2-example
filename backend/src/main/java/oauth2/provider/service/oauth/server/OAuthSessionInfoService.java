package oauth2.provider.service.oauth.server;

import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.model.form.response.oauth.OAuthAuthorizeTokenResp;

public interface OAuthSessionInfoService {
    String generateCode(String clientId, String scope, String username);
    OAuthAuthorizeTokenResp generateAccessToken(String clientId, String code);
    OAuthAuthorizeTokenResp generateIdToken(String clientId, String code);
    UserEntity getUserEntityFromBearer(String bearer);
    void deleteOAuthSessionInfoByUsername(String username);
    void deleteOAuthSessionInfoByBearer(String bearer);
    String getScope(String bearer);
}
