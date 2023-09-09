package oauth2.provider.v2.service.profile.user.forgot;

public interface ForgotUserInfoService {
    void addVerifyCodeFromEmail(String email);
    boolean checkVerifyCodeFromEmail(String email, int code);
}
