package oauth2.provider.aspect.request;

import jakarta.servlet.http.HttpServletRequest;
import oauth2.provider.annotation.request.RequestLimit;
import oauth2.provider.queue.factory.AbstractCodeQueue;
import oauth2.provider.model.user.info.queue.RequestInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class RequestLimitAspect extends AbstractCodeQueue<RequestInfo> {

    private int maxTimes = 0;

    public RequestLimitAspect() {}

    public boolean insert(RequestInfo info) {
        findExpired(RequestInfo -> System.currentTimeMillis() - RequestInfo.getExpire() >= (180 * 1000));
        for(RequestInfo requestInfo : queue) {
            if(requestInfo.getAddress().equals(info.getAddress())) {
                requestInfo.addTimes();
            }
            if(requestInfo.getTimes() >= maxTimes) {
                return false;
            }
        }
        add(info);

        return true;
    }

    @Pointcut("@annotation(oauth2.provider.annotation.request.RequestLimit)")
    private void requestLimit() {
        super.maxCapacity = 500;
    }

    @Around("requestLimit()")
    public Object doLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        RequestLimit limit = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(RequestLimit.class);
        this.maxTimes = limit.maxTimes();

        String address = request.getRemoteAddr();
        if(insert(new RequestInfo(address, 0))) {
            return joinPoint.proceed();
        }

        throw new RuntimeException("Request heavy");
    }

    @Scheduled(cron = "0/50 * *  * * ? ")
    public void execute() {
        findExpired(RequestInfo -> System.currentTimeMillis() - RequestInfo.getExpire() >= (180 * 1000));
    }
}
