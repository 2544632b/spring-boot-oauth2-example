package oauth2.provider.model.form.request.user.forgot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

public class ForgotPasswordForm implements Serializable {

    @NotBlank
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "Invalid email")
    private String email;

    public String getEmail() {
        return email;
    }

}
