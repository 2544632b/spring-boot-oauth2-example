package oauth2.provider.controller.user;

import oauth2.provider.model.user.info.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserInfo {

    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserInfo() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity)authentication.getPrincipal();
    }
}
