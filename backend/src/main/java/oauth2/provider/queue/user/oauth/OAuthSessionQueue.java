package oauth2.provider.queue.user.oauth;

import oauth2.provider.model.user.info.oauth.server.OAuthSessionInfo;

public interface OAuthSessionQueue {
    boolean insert(OAuthSessionInfo OAuthSessionInfo);
    OAuthSessionInfo find(String clientId, String code);
    OAuthSessionInfo find(String bearer);
    void deleteByUsername(String username);
    void deleteByBearer(String bearer);
}
