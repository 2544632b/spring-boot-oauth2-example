package oauth2.provider.util.functional.oidc.jwk;

import oauth2.provider.util.functional.FunctionalTreeMap;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.TreeMap;

public class JWKConfiguration {

    private final FunctionalTreeMap<String, Object> treeMap;

    public Key privateKey;

    public JWKConfiguration() throws NoSuchAlgorithmException {
        treeMap = new FunctionalTreeMap<String, Object>();
    }

    public JWKConfiguration setupRSA() {
        treeMap.putEx("kty", "RSA");
        return this;
    }

    public JWKConfiguration setSig() {
        treeMap.putEx("use", "sig");
        return this;
    }

    public JWKConfiguration setAlg() {
        treeMap.putEx("alg", "RS256");
        return this;
    }

    public JWKConfiguration setPublicKey() {
        treeMap.putEx("kid", "2023-11-29");
        treeMap.putEx("n", "iZbwulCGaELId_FFmBHb0MO-29v9ep5YjVcuAAzW-jolDDIgWuekDeTvfNIY88uhk9j1SPxi0E3eCkp6M9bM61OY6hESW2EKaqMkhS9LEb95ZoOZ8sj130VwUskqAt7kPbDRtTRjqxRdxQyu3AZzVrIGTii1wBnlXaRMULq95VzhwUK6Y0ehvFZBcYiMXN1w17RqNQzaOoc4FmG2yZ_iY-iHIxQdiUBhNIkRughLP0OMePpAgxZgAxsz1C1V5tGXdE14tnmgOe9yCyronH57-6u7pqVJgnJ6bYt0GAg_kwcwnh3gPcQyRSXnjezqW3lxbDXdGxvpRs6HHYXLuAGgHw");
        treeMap.putEx("e", Base64.getEncoder().encodeToString(new byte[] { 0x001, 0x00, 0x01 }));
        return this;
    }

    public JWKConfiguration setPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String str = "MIIEugIBADANBgkqhkiG9w0BAQEFAASCBKQwggSgAgEAAoIBAQCJlvC6UIZoQsh38UWYEdvQw77b2/16nliNVy4ADNb6OiUMMiBa56QN5O980hjzy6GT2PVI/GLQTd4KSnoz1szrU5jqERJbYQpqoySFL0sRv3lmg5nyyPXfRXBSySoC3uQ9sNG1NGOrFF3FDK7cBnNWsgZOKLXAGeVdpExQur3lXOHBQrpjR6G8VkFxiIxc3XDXtGo1DNo6hzgWYbbJn+Jj6IcjFB2JQGE0iRG6CEs/Q4x4+kCDFmADGzPULVXm0Zd0TXi2eaA573ILKuicfnv7q7umpUmCcnpti3QYCD+TBzCeHeA9xDJFJeeN7OpbeXFsNd0bG+lGzocdhcu4AaAfAgMBAAECgf81vsBKByp5j5y/PX/x5wf2hIAJYeWiADiW/Xb6jglrtUrDeNEqsVFkHAlwuKEGmoSDPDyURgJTXBdf7bfOiBmD0yPoSsLg7wcioJejbtXkGOdTJBT+wYFJtdYqgGnotShy8J76KXzFVy9JyaqESADF3MSZw007vOHbslIdNtT2wrlf2NGuRQNT2cGLzKTFpz7zNm646+CJBDrNXeMMa4vHh/R9uQ8fmOjwsCTkgI9uM+ng2iXD4r7j47hMvbJn3XAX1fD9Nn6Lta7RG3R70Uygx+sv+Jwq9NxkjaWxJWOjc/wYYTOes4vDzkFtE+Uxlv2/7MNk8LqkKbaZJaumtwECgYEA9ii2gPT8oU6UYZqBWXbE1QwM3jvEIGiZFhtc/jZ/Rp8qAhYip+T2c9Fml52ph3aBJwh/7+pcuttE5T2EJPcoqfYpjAoNzxlUgS9B+f0djbcK8a4blCaG/VOwlP/wklndV0B4gR8rSf1D/7wzqTAR9YraU3mE9bxlVcZR/Zk0WiECgYEAjxcV+BR3TkKWI/HEJzIizXNwzrNCEkBIVaUC9POfFiNEUcs1K8uRhZx73HbgKltJMggeFrI9dRbZ3XbCzLh2IMzMy9eL2/w+MhI1CUUUr6/xTywFG2dFA+MI2VifJhhJiJonK+ulSF4FsXdFO1rNaRdVGrvyy6bY7NT/6svlMj8CgYB6Tcwv0ola1CIhRJUUEFDH0c7q9CMEPzQgqcPQgt9GBXrnwnvBk59n+BjTKAC9T4HoFO8MExOzu5JFnAT2IN9RYv9Nlwk5Zsp3zTUosjxm1xVW8zy8pU/YuiY4QdoTvRaebWfyL7xgbLfGbon0e+/QYgcMBoqrVcm7jmfaaq56QQKBgHmcQkSkiD1QJDCKYo55ctCwAzj5avb+ATwg0SGECDoVYRQvI5KEIbqvoyldMulsygmmkoPxmn/wGtu4+phpUaRQyuGX4LrnZ6jAhqjJOGqUmv8Rx4lhvGswQq4OPrxuudhKQ251iPS3TBlm58UpSBjABsmA4ToqJIUsBhD0l2ebAoGANYdv8QUEVNArNqay2ZxGFhZhsEkSIj9zNKPx9+9T2EQjxtQQG+2gZKKvMX1zGE/Xmw4SdS0niz1wtvvEVBU8bPBO1Nc0n2EHi4XZcxdF0TaCPWTsR8Akpql8AHJIi70umBGt6TYF8U0PQs6oY6a1QTyBQfF8BWAI6oX7dy/OQgI=";
        byte[] privateByte = Base64.getDecoder().decode(str);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        return this;
    }

    public Key getPrivateKey() {
        return this.privateKey;
    }

    public TreeMap<String, Object> finish() {
        return treeMap.finish();
    }

}
