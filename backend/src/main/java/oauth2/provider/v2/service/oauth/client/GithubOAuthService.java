package oauth2.provider.v2.service.oauth.client;


public interface GithubOAuthService {
    public Object getUserInfo(String state, String code, String currentIp) throws Exception;
}
