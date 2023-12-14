package oauth2.provider.model.form.response.login;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize
public class LoginResp implements Serializable {

    public String email;

    public String username;

    public String totp;

    public String ip;

    public String login;

    @JsonCreator
    public LoginResp(String email, String username, String totp, String ip, String login) {
        this.email = email;
        this.username = username;
        this.totp = totp;
        this.ip = ip;
        this.login = login;
    }

    public static LoginResp make(String email, String username, String totp, String ip, String login) {
        return new LoginResp(email, username, totp, ip, login);
    }
}
