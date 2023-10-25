package oauth2.provider.service.register;

public interface RegisterService {
    public boolean doRegister(String email, String username, String password, String address);
    public boolean finalRegister(String email, int code);
}
