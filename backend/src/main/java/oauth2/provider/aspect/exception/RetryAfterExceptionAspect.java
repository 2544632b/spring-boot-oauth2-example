package oauth2.provider.aspect.exception;

import oauth2.provider.annotation.exception.RetryAfterException;
import oauth2.provider.util.base.Operator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RetryAfterExceptionAspect {
    @Pointcut("@annotation(oauth2.provider.annotation.exception.RetryAfterException)")
    private void callRetry() {}

    @Around("callRetry()")
    public Object retry(ProceedingJoinPoint joinPoint) throws InterruptedException {
        RetryAfterException retry = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(RetryAfterException.class);
        int maxRetryTimes = retry.times();
        int retryInterval = retry.interval();

        Throwable error = new RuntimeException();
        for(int retryTimes = 1; retryTimes <= maxRetryTimes; retryTimes++) {
            try {
                return joinPoint.proceed();
            } catch(Throwable t) {
                error = t;
                System.err.println("Something is not working, now retrying... #" + retryTimes);
            }
            Thread.sleep(Operator.mul(retryInterval, (int)1000L));
        }

        throw new RuntimeException("Can not finish the task after #" + maxRetryTimes + " ", error);
    }
}
