package oauth2.provider.controller.openid;

import oauth2.provider.util.functional.oidc.ServerConfiguration;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenidConfiguration {

    @Resource
    private ServerConfiguration serverConfiguration;

    @RequestMapping(value = "/.well-known/openid-configuration", method = RequestMethod.GET)
    public Object getConfig() {
        return serverConfiguration.finish();
    }

}
