package oauth2.provider.model.form.response.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.annotation.Nullable;

import java.io.Serializable;

@JsonSerialize
public class OAuthAuthorizeTokenResp implements Serializable {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("token_type")
    private String tokenType = "Bearer";

    @JsonProperty("expires_in")
    private long expiresIn = (120 * 1000);

    @JsonCreator
    public OAuthAuthorizeTokenResp(@Nullable String accessToken, @Nullable String idToken) {
        this.accessToken = accessToken;
        this.idToken = idToken;
    }
}
