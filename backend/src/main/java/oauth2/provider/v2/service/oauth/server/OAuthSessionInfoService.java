package oauth2.provider.v2.service.oauth.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.model.form.response.oauth.OAuthAuthorizeTokenResp;

public interface OAuthSessionInfoService {
    String generateCode(String clientId, String scope, String username);
    OAuthAuthorizeTokenResp generateAccessToken(String clientId, String code) throws JsonProcessingException;
    OAuthAuthorizeTokenResp generateIdToken(String clientId, String code) throws JsonProcessingException;
    UserEntity getUserEntityFromBearer(String bearer);
    void deleteOAuthCodeInfo(String username);
    String getScope(String bearer);
}
