package oauth2.provider.v2.service.oauth.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.model.form.response.oauth.OAuthAuthorizeTokenResp;
import oauth2.provider.v2.model.user.info.oauth.server.OAuthSessionInfo;

import java.util.List;

public interface OAuthSessionInfoService {
    String generateCode(String clientId, String scope, String username);
    OAuthAuthorizeTokenResp generateAccessToken(String clientId, String code);
    UserEntity getUserEntityFromBearer(String bearer);
    void deleteOAuthSessionInfoByUsername(String username);
    void deleteOAuthSessionInfoByBearer(String bearer);
    String getScope(String bearer);
}
