package oauth2.provider.util.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import oauth2.provider.util.functional.oidc.jwk.JWKConfiguration;
import oauth2.provider.util.jwt.factory.AbstractJSONWebToken;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.Serial;

@Component
@DependsOn(value = "jwkConfiguration")
public class ClientJSONWebToken extends AbstractJSONWebToken {

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    @Resource
    private JWKConfiguration jwkConfiguration;

    public ClientJSONWebToken() {
        super.algorithm = SignatureAlgorithm.RS256;
        super.JSON_WEB_TOKEN_VALIDITY = 240;
    }

    @PostConstruct
    public void init() {
        super.secretKey = jwkConfiguration.getPrivateKey();
    }

    public void setPrivateKey(String privateKey) {
        super.secret = privateKey;
    }

}