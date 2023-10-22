package oauth2.provider.v2.deque.user.oauth;

import oauth2.provider.v2.deque.factory.AbstractCodeDeque;
import oauth2.provider.v2.model.user.info.oauth.server.OAuthSessionInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OAuthSessionDequeImpl extends AbstractCodeDeque<OAuthSessionInfo> implements OAuthSessionDeque {

    public OAuthSessionDequeImpl() {
        super();
    }

    /**
     * @param codeInfo
     * Insert method
     *
     * @return boolean
     * Return a boolean
     **/
    @Override
    public boolean insert(OAuthSessionInfo codeInfo) {
        findExpired(OAuthSessionInfo -> System.currentTimeMillis() - OAuthSessionInfo.getExpire() >= (180 * 1000));
        for(OAuthSessionInfo oAuthSessionInfo : deque) {
            if(oAuthSessionInfo.getCode().equals(codeInfo.getCode()) || oAuthSessionInfo.getAccessToken().equals(codeInfo.getAccessToken())) {
                return false;
            }
        }

        // Add finally
        add(codeInfo);
        return true;
    }

    /**
     * @param clientId
     * Client ID param
     *
     * @param code
     * Code generated
     *
     * @return OAuthCodeInfo
     * return a properties from token end points
     */
    @Override
    public OAuthSessionInfo find(String clientId, String code) {
        findExpired(OAuthSessionInfo -> System.currentTimeMillis() - OAuthSessionInfo.getExpire() >= (180 * 1000));
        for(OAuthSessionInfo oAuthSessionInfo : deque) {
            if(oAuthSessionInfo.getClientId().equals(clientId) && oAuthSessionInfo.getCode().equals(code)) {
                oAuthSessionInfo.removeCode();
                return oAuthSessionInfo;
            }
        }
        return null;
    }

    /**
     *
     * @param bearer
     * Bearer token from third party site
     *
     * @return
     * Return a profile from resource end points
     */
    @Override
    public OAuthSessionInfo find(String bearer) {
        findExpired(OAuthSessionInfo -> System.currentTimeMillis() - OAuthSessionInfo.getExpire() >= (180 * 1000));
        for(OAuthSessionInfo oAuthSessionInfo : deque) {
            if(oAuthSessionInfo.getAccessToken().equals(bearer)) {
                return oAuthSessionInfo;
            }
        }
        return null;
    }

    @Override
    public void deleteByUsername(String username) {
        findExpired(OAuthSessionInfo -> System.currentTimeMillis() - OAuthSessionInfo.getExpire() >= (180 * 1000));
        deque.removeIf(OAuthSessionInfo -> OAuthSessionInfo.getUsername().equals(username));
    }

    @Override
    public void deleteByBearer(String bearer) {
        findExpired(OAuthSessionInfo -> System.currentTimeMillis() - OAuthSessionInfo.getExpire() >= (180 * 1000));
        deque.removeIf(OAuthSessionInfo -> OAuthSessionInfo.getAccessToken().equals(bearer));
    }

    @Scheduled(cron = "0/50 * *  * * ? ")
    public void execute() {
        findExpired(OAuthSessionInfo -> System.currentTimeMillis() - OAuthSessionInfo.getExpire() >= (180 * 1000));
    }
}
