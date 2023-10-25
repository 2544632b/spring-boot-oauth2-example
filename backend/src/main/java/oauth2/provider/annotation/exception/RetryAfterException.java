package oauth2.provider.annotation.exception;

import java.lang.annotation.*;

/**
 * <p>Retry annotation</p>
 * <p>Re-execute a function after exception.</p>
 *
 * @since 2.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryAfterException {
    int times() default 3;
    int interval() default 1;
}
