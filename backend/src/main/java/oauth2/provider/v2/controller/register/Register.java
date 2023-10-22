package oauth2.provider.v2.controller.register;

import jakarta.servlet.http.HttpServletRequest;
import oauth2.provider.v2.annotation.request.RequestLimit;
import oauth2.provider.v2.model.form.request.user.register.RegisterForm;
import oauth2.provider.v2.model.form.response.Response;
import oauth2.provider.v2.model.form.request.verify.VerifyCodeInfoForm;
import jakarta.annotation.Resource;
import oauth2.provider.v2.service.register.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Register {

    @Resource
    private RegisterService RegisterService;

    private static final Logger logger = LoggerFactory.getLogger(Register.class);

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @RequestLimit
    public Object doRegister(@Validated @RequestBody RegisterForm RegisterForm, HttpServletRequest request) throws BadCredentialsException {
        if (RegisterForm.getPassword().equals(RegisterForm.getRepeatPassword())) {
            if(RegisterService.doRegister(RegisterForm.getEmail(), RegisterForm.getUsername(), RegisterForm.getPassword(), request.getRemoteAddr())) {
                return Response.response("Finish, please check your email");
            }
        }
        throw new BadCredentialsException("Invalid email or username");
    }

    @RequestMapping(value = "/register/verify", method = RequestMethod.POST)
    @RequestLimit
    public Object doVerify(@RequestBody VerifyCodeInfoForm VerifyCodeInfoForm) throws BadCredentialsException {
        if(RegisterService.finalRegister(VerifyCodeInfoForm.getEmail(), VerifyCodeInfoForm.getCode())) {
            return Response.response("Validated");
        }
        throw new BadCredentialsException("Invalid email or verification code");
    }
}