package oauth2.provider.v2.controller.oauth;

import jakarta.servlet.http.HttpServletRequest;
import oauth2.provider.v2.annotation.controller.OAuthController;
import oauth2.provider.v2.model.form.request.oauth.OAuthCallbackForm;
import oauth2.provider.v2.service.oauth.client.GithubOAuthService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@OAuthController
public class OAuthClientAuthorizeToken {

    @Resource
    private GithubOAuthService GithubOAuthService;

    @RequestMapping(value = "/oauth/client/continue/github", method = RequestMethod.POST)
    public Object getTokenFromProvider(@RequestBody OAuthCallbackForm OAuthCallbackForm, HttpServletRequest request) throws Exception {
        return GithubOAuthService.getUserInfo(OAuthCallbackForm.getState(), OAuthCallbackForm.getCode(), request.getRemoteAddr());
    }

    // ......
}
