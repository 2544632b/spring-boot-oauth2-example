package oauth2.provider.service.oauth.server;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import oauth2.provider.queue.user.oauth.OAuthSessionQueue;
import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.model.form.response.oauth.OAuthAuthorizeTokenResp;
import oauth2.provider.model.user.info.oauth.server.OAuthSessionInfo;
import oauth2.provider.service.profile.user.entity.UserEntityService;
import oauth2.provider.util.jwt.ClientJSONWebToken;
import oauth2.provider.util.random.RandomPassword;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service
public class OAuthSessionInfoServiceImpl implements OAuthSessionInfoService {

    @Resource
    private OAuthSessionQueue queue;

    @Resource
    private UserEntityService userEntity;

    @Resource
    private RandomPassword RandomPassword;

    @Resource
    private ClientJSONWebToken ClientJSONWebToken;

    @Override
    public String generateCode(String clientId, String scope, String username) {
        //  code
        String code = RandomPassword.getPassword();
        //  access_token in JSON Response
        String accessToken = RandomPassword.getPassword();
        // insert into the deque
        if(queue.insert(new OAuthSessionInfo(StringEscapeUtils.escapeJava(clientId), StringEscapeUtils.escapeJava(scope), code, System.currentTimeMillis(), accessToken, username))) {
            return code;
        }
        return null;
    }

    @Override
    public OAuthAuthorizeTokenResp generateAccessToken(String clientId, String code) {
        // Only in once, authorize code will delete after this
        OAuthSessionInfo info = queue.find(StringEscapeUtils.escapeJava(clientId), StringEscapeUtils.escapeJava(code));

        // OAuth2 Access Token
        String accessToken = info.getAccessToken();

        // Final actions
        return new OAuthAuthorizeTokenResp(accessToken, null);
    }

    // OpenID JWT
    @Override
    public OAuthAuthorizeTokenResp generateIdToken(String clientId, String code) {
        // Only in once, authorize code will delete after this
        OAuthSessionInfo info = queue.find(StringEscapeUtils.escapeJava(clientId), StringEscapeUtils.escapeJava(code));

        // Get the open id profile
        String username = info.getUsername();

        Claims claims = Jwts.claims();
        claims.put("iss", "http://localhost/");
        claims.put("aud", StringEscapeUtils.escapeJava(clientId));
        claims.put("idp", "test");
        String idToken = ClientJSONWebToken.doGenerateToken(claims, username);

        deleteOAuthSessionInfoByUsername(username);

        return new OAuthAuthorizeTokenResp(null, idToken);
    }

    @Override
    public UserEntity getUserEntityFromBearer(String bearer) {
        try {
            String username = queue.find(StringEscapeUtils.escapeJava(bearer)).getUsername();
            return userEntity.findByUsername(username);
        } catch(Exception e) {
            // TODO
        }
        return null;
    }

    @Override
    public void deleteOAuthSessionInfoByUsername(String username) {
        queue.deleteByUsername(username);   // Delete a user session immediately
    }

    @Override
    public void deleteOAuthSessionInfoByBearer(String bearer) {
        queue.deleteByBearer(bearer);
    }

    @Override
    public String getScope(String bearer) {
        try {
            return queue.find(bearer).getScope();
        }  catch(Exception e) {
            // TODO
        }
        return null;
    }

}
