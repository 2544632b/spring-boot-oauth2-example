package oauth2.provider.v2.deque.user.oauth;

import oauth2.provider.v2.model.user.info.oauth.server.OAuthSessionInfo;

import java.util.List;

public interface OAuthSessionDeque {
    boolean insert(OAuthSessionInfo OAuthSessionInfo);
    OAuthSessionInfo find(String clientId, String code);
    OAuthSessionInfo find(String bearer);
    void deleteByUsername(String username);
    void deleteByBearer(String bearer);
}
