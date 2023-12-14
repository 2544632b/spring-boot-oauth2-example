package oauth2.provider.service.email;

public interface EmailSenderService {
    String applicationName = "Application";
    void send(String email, String title, String content);
}
