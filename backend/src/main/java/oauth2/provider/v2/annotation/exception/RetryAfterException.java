package oauth2.provider.v2.annotation.exception;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryAfterException {
    int times() default 3;
    int interval() default 1;
}
