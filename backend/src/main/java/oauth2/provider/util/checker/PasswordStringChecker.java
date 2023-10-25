package oauth2.provider.util.checker;

import oauth2.provider.util.checker.factory.AbstractStringChecker;
import org.springframework.stereotype.Component;

@Component
public class PasswordStringChecker extends AbstractStringChecker {
    public PasswordStringChecker() {
        super.specialChars = "/\\?<>[]{}':();\"\n\r\f\b\t\0 ";
        super.replace = '_';
    }

    @Override
    public boolean check(String str) {
        return super.check(str);
    }
}
