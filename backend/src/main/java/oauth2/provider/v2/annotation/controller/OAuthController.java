package oauth2.provider.v2.annotation.controller;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * <p>OAuth controller tag</p>
 * <p>They are no any features provided for these controllers right now.</p>
 *
 * @since 2.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface OAuthController {
    @AliasFor(annotation = Controller.class)
    String value() default "";
}
