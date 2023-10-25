package oauth2.provider.service.base.email;

public interface EmailSenderService {
    String applicationName = "Application";
    void send(String email, String title, String content);
}
