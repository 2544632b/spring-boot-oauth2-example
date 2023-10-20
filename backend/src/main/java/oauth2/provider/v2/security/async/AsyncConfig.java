package oauth2.provider.v2.security.async;


import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());

        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 5);

        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors() * 2);

        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor exe) -> {
            System.out.println("Task full");
        });

        executor.setThreadNamePrefix("sub-executor-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable e, Method m, Object... params) -> {
            System.out.println("class#method: " + m.getDeclaringClass().getName() + "#" + m.getName());
            System.out.println("Type: " + e.getClass().getName());
            System.out.println("Exception: " + e.getMessage());
        };
    }
}
