package oauth2.provider.queue.user.register;

import jakarta.annotation.Resource;
import oauth2.provider.queue.factory.AbstractCodeQueue;
import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.model.user.info.deque.VerifyCodeInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
public class RegisterVerifyCodeQueueImpl extends AbstractCodeQueue<VerifyCodeInfo> implements RegisterVerifyCodeQueue {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    public boolean isRedis = true;  // Enable or disable the redis

    public RegisterVerifyCodeQueueImpl() {
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
        try {
            // #ifdef isRedis
            if(isRedis) {
                Set<Object> keys = redisTemplate.keys("*");
                assert keys != null;
                for(Object key : keys) {
                    UserEntity val = (UserEntity) redisTemplate.opsForValue().get(key);
                    assert val != null;
                    if(String.valueOf(key).charAt(0) == 'R') {
                        String codeStr = "R" + code;
                        if(
                                String.valueOf(key).equals(codeStr)
                                        || userInfo.getUsername().equals(val.getUsername())
                                        || userInfo.getEmail().equals(val.getEmail())
                        ) {
                            return false;
                        }
                    }
                }
            }
            // #endif
        } catch(Exception e) {
            // SKIP
        }

        findExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
        for (VerifyCodeInfo verifyCodeInfo : queue) {
            if(
                    code == verifyCodeInfo.getCode()
                    || verifyCodeInfo.getLoginUserEntity().getEmail().equals(userInfo.getEmail())
                    || verifyCodeInfo.getLoginUserEntity().getUsername().equals(userInfo.getUsername())
            ) {
                return false;
            }
        }
        try {
            // #ifdef isRedis
            if(isRedis) {
                redisTemplate.opsForValue().set("R" + code, userInfo, 500000, TimeUnit.MILLISECONDS);
                return true;
            }
            // #endif
        } catch(Exception e) {
            // SKIP
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
     * Return a properties & remove it
     */
    @Override
    public UserEntity find(String email, int code) {
        try {
            // #ifdef isRedis
            if(isRedis) {
                String key = "R" + code;
                UserEntity val = (UserEntity) redisTemplate.opsForValue().get(key);

                assert val != null;
                if(val.getEmail().equals(email)) {
                    redisTemplate.delete(key);
                    return val;
                }
            }
            // #endif
        } catch(Exception e) {
            // SKIP
        }
        findExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
        for(VerifyCodeInfo verifyCodeInfo : queue) {
            if(verifyCodeInfo.getLoginUserEntity().getEmail().equals(email) &&
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

    // Internal queue
    @Scheduled(cron = "0/50 * *  * * ? ")
    public void execute() {
        findExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
    }
}
