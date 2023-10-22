package oauth2.provider.v2.model.user.info.oauth.server;

public class OAuthSessionInfo {
    private String clientId;
    private String scope;
    private String code;
    private String accessToken;
    private long expire;
    private String username;

    public OAuthSessionInfo(String clientId, String scope, String code, long expire, String accessToken, String username) {
        this.clientId = clientId;
        this.scope = scope;
        this.code = code;
        this.expire = expire;
        this.accessToken = accessToken;
        this.username = username;
    }

    public String getClientId() {
        return clientId;
    }

    public String getCode() {
        return code;
    }

    public void removeCode() {
        code = "";
    }

    public String getScope() {
        return scope;
    }

    public long getExpire() {
        return expire;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUsername() {
        return username;
    }
}
