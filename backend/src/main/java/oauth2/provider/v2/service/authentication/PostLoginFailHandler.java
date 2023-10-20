package oauth2.provider.v2.service.authentication;

import org.springframework.stereotype.Service;

@Service("LoginFailHandler")
public class PostLoginFailHandler implements PostLoginService {
    @Override
    public void handle(String... texts) {
        // Some actions after failed ....
    }
}
