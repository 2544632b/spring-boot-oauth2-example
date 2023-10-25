package oauth2.provider.queue.user.register;

import jakarta.annotation.Resource;
import oauth2.provider.queue.factory.AbstractCodeQueue;
import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.model.user.info.deque.VerifyCodeInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RegisterVerifyCodeQueueImpl extends AbstractCodeQueue<VerifyCodeInfo> implements RegisterVerifyCodeQueue {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    private boolean isRedis = true;

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
        /*
        if(isRedis) {
            Set<Object> keys = redisTemplate.keys("*");
            //assert keys != null;
            for(Object key : keys) {
                System.out.println(key);
                UserEntity val = (UserEntity) redisTemplate.opsForValue().get(key);
                //assert val != null;
                if(userInfo.getUsername().equals(val.getUsername()) || userInfo.getEmail().equals(val.getEmail())) {
                    return false;
                }
            }

            redisTemplate.opsForValue().set(String.valueOf(code), userInfo, 50000, TimeUnit.MICROSECONDS);
            return true;
        }
        */

        findExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
        for (VerifyCodeInfo verifyCodeInfo : queue) {
            if (verifyCodeInfo.getLoginUserEntity().getEmail().equals(userInfo.getEmail())) {
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
     * Return a properties & remove it
     */
    @Override
    public UserEntity find(String email, int code) {
        /*
        if(isRedis) {
            Set<Object> keys = redisTemplate.keys("*");
            //assert keys != null;
            for(Object key : keys) {
                UserEntity val = (UserEntity) redisTemplate.opsForValue().get(key);
                //assert val != null;
                if(email.equals(val.getEmail()) && String.valueOf(code).equals(key)) {
                    redisTemplate.delete(key);
                    return val;
                }
            }
        }
        */

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
