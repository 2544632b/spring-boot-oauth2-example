package oauth2.provider.model.form.request.user.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

public class RegisterForm implements Serializable {
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "Email is not valid.")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9_]{5,20}$", message = "Username should have 5-20 characters.")
    private String username;

    @NotBlank
    @Pattern(regexp = "^.{10,20}$", message = "Password should have 10-20 characters.")
    private String password;

    @NotBlank
    @JsonProperty("repeat_password")
    @Pattern(regexp = "^.{10,20}$", message = "Repeat password should have 10-20 characters.")
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
