package oauth2.provider.controller.oauth;

import oauth2.provider.annotation.controller.OAuthController;
import oauth2.provider.model.form.request.oauth.OAuthAuthorizeForm;
import oauth2.provider.model.user.info.entity.OAuthEntity;
import oauth2.provider.service.oauth.server.entity.OAuthEntityService;
import oauth2.provider.service.oauth.server.OAuthSessionInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@OAuthController
public class OAuthAuthorizeCode {

    @Resource
    private OAuthSessionInfoService OAuthSessionInfoService;

    @Resource
    private OAuthEntityService oauthEntity;

    @RequestMapping(value = "/oauth/authorize", method = RequestMethod.POST)
    public Object getCode(@Validated @RequestBody OAuthAuthorizeForm OAuthAuthorizeForm) throws Exception {
        OAuthEntity oauthInfo = oauthEntity.findByClientId(StringEscapeUtils.escapeJava(OAuthAuthorizeForm.getClientId()));
        if("code".equals(OAuthAuthorizeForm.getResponseType())
                && oauthInfo.getScope().equals(OAuthAuthorizeForm.getScope())
                && oauthInfo.getRedirectURI().equals(OAuthAuthorizeForm.getRedirectURI())) {

            String username = SecurityContextHolder.getContext().getAuthentication().getName(); // Get the username and find the entity by a username.
            String code = OAuthSessionInfoService.generateCode(OAuthAuthorizeForm.getClientId(), OAuthAuthorizeForm.getScope(), username);  // Generate the random code and join to deque.

            Map<String, String> direction = new HashMap<>();
            direction.put("url", oauthInfo.getRedirectURI());
            direction.put("code", code);
            direction.put("state", OAuthAuthorizeForm.getState());

            return direction;
        }

        throw new BadCredentialsException("Invalid platform info, please contact to your Service Provider.");
    }
}
