package oauth2.provider.model.form.request.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class OAuthAuthorizeForm implements Serializable {

    @NotBlank
    @JsonProperty("response_type")
    private String responseType;

    @NotBlank
    @JsonProperty("client_id")
    private String clientId;

    @NotBlank
    private String scope;

    @NotBlank
    private String state;

    @NotBlank
    @JsonProperty("redirect_uri")
    private String redirectURI;

    public String getResponseType() {
        return responseType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getScope() {
        return scope;
    }

    public String getState() {
        return state;
    }

    public String getRedirectURI() {
        return redirectURI;
    }
}
