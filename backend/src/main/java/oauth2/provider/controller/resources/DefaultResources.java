package oauth2.provider.controller.resources;

import jakarta.annotation.Resource;
import oauth2.provider.annotation.controller.BearerController;
import oauth2.provider.annotation.exception.RetryAfterException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@BearerController
public class DefaultResources {

    @Resource
    private RestTemplate restTemplate;

    @RequestMapping(value = "/default/user/email", method = RequestMethod.POST)
    @RetryAfterException(times = 3)
    public Object getEmail(Object obj) {
        String url = "https://hs.example.oauth2-provider.net/resources/user/email";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth("d9g7aaaGVBqx-6677bedaF");
        HttpEntity<Object> request = new HttpEntity<>(obj, httpHeaders);
        return this.restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
    }

}