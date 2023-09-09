package oauth2.provider.v2.service.authentication;

import org.springframework.stereotype.Service;

@Service("LoginFailHandler")
public class LoginFailHandler implements LoginAfterService {
    @Override
    public void handle(String... texts) {
        // Some actions after failed ....
    }
}
