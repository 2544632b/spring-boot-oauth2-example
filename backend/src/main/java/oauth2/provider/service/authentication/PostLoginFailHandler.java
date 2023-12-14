package oauth2.provider.service.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("LoginFailHandler")
public class PostLoginFailHandler implements PostLoginService {

    private static final Logger logger = LoggerFactory.getLogger(PostLoginService.class);

    @Override
    public void handle(String... texts) {
        // Some actions after failed ....
        logger.info("{} log in failed at {}, {}", texts[0], texts[2], texts[3]);
    }
}
