package oauth2.provider.controller.resources;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import oauth2.provider.annotation.controller.BearerController;
import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.service.oauth.server.OAuthSessionInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@BearerController
public class UnAuthorizeBearer {

    @Resource
    private OAuthSessionInfoService OAuthSessionInfoService;

    @RequestMapping(value = "/default/bearer/un-authorize", method = RequestMethod.GET)
    public void unAuthorize(HttpServletRequest request) {
        String[] texts = request.getHeader("Authorization").split(" ");

        UserEntity userInfo = OAuthSessionInfoService.getUserEntityFromBearer(texts[1]);
        OAuthSessionInfoService.deleteOAuthSessionInfoByUsername(userInfo.getUsername());
    }

}
