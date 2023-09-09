package oauth2.provider.v2.security.async;


import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());

        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 5);

        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors() * 2);

        executor.setThreadNamePrefix("this-excutor-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable ex, Method m, Object... params) -> {
            //System.out.println("class#method: " + m.getDeclaringClass().getName() + "#" + m.getName());
            //System.out.println("Type: " + ex.getClass().getName());
            //System.out.println("Exception: " + ex.getMessage());
        };
    }
}
