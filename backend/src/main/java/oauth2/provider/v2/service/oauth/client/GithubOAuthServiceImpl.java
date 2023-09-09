package oauth2.provider.v2.service.oauth.client;

import oauth2.provider.v2.annotation.exception.RetryAfterException;
import oauth2.provider.v2.model.user.info.oauth.client.GithubTokenResp;
import oauth2.provider.v2.model.user.info.oauth.client.GithubUserProfileResp;
import oauth2.provider.v2.util.JSONWebToken;
import oauth2.provider.v2.util.AESUtil;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class GithubOAuthServiceImpl implements GithubOAuthService {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private JSONWebToken JSONWebToken;
    @Resource
    private AESUtil AESUtil;

    /**
     * Callback service, get the user info from GitHub.
     * @param state
     * @param code
     * @return Return a user session
     * @throws Exception
     */
    @Override
    @RetryAfterException(times = 5, interval = 1)
    public Object getUserInfo(String state, String code) throws Exception {
        // Noting to do with `state` because of state already completed on browser session storage.

        // Get the access token of a user.
        String githubURL = "https://github.com/login/oauth/access_token";
        HttpHeaders httpHeaders1 = new HttpHeaders();

        httpHeaders1.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", "FAKE_CLIENT_ID");  // GitHub Client ID
        form.add("client_secret", "FAKE_CLIENT_SECRET");  // GitHub Client Secret
        form.add("code", StringEscapeUtils.escapeJava(code));   // Authorization code
        form.add("grant_type", "authorization_code");   // Standard param, see the RFC for OAuth 2.0
        form.add("redirect_uri", "http://localhost/authorize/github/continue"); // Redirect to front end

        HttpEntity<MultiValueMap<String, String>> request1 = new HttpEntity<>(form, httpHeaders1);
        ResponseEntity<GithubTokenResp> response1 = restTemplate.postForEntity(githubURL, request1, GithubTokenResp.class);

        // Get the user info
        String githubUserInfoURL = "https://api.github.com/user";
        HttpHeaders httpHeaders2 = new HttpHeaders();
        httpHeaders2.setBearerAuth(Objects.requireNonNull(response1.getBody()).getAccessToken());
        httpHeaders2.set("X-GitHub-Api-Version", "2022-11-28"); // Last API Version
        HttpEntity<String> request2 = new HttpEntity<>("", httpHeaders2);   // Only post can set the http headers
        ResponseEntity<GithubUserProfileResp> response2 = restTemplate.postForEntity(githubUserInfoURL, request2, GithubUserProfileResp.class);

        /*
        // User handle example
        String username = Objects.requireNonNull(response2.getBody()).getLogin();
        String email = response2.getBody().getEmail();

        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date2 = dateFormat.format(date);
        LoginResp loginResp = LoginResp.make("github/" + username, StringEscapeUtils.escapeJava(username), null, null, date2);

        // Transform to signature token
        Map<String, String> SessionID = new HashMap<>();
        SessionID.put("SessionID", AESUtil.encrypt(JSONWebToken.generateToken(new ObjectMapper().writeValueAsString(loginResp))));
        return SessionID;
        */

        return null;
    }
}
