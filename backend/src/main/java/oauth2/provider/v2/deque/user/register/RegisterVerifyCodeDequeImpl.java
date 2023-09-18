package oauth2.provider.v2.deque.user.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import oauth2.provider.v2.deque.factory.AbstractCodeDeque;
import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.model.user.info.deque.VerifyCodeInfo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RegisterVerifyCodeDequeImpl extends AbstractCodeDeque<VerifyCodeInfo> implements RegisterVerifyCodeDeque {

    @Resource
    private RedisTemplate<Object, Integer> redisTemplate;

    public RegisterVerifyCodeDequeImpl() {
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
            redisTemplate.opsForValue().set(new ObjectMapper().writeValueAsString(userInfo), code);
        } catch(Exception e) {
            e.printStackTrace();
        }
        checkExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
        for (VerifyCodeInfo verifyCodeInfo : deque) {
            if (verifyCodeInfo.getUserEntity().getEmail().equals(userInfo.getEmail())) {
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
     * @return LoginUserEntity
     * Return a properties
     */
    @Override
    public UserEntity find(String email, int code) {
        checkExpired(VerifyCodeInfo -> System.currentTimeMillis() - VerifyCodeInfo.getExpire() >= 50000);
        for (VerifyCodeInfo verifyCodeInfo : deque) {
            if (verifyCodeInfo.getUserEntity().getEmail().equals(email) &&
                    code == verifyCodeInfo.getCode()
            ) {
                try {
                    if(lock.tryLock(5, TimeUnit.SECONDS)) {
                        UserEntity temp = verifyCodeInfo.getUserEntity();
                        deque.remove(verifyCodeInfo);
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
