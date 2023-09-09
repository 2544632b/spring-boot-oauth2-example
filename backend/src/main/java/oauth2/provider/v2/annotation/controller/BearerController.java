package oauth2.provider.v2.annotation.controller;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

// They are no any features provided for these controllers right now.
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface BearerController {    // None different with "@RestController"
    @AliasFor(annotation = Controller.class)
    String value() default "";
}
