package oauth2.provider.model.form.request.user.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginFormPreview(
        @NotBlank
        @Pattern(regexp = "^.{5,20}$", message = "用户名或电子邮件不能少于5个字符。")
        String keywords,

        @NotBlank
        @Pattern(regexp = "^.{5,20}$", message = "密码不能少于5个字符。")
        String password
) {}
