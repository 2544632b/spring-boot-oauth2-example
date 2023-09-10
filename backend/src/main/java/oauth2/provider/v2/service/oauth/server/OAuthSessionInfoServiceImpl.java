package oauth2.provider.v2.service.oauth.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import oauth2.provider.v2.deque.user.oauth.OAuthSessionDeque;
import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.model.form.response.oauth.OAuthAuthorizeTokenResp;
import oauth2.provider.v2.model.user.info.oauth.server.OAuthSessionInfo;
import oauth2.provider.v2.service.base.oauth.OAuthEntityService;
import oauth2.provider.v2.service.base.user.UserEntityService;
import oauth2.provider.v2.util.random.RandomPassword;
import jakarta.annotation.Resource;
import oauth2.provider.v2.util.jwt.ClientJSONWebToken;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service
public class OAuthSessionInfoServiceImpl implements OAuthSessionInfoService {

    @Resource
    private OAuthSessionDeque deque;

    @Resource
    private OAuthEntityService oauthEntity;

    @Resource
    private UserEntityService userEntity;

    @Resource
    private RandomPassword RandomPassword;

    @Override
    public String generateCode(String clientId, String scope, String username) {
        //  code
        String code = RandomPassword.getPassword();
        //  access_token in JSON Response
        String accessToken = RandomPassword.getPassword();
        // insert into the deque
        if(deque.insert(new OAuthSessionInfo(StringEscapeUtils.escapeJava(clientId), StringEscapeUtils.escapeJava(scope), code, System.currentTimeMillis(), accessToken, username))) {
            return code;
        }
        return null;
    }

    @Override
    public OAuthAuthorizeTokenResp generateAccessToken(String clientId, String code) throws JsonProcessingException {
        // Only in once, authorize code will delete after this
        OAuthSessionInfo info = deque.find(StringEscapeUtils.escapeJava(clientId), StringEscapeUtils.escapeJava(code));

        // Get the open id profile
        // UserEntity userInfo = userEntity.findByUsername(info.getUsername());

        // OAuth2 Access Token
        String accessToken = info.getAccessToken();

        // Final actions
        return new OAuthAuthorizeTokenResp(accessToken, null);
    }

    @Override
    public OAuthAuthorizeTokenResp generateIdToken(String clientId, String code) throws JsonProcessingException {
        // Only in once, authorize code will delete after this
        OAuthSessionInfo info = deque.find(StringEscapeUtils.escapeJava(clientId), StringEscapeUtils.escapeJava(code));

        // Get the open id profile
        String secret = oauthEntity.findByClientId(clientId).getClientSecret();
        String username = info.getUsername();

        ClientJSONWebToken cJ = new ClientJSONWebToken(secret);

        Claims claims = Jwts.claims();
        claims.put("iss", "http://localhost/");
        claims.put("aud", StringEscapeUtils.escapeJava(clientId));
        String idToken = cJ.doGenerateToken(claims, username);

        return new OAuthAuthorizeTokenResp(null, idToken);
    }

    @Override
    public UserEntity getUserEntityFromBearer(String bearer) {
        try {
            String username = deque.find(StringEscapeUtils.escapeJava(bearer)).getUsername();
            return userEntity.findByUsername(username);
        } catch(Exception e) {
            // TODO
        }
        return null;
    }

    @Override
    public void deleteOAuthCodeInfo(String username) {
        deque.delete(username);   // Delete a user session immediately
    }

    @Override
    public String getScope(String bearer) {
        try {
            return deque.find(bearer).getScope();
        }  catch(Exception e) {
            // TODO
        }
        return null;
    }

}
