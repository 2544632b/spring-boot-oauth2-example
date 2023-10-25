package oauth2.provider.model.form.request.user.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

public class RegisterForm implements Serializable {
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "请输入正确的电子邮件。")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9_]{5,20}$", message = "用户名应当在5-20个字符内。")
    private String username;

    @NotBlank
    @Pattern(regexp = "^.{10,20}$", message = "密码应当在10-20个字符内。")
    private String password;

    @NotBlank
    @Pattern(regexp = "^.{10,20}$", message = "重复密码应当在10-20个字符内且需要与首次输入相符。")
    private String repeatPassword;

    public String getEmail() {
        return this.email;
    }
    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
    public String getRepeatPassword() {
        return this.repeatPassword;
    }
}
