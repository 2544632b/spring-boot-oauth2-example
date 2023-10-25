package oauth2.provider.util.jwt;

import java.io.Serial;
import java.io.Serializable;

import oauth2.provider.util.jwt.factory.AbstractJSONWebToken;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JSONWebToken extends AbstractJSONWebToken implements Serializable {
    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    public JSONWebToken() {
        super.secret = "f8ertidigufufqqEPGFHL999801";
        super.algorithm = SignatureAlgorithm.HS512;
        super.JSON_WEB_TOKEN_VALIDITY = 240;
    }
}
