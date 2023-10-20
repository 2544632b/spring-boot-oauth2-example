package oauth2.provider.v2.util.checker;

import oauth2.provider.v2.util.checker.factory.AbstractStringChecker;
import org.springframework.stereotype.Component;

@Component
public class UsernameStringChecker extends AbstractStringChecker {
    public UsernameStringChecker() {
        super.specialChars = "./\\?<>[]{}':();@$%&#\"\n\r\f\b\t\0` ";
        super.replace = '_';
    }

    @Override
    public boolean check(String str) {
        if(str.isEmpty() || str.length() > 12) {
            return false;
        }
        return super.check(str);
    }
}
