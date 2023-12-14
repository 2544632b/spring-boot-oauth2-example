package oauth2.provider.model.form.request.verify;

import jakarta.validation.constraints.Pattern;

public class VerifyCodeInfoForm {

    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "Invalid email")
    private String email;

    private int code;

    public String getEmail() {
        return email;
    }

    public int getCode() {
        return code;
    }
}
