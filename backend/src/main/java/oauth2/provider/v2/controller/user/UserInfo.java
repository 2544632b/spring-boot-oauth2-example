package oauth2.provider.v2.controller.user;

import oauth2.provider.v2.model.user.info.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserInfo {

    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    //@PreAuthorize("authenticated")    <-- Needn't this, filter has done.
    @ResponseBody
    public Object getUserInfo() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity)authentication.getPrincipal();
    }
}
