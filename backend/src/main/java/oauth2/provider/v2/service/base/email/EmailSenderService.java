package oauth2.provider.v2.service.base.email;

public interface EmailSenderService {
    String applicationName = "Application";
    void send(String email, String title, String content);
}
