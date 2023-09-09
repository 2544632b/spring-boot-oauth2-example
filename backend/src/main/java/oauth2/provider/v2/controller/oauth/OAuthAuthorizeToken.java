package oauth2.provider.v2.controller.oauth;

import oauth2.provider.v2.annotation.controller.OAuthController;
import oauth2.provider.v2.model.user.info.entity.OAuthEntity;
import oauth2.provider.v2.service.base.oauth.OAuthEntityService;
import oauth2.provider.v2.service.oauth.server.OAuthSessionInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private BCryptPasswordEncoder bc;
    /***
     * OAuth2.0 / Open ID Connect 1.0 #2 (Token End point)
     * Publicly for third party web.
     *
     * @param code
     * Code param: <strong>Required</strong>
     *
     * @param clientId
     * Client ID: <strong>Required</strong>
     *
     * @param secret
     * Client Secret: <strong>Required</strong>
     */
    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    public Object getToken(
            @RequestParam("code") String code,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String secret,
            @RequestParam("grant_type") String grantType
    ) throws Exception {
        OAuthEntity oauthInfo = oauthEntity.findByClientId(StringEscapeUtils.escapeJava(clientId));

        if(bc.matches(secret, oauthInfo.getClientSecret())
                && grantType.equals("authorization_code")) {

            return OAuthSessionInfoService.generateAccessToken(clientId, code);
        }

        throw new NullPointerException("Mismatch info");
    }
}
