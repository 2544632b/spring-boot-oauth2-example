package oauth2.provider.v2.model.user.info.oauth.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

// Response for backend not for user.
public class GithubTokenResp implements Serializable {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getScope() {
        return scope;
    }
}
