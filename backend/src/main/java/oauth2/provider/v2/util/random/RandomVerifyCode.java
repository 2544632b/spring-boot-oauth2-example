package oauth2.provider.v2.util.random;

import oauth2.provider.v2.util.random.factory.AbstractRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomVerifyCode extends AbstractRandom {
    public RandomVerifyCode() {
        super.str = "0123456789";
    }

    public int getVerifyCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 6; ++i) {
            stringBuilder.append(getChars());
        }

        String final_str = stringBuilder.toString();

        return Integer.parseInt(final_str);
    }
}
