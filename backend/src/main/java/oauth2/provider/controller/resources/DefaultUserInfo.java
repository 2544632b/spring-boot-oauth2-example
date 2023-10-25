package oauth2.provider.controller.resources;

import oauth2.provider.annotation.controller.BearerController;
import oauth2.provider.model.user.info.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@BearerController
public class DefaultUserInfo {

    @RequestMapping(value = "/default/user/info", method = RequestMethod.GET)
    public Object responseInfo() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userInfo = (UserEntity) authentication.getPrincipal();
        return new UserEntity(null, userInfo.getUsername(), userInfo.getLastLoginDate());
    }

}
