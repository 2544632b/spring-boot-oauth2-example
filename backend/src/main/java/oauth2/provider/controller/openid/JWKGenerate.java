package oauth2.provider.controller.openid;

import oauth2.provider.util.functional.oidc.jwk.JWKConfiguration;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
public class JWKGenerate {

    @Resource
    private JWKConfiguration JWKConfiguration;

    @RequestMapping(value = "/openid/jwks", method = RequestMethod.GET)
    public Object getKey() throws NoSuchAlgorithmException {
        return JWKConfiguration.finish();
    }

}
