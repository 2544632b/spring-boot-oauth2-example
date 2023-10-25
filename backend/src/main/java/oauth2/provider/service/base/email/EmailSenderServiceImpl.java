package oauth2.provider.service.base.email;

import oauth2.provider.email.Email;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Resource
    private Email email;

    private static final Logger logger = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

    @Async
    @Override
    public void send(String email, String title, String content) {
        logger.info("Sending email to {}", email);
        this.email.sendEmail(
                email,
                title,
                content
        );
    }
}
