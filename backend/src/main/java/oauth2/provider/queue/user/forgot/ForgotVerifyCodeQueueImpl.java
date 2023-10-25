package oauth2.provider.queue.user.forgot;

import oauth2.provider.queue.factory.AbstractCodeQueue;
import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.model.user.info.deque.VerifyCodeInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class ForgotVerifyCodeQueueImpl extends AbstractCodeQueue<VerifyCodeInfo> implements ForgotVerifyCodeQueue {
    public ForgotVerifyCodeQueueImpl() {
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
        findExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
        for (VerifyCodeInfo verifyCodeInfo : queue) {
            if (userInfo.getEmail().equals(verifyCodeInfo.getLoginUserEntity().getEmail())) {
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
        findExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
        for (VerifyCodeInfo verifyCodeInfo : queue) {
            if (verifyCodeInfo.getLoginUserEntity().getEmail().equals(email) &&
                    code == verifyCodeInfo.getCode()
            ) {
                try {
                    if(lock.tryLock(5, TimeUnit.SECONDS)) {
                        UserEntity temp = verifyCodeInfo.getLoginUserEntity();
                        queue.remove(verifyCodeInfo);
                        return temp;
                    }
                } catch(Exception e) {
                    lock.unlock();
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
        return null;
    }

    @Scheduled(cron = "0/50 * *  * * ? ")
    public void execute() {
        findExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
    }
}
