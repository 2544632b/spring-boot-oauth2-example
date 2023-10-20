package oauth2.provider.v2.util.checker;

import oauth2.provider.v2.util.checker.factory.AbstractStringChecker;
import org.springframework.stereotype.Component;

@Component
public class EmailStringChecker extends AbstractStringChecker {

    public EmailStringChecker() {
        super.specialChars = "/\\?<>[]{}':();$#&%\"\n\r\f\b\t\0` ";
        super.replace = '_';
    }

    @Override
    public boolean check(String str) {
        if(str.length() < 5 || str.length() > 30 || str.charAt(0) == '@' || str.charAt(str.length() - 3) == '@') {
            return false;
        }

        boolean isEmail = false;

        for(int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '@') {
                isEmail = true;
                break;
            }
        }

        if(isEmail) {
            return super.check(str);
        }

        return false;
    }
}
