package oauth2.provider.util.aes;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@Component
public class AESUtil {

    private final byte[] raw = "1123141111114590".getBytes(StandardCharsets.UTF_8);

    private final byte[] key_iv = "eaqLqCj0oq#$(+$%".getBytes(StandardCharsets.UTF_8);

    public AESUtil() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
    }

    public String encrypt(String str) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(this.key_iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
        byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(encrypted);
    }

    public String decrypt(String str) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(this.key_iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
        byte[] dec64 = Base64.decodeBase64(str);
        byte[] original = cipher.doFinal(dec64);
        return new String(original, StandardCharsets.UTF_8);
    }
}
