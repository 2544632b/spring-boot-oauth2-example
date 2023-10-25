package oauth2.provider.email;

/*
 *  Platform: Windows 11 22H2 22621.1413
 *  Java version: 17.0.1 2021-10-19 LTS
 *  Java(TM) SE Runtime Environment: build 17.0.1+12-LTS-39
 *  Java HotSpot(TM) 64-Bit Server VM: build 17.0.1+12-LTS-39, mixed mode, sharing
 */

public interface Email {
    public boolean sendEmail(String email, String title, String content);
}
