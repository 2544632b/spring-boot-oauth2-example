package oauth2.provider.model.form.request.user.change;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

public class PasswordChangeForm implements Serializable {
    @NotBlank
    @Pattern(regexp = "^.{10,20}$")
    private String password;
    @NotBlank
    @Pattern(regexp = "^.{10,20}$")
    private String newPassword;
    public String getPassword() {
        return this.password;
    }
    public String getNewPassword() {
        return this.newPassword;
    }
}
