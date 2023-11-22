package oauth2.provider.controller.user;

import oauth2.provider.annotation.request.RequestLimit;
import oauth2.provider.model.form.request.user.forgot.ForgotPasswordForm;
import oauth2.provider.model.form.response.Response;
import oauth2.provider.model.form.request.verify.VerifyCodeInfoForm;
import oauth2.provider.service.profile.user.forgot.ForgotUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserForgot {
    @Resource
    private ForgotUserInfoService ForgotUserInfoService;

    @RequestMapping(value = "/user/forgot/password/email", method = RequestMethod.POST)
    @RequestLimit
    public Object resetPasswordEmail(@Validated @RequestBody ForgotPasswordForm ForgotPasswordForm) throws UsernameNotFoundException {
        if(!ForgotPasswordForm.getEmail().isEmpty()) {
            ForgotUserInfoService.addVerifyCodeFromEmail(ForgotPasswordForm.getEmail());
            return Response.response("Please look up your email");
        }
        throw new UsernameNotFoundException("Invalid email");
    }

    @RequestMapping(value = "/user/forgot/password/email/verify", method = RequestMethod.POST)
    @RequestLimit
    public Object verifyPasswordEmail(@Validated @RequestBody VerifyCodeInfoForm VerifyCodeInfoForm) {
        if(ForgotUserInfoService.checkVerifyCodeFromEmail(VerifyCodeInfoForm.getEmail(), VerifyCodeInfoForm.getCode())) {
            return Response.response("Reset success");
        }
        throw new BadCredentialsException("Invalid email or verification code");
    }
}
