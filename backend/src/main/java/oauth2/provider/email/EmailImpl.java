package oauth2.provider.email;

import jakarta.annotation.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EmailImpl implements Email {
    @Resource
    private JavaMailSender javaMailSender;

    /**
     * Simplify email sender
     **/
    @Override
    public boolean sendEmail(String email, String title, String content) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("noreply@example.net");
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSentDate(new Date());
            simpleMailMessage.setSubject(title);
            simpleMailMessage.setText(content);
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
