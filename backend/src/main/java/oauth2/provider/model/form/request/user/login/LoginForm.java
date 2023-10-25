package oauth2.provider.model.form.request.user.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

@Deprecated
public class LoginForm implements Serializable {
    @NotBlank
    @Pattern(regexp = "^.{5,20}$", message = "用户名或电子邮件不能少于5个字符。")
    private String keywords;
    @NotBlank
    @Pattern(regexp = "^.{5,20}$", message = "密码不能少于5个字符。")
    private String password;

    public String getKeywords() {
        return this.keywords;
    }
    public String getPassword() {
        return this.password;
    }
}
