package oauth2.provider.model.form.request.user.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginFormPreview(
        @NotBlank
        @Pattern(regexp = "^.{5,20}$", message = "Username or email is not valid.")
        String keywords,
        @NotBlank
        @Pattern(regexp = "^.{5,20}$", message = "Password is not valid.")
        String password
) {}
