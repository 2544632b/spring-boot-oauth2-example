package oauth2.provider.v2.service.profile.user.change;

public interface ChangeUserDetailsService {
    public boolean changeUsername(String username, String new_username);
    public boolean changePassword(String username, String new_password);
}
