package oauth2.provider.v2.model.form.request.oauth;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class OAuthAuthorizeForm implements Serializable {

    @NotBlank
    private String responseType;

    @NotBlank
    private String clientId;

    @NotBlank
    private String scope;

    @NotBlank
    private String state;

    @NotBlank
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
