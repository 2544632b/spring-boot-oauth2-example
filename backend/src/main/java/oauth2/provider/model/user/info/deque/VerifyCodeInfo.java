package oauth2.provider.model.user.info.deque;

import oauth2.provider.model.user.info.entity.UserEntity;

public class VerifyCodeInfo {
    private UserEntity userInfo;
    private long expire;
    private int code;
    public VerifyCodeInfo() {}
    public VerifyCodeInfo(UserEntity userInfo, int code) {
        this.userInfo = userInfo;
        this.code = code;
        this.expire = System.currentTimeMillis();
    }

    public UserEntity getLoginUserEntity() {
        return userInfo;
    }
    public long getExpire() {
        return expire;
    }
    public int getCode() {
        return code;
    }
}
