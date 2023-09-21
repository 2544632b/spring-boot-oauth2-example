package oauth2.provider.v2.model.user.info.oauth.client;

import java.io.Serializable;
import java.math.BigInteger;

// Response for backend not for user.
public class GithubUserProfileResp implements Serializable {

    private String email;

    private BigInteger id;

    private String login;

    private String createAt;


    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public BigInteger getId() {
        return id;
    }

    public String getCreateAt() {
        return createAt;
    }
}
