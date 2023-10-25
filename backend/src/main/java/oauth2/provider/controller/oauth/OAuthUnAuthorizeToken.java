package oauth2.provider.controller.oauth;

import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.service.oauth.server.OAuthSessionInfoService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthUnAuthorizeToken {

    @Resource
    private OAuthSessionInfoService OAuthSessionInfoService;

    @RequestMapping(value = "/oauth/un-authorize", method = RequestMethod.GET)
    public void unAuthorize() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserEntity userInfo = (UserEntity) authentication.getPrincipal();  // Object -> LoginUserEntity
            OAuthSessionInfoService.deleteOAuthSessionInfoByUsername(userInfo.getUsername());
        } catch(Exception e) {
            // TODO
            // Noting to do.
        }
    }
}
