package oauth2.provider.v2.deque.user.forgot;

import oauth2.provider.v2.deque.factory.AbstractCodeDeque;
import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.model.user.info.deque.VerifyCodeInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class ForgotVerifyCodeDequeImpl extends AbstractCodeDeque<VerifyCodeInfo> implements ForgotVerifyCodeDeque {
    public ForgotVerifyCodeDequeImpl() {
        super();
    }

    /**
     * @param userInfo
     * Insert a properties and return a value.
     *
     * @return boolean
     */
    @Override
    public boolean insert(UserEntity userInfo, int code) {
        checkExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
        for (VerifyCodeInfo verifyCodeInfo : /*VerifyCodeInfoS*/ deque) {
            if (userInfo.getEmail().equals(verifyCodeInfo.getUserEntity().getEmail())) {
                return false;
            }
        }

        add(new VerifyCodeInfo(userInfo, code));
        return true;
    }

    /**
     * @param email
     * Email string
     *
     * @param code
     * Integer code
     *
     * @return UserEntity
     * Return a properties
     */
    @Override
    public UserEntity find(String email, int code) {
        checkExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
        for (VerifyCodeInfo verifyCodeInfo : /*VerifyCodeInfoS*/ deque) {
            if (verifyCodeInfo.getUserEntity().getEmail().equals(email) &&
                    code == verifyCodeInfo.getCode()
            ) {
                try {
                    if(lock.tryLock(5, TimeUnit.SECONDS)) {
                        UserEntity temp = verifyCodeInfo.getUserEntity();
                        /*VerifyCodeInfoS*/ deque.remove(verifyCodeInfo);
                        return temp;
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    lock.unlock();
                } finally {
                    lock.unlock();
                }
            }
        }
        return null;
    }

    @Scheduled(cron = "0/50 * *  * * ? ")
    public void delete() {
        checkExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
    }
}
