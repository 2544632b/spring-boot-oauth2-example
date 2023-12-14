package oauth2.provider.service.oauth.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import oauth2.provider.annotation.exception.RetryAfterException;
import oauth2.provider.model.form.response.login.LoginResp;
import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.model.user.info.oauth.client.GithubTokenResp;
import oauth2.provider.model.user.info.oauth.client.GithubUserProfileResp;
import oauth2.provider.service.profile.user.entity.UserEntityService;
import oauth2.provider.util.functional.FunctionalHttpHeaders;
import oauth2.provider.util.functional.FunctionalLinkedMultiValueMap;
import oauth2.provider.util.jwt.JSONWebToken;
import oauth2.provider.util.aes.AESUtil;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class GithubOAuthServiceImpl implements GithubOAuthService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private JSONWebToken JSONWebToken;

    @Resource
    private AESUtil AESUtil;

    @Resource
    private UserEntityService userEntity;

    /**
     * Callback service, get the user info from GitHub.
     * @param state
     * @param code
     * @return Return a user session
     * @throws Exception
     */
    @Override
    @RetryAfterException(times = 5, interval = 1)
    public Object getUserInfo(String state, String code, String currentIp) throws Exception {
        // Noting to do with `state` because of state already completed on browser.

        // Get an access token.
        String githubURL = "https://github.com/login/oauth/access_token";
        HttpHeaders httpHeaders1 = new FunctionalHttpHeaders()
                .setContentTypeEx(MediaType.APPLICATION_FORM_URLENCODED).finish();

        MultiValueMap<String, String> form = new FunctionalLinkedMultiValueMap<String, String>()
                .addEx("client_id", "INVALID_ID") // GitHub Client ID
                .addEx("client_secret", "INVALID_SECRET") // GitHub Client Secret
                .addEx("code", StringEscapeUtils.escapeJava(code)) // Authorization code
                .addEx("grant_type", "authorization_code") // Authorization code only, see RFC 6749.
                .finish();

        HttpEntity<MultiValueMap<String, String>>   request1 = new HttpEntity<>(form, httpHeaders1);
        ResponseEntity<GithubTokenResp> response1 = restTemplate.postForEntity(githubURL, request1, GithubTokenResp.class);

        // Get the user info.
        String githubUserInfoURL = "https://api.github.com/user";
        HttpHeaders httpHeaders2 = new FunctionalHttpHeaders()
                .setBearerAuthEx(Objects.requireNonNull(response1.getBody()).getAccessToken())
                .setEx("X-GitHub-Api-Version", "2022-11-28")
                .finish();
        HttpEntity<String> request2 = new HttpEntity<>("", httpHeaders2);
        ResponseEntity<GithubUserProfileResp> response2 = restTemplate.exchange(githubUserInfoURL, HttpMethod.GET, request2, GithubUserProfileResp.class);

        String username = Objects.requireNonNull(response2.getBody()).getLogin();
        BigInteger id = Objects.requireNonNull(response2.getBody()).getId();
        String email = response2.getBody().getEmail();

        UserEntity user = userEntity.findByGithubId(id);

        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date2 = dateFormat.format(date);
        var loginResp = LoginResp.make(user.getEmail(), user.getUsername(), null, currentIp, date2);

        // Transform to signature token
        Map<String, String> session = new HashMap<>();
        session.put("session_id", AESUtil.encrypt(JSONWebToken.generateToken(new ObjectMapper().writeValueAsString(loginResp))));
        return session;
    }
}
