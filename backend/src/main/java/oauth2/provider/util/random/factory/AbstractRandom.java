package oauth2.provider.util.random.factory;

import java.security.SecureRandom;

public abstract class AbstractRandom {
    protected String str;
    public char getChars() {
        SecureRandom random = new SecureRandom();
        return str.charAt(random.nextInt(str.length()));
    }
}
