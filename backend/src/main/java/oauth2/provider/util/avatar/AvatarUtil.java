package oauth2.provider.util.avatar;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

@Component
public class AvatarUtil {

    public static final String BASE64_PREFIX = "data:image/png;base64,";

    /**
     * @param id
     * @return Return a String format
     */
    public static String createBase64Avatar(int id) throws IOException {
        return new String(Base64.getEncoder().encode(create(id)));
    }

    /*
    * @param id
    * @return Return a byte array
    */
    public static byte[] create(int id) throws IOException {
        int width = 20;
        int grid = 5;
        int padding = width >> 1;
        int size = width * grid + width;
        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(new Color(255, 255, 255));
        graphics2D.fillRect(0, 0, size, size);
        graphics2D.setColor(randomColor(80, 240));
        char[] idCharacters = createIdent(id);
        int i = idCharacters.length;
        for(int x = 0; x < Math.ceil(grid / 2.0); ++x) {
            for(int y = 0; y < grid; ++y) {
                if(idCharacters[--i] < 53) {
                    graphics2D.fillRect((padding + x * width), (padding + y * width), width, width);
                    if(x < Math.floor(grid >> 1)) {
                        graphics2D.fillRect((padding + ((grid - 1) - x) * width), (padding + y * width), width, width);
                    }
                }
            }
        }
        graphics2D.dispose();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static Color randomColor(int fc, int bc) {
        Random random = new SecureRandom();
        if(fc >= 255) fc = 240;
        if(fc < 0) fc = 0;
        if(bc >= 255) bc = 240;
        if(bc < 0) bc = 0;

        int r = fc + random.nextInt(Math.abs(bc - fc));
        int g = fc + random.nextInt(Math.abs(bc - fc));
        int b = fc + random.nextInt(Math.abs(bc - fc));

        return new Color(r, g, b);
    }

    private static char[] createIdent(int id) {
        BigInteger bic = new BigInteger((String.valueOf(id)).getBytes());
        BigInteger bi = new BigInteger(String.valueOf(id), 36);
        bi = bi.xor(bic);
        return bi.toString(10).toCharArray();
    }
}
