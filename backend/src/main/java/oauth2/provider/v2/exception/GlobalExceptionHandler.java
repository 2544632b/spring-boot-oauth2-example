package oauth2.provider.v2.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import oauth2.provider.v2.model.form.response.Response;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Resource;
import oauth2.provider.v2.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Resource
    private DateUtil DateUtil;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object exceptionResponse(Exception e) throws JsonProcessingException {
        Date date = new Date(System.currentTimeMillis());
        String date2 = DateUtil.format(date);

        Map<String, Object> format = new HashMap<>();
        format.put("uuid", UUID.randomUUID().toString());
        format.put("date", date2);
        format.put("situation", e.getMessage());
        format.put("localized", e.getLocalizedMessage());
        format.put("class", e.getClass().getMethods());
        format.put("method", e.getClass().getMethods());
        format.put("stack", e.getStackTrace());

        logger.warn("{}", format);

        format.clear();

        e.printStackTrace();

        return Response.responseError("Internal Error");
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public Object nullPointerResponse(NullPointerException n) {
        n.printStackTrace();
        return Response.responseResultNotFound("Mismatch information");
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseBody
    public Object userNotFoundResponse(UsernameNotFoundException u) {
        return Response.responseResultNotFound("Invalid user or password \uD83D\uDE12");
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseBody
    public Object badCredentialsResponse(BadCredentialsException b) {
        return Response.responseNotAuthorized("Invalid user or password \uD83D\uDE12");
    }

    @ExceptionHandler(value = DisabledException.class)
    @ResponseBody
    public Object disabledExceptionResponse(DisabledException d) {
        return Response.responseForbidden("Invalid user or password \uD83D\uDE12");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public Object illegalArgumentResponse(IllegalArgumentException i) {
        return Response.responseNotAuthorized("Session expired");
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    @ResponseBody
    public Object expiredJwtResponse(ExpiredJwtException e) {
        return Response.responseNotAuthorized("Session expired");
    }
}
