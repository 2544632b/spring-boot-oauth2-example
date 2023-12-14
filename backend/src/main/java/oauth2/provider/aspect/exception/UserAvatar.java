package oauth2.provider.aspect.exception;

import jakarta.annotation.Resource;
import oauth2.provider.util.avatar.AvatarUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserAvatar {

    @RequestMapping(value = "/user/avatar", method = RequestMethod.GET)
    public Object getAvatar() throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String avatar = AvatarUtil.createBase64Avatar(Math.abs(username.hashCode()));

        Map<String, String> result = new HashMap<>();
        result.put("avatar", AvatarUtil.BASE64_PREFIX + avatar);

        return result;
    }

}
