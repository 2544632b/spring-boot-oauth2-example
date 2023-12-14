package oauth2.provider.controller.oauth;

import oauth2.provider.annotation.controller.OAuthController;
import oauth2.provider.model.user.info.entity.OAuthEntity;
import oauth2.provider.service.oauth.server.entity.OAuthEntityService;
import oauth2.provider.service.oauth.server.OAuthSessionInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@OAuthController
public class OAuthAuthorizeToken {

    @Resource
    private OAuthSessionInfoService OAuthSessionInfoService;

    @Resource
    private OAuthEntityService oauthEntity;

    @Resource
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    public Object getToken(
            @RequestParam("code") String code,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String secret,
            @RequestParam("grant_type") String grantType
    ) {
        OAuthEntity oauthInfo = oauthEntity.findByClientId(StringEscapeUtils.escapeJava(clientId));

        String[] scopes = oauthInfo.getScope().split(",");
        for(String s : scopes) {
            if(s.equals("openid")) {
                if(passwordEncoder.matches(secret, oauthInfo.getClientSecret()) && grantType.equals("authorization_code")) {
                    return OAuthSessionInfoService.generateIdToken(clientId, code);
                }
            }
        }

        if(passwordEncoder.matches(secret, oauthInfo.getClientSecret())
                && grantType.equals("authorization_code")) {
            return OAuthSessionInfoService.generateAccessToken(clientId, code);
        }

        throw new NullPointerException("Mismatch info");
    }
}
