package oauth2.provider.queue.user.oauth;

import oauth2.provider.queue.factory.AbstractCodeQueue;
import oauth2.provider.model.user.info.oauth.server.OAuthSessionInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

@Repository
public class OAuthSessionQueueImpl extends AbstractCodeQueue<OAuthSessionInfo> implements OAuthSessionQueue {

    public OAuthSessionQueueImpl() {
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
        for(OAuthSessionInfo oAuthSessionInfo : queue) {
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
        for(OAuthSessionInfo oAuthSessionInfo : queue) {
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
        for(OAuthSessionInfo oAuthSessionInfo : queue) {
            if(oAuthSessionInfo.getAccessToken().equals(bearer)) {
                return oAuthSessionInfo;
            }
        }
        return null;
    }

    @Override
    public void deleteByUsername(String username) {
        findExpired(OAuthSessionInfo -> System.currentTimeMillis() - OAuthSessionInfo.getExpire() >= (180 * 1000));
        queue.removeIf(OAuthSessionInfo -> OAuthSessionInfo.getUsername().equals(username));
    }

    @Override
    public void deleteByBearer(String bearer) {
        findExpired(OAuthSessionInfo -> System.currentTimeMillis() - OAuthSessionInfo.getExpire() >= (180 * 1000));
        queue.removeIf(OAuthSessionInfo -> OAuthSessionInfo.getAccessToken().equals(bearer));
    }

    @Scheduled(cron = "0/50 * *  * * ? ")
    public void execute() {
        findExpired(OAuthSessionInfo -> System.currentTimeMillis() - OAuthSessionInfo.getExpire() >= (180 * 1000));
    }
}
