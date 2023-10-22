package oauth2.provider.v2.service.oauth.server;

import oauth2.provider.v2.deque.user.oauth.OAuthSessionDeque;
import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.model.form.response.oauth.OAuthAuthorizeTokenResp;
import oauth2.provider.v2.model.user.info.oauth.server.OAuthSessionInfo;
import oauth2.provider.v2.service.base.user.UserEntityService;
import oauth2.provider.v2.util.random.RandomPassword;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OAuthSessionInfoServiceImpl implements OAuthSessionInfoService {

    @Resource
    private OAuthSessionDeque deque;

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
    public OAuthAuthorizeTokenResp generateAccessToken(String clientId, String code) {
        // Only in once, authorize code will delete after this
        OAuthSessionInfo info = deque.find(StringEscapeUtils.escapeJava(clientId), StringEscapeUtils.escapeJava(code));

        // OAuth2 Access Token
        String accessToken = info.getAccessToken();

        // Final actions
        return new OAuthAuthorizeTokenResp(accessToken, null);
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
    public void deleteOAuthSessionInfoByUsername(String username) {
        deque.deleteByUsername(username);   // Delete a user session immediately
    }

    @Override
    public void deleteOAuthSessionInfoByBearer(String bearer) {
        deque.deleteByBearer(bearer);
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
