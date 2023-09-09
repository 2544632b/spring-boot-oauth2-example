package oauth2.provider.v2.service.authentication;

import org.springframework.stereotype.Service;

@Service("LoginSuccessHandler")
public class LoginSuccessHandler implements LoginAfterService {

    @Override
    public void handle(String... texts) {
        // Some actions after success ....
    }
}
