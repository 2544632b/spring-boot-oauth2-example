package oauth2.provider.v2.service.authentication;

import org.springframework.stereotype.Service;

@Service("LoginSuccessHandler")
public class PostLoginSuccessHandler implements PostLoginService {

    @Override
    public void handle(String... texts) {
        // Some actions after success ....
    }
}
