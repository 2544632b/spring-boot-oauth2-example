package oauth2.provider.v2.model.user.info.oauth.client;

import java.io.Serializable;

// Response for backend not for user.
public class GithubUserProfileResp implements Serializable {
    private String email;
    private String login;

    public String getEmail() {
        return email;
    }
    public String getLogin() {
        return login;
    }
}
