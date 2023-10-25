package oauth2.provider.util.random;

import oauth2.provider.util.random.factory.AbstractRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomPassword extends AbstractRandom {

    public RandomPassword() {
        super.str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    }

    public String getPassword() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 15; ++i) {
            stringBuilder.append(getChars());
        }
        return stringBuilder.toString();
    }
}
