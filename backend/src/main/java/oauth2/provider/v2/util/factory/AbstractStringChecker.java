package oauth2.provider.v2.util.factory;

public abstract class AbstractStringChecker {
    protected String specialChars;
    protected char replace;

    public boolean check(String str) {
        // char isLegalChar = 't';
        // StringBuilder sb = new StringBuilder(" ");
        if(str.isEmpty()) return false;

        for(int i = 0; i < str.length(); ++i) {
            for(int j = 0; j < specialChars.length(); ++j) {
                if(str.charAt(i) == specialChars.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }
}
