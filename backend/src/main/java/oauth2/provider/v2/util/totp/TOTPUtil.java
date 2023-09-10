package oauth2.provider.v2.util.totp;

import oauth2.provider.v2.util.base.Base32;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TOTPUtil {
    public String generateToken() {
        Random rd = new Random();
        byte[] arr = new byte[20];
        rd.nextBytes(arr);
        return Base32.encode(arr);
    }
}
