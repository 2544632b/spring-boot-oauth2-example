package oauth2.provider.controller.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import oauth2.provider.annotation.request.RequestLimit;
import oauth2.provider.service.authentication.UserAuthenticateService;
import oauth2.provider.model.form.request.user.login.LoginFormPreview;
import oauth2.provider.model.form.response.login.LoginResp;
import oauth2.provider.service.authentication.PostLoginService;
import oauth2.provider.util.date.DateUtil;
import oauth2.provider.util.jwt.JSONWebToken;
import oauth2.provider.util.aes.AESUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RestController
public class Login {

    @Resource
    private UserAuthenticateService UserAuthenticateService;

    @Resource
    private PostLoginService LoginSuccessHandler;

    @Resource
    private PostLoginService LoginFailHandler;

    @Resource
    private JSONWebToken JSONWebToken;

    @Resource
    private AESUtil AESUtil;

    @Resource
    private DateUtil DateUtil;

    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @RequestLimit
    public Object doLogin(@Validated @RequestBody LoginFormPreview LoginForm, HttpServletRequest request) throws Exception {
        try {
            UserAuthenticateService.attemptLogin(LoginForm.keywords(), LoginForm.password());
            var user = UserAuthenticateService.getLoginUserEntity(LoginForm.keywords());

            this.UserAuthenticateService.updateLastLoginIp(LoginForm.keywords(), request.getRemoteAddr());
            Date date = new Date(System.currentTimeMillis());
            String date2 = DateUtil.format(date);

            var loginResp = LoginResp.make(user.getEmail(), user.getUsername(), user.getUserTotp(), request.getRemoteAddr(), date2);

            var SessionInfo = new HashMap<>();
            SessionInfo.put("SessionID", AESUtil.encrypt(JSONWebToken.generateToken(new ObjectMapper().writeValueAsString(loginResp))));

            LoginSuccessHandler.handle(LoginForm.keywords(), LoginForm.password(), request.getRemoteAddr(), date2);

            return SessionInfo;
        } catch(Exception e) {
            Date date = new Date(System.currentTimeMillis());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date2 = dateFormat.format(date);

            LoginFailHandler.handle(LoginForm.keywords(), LoginForm.password(), request.getRemoteAddr(), date2);

            throw new BadCredentialsException(e.getMessage());
        }
    }
}
