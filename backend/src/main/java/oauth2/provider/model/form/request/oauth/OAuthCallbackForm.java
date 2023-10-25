package oauth2.provider.model.form.request.oauth;

import java.io.Serializable;

public class OAuthCallbackForm implements Serializable {
    private String state;
    private String code;

    public String getState() {
        return state;
    }
    public String getCode() {
        return code;
    }
}
