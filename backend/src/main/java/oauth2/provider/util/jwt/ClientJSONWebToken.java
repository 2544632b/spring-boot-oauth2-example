package oauth2.provider.util.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import oauth2.provider.util.jwt.factory.AbstractJSONWebToken;

import java.io.Serial;

public class ClientJSONWebToken extends AbstractJSONWebToken {

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    public ClientJSONWebToken(String secret) {
        super.secret = secret;
        super.algorithm = SignatureAlgorithm.HS512;
        super.JSON_WEB_TOKEN_VALIDITY = 240;
    }

}
