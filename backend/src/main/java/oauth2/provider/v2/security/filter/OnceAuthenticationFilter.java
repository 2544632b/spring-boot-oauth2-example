package oauth2.provider.v2.security.filter;

import com.alibaba.fastjson2.JSONObject;
import oauth2.provider.v2.authentication.token.UsernamePasswordAuthenticationToken;
import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.service.base.user.UserEntityService;
import oauth2.provider.v2.util.AESUtil;
import oauth2.provider.v2.util.JSONWebToken;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;


public class OnceAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private AESUtil AES;
    @Resource
    private JSONWebToken JWT;
    @Resource
    private UserEntityService userEntity;
    private static final Logger logger = LoggerFactory.getLogger(OnceAuthenticationFilter.class);

    /***
     * @param request
     * Request container from java servlet
     *
     * @param response
     * Response container from java servlet
     *
     * @param filterChain
     * Filter rules from MainSecurityConfig.java
     *
     * @throws ServletException
     * Servlet exceptions
     *
     * @throws IOException
     * Network/IO exceptions
     *
     * EncryptedToken(Header: X-Access-Token) → DecryptedToken → username → authenticateManager → doFilter
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Header settings
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Powered-By", "");

        response.setHeader("Set-Cookie", "HttpOnly;Secure;SameSite=None");

        final String encryptedToken = request.getHeader("X-Access-Token");
        String userToken;
        String decryptedToken = null;

        List<String> urls = List.of(
                "/login",
                "/register",
                "/register/verify",
                "/user/forgot/password/email",
                "/user/forgot/password/email/verify",
                "/oauth/token",
                "/oauth/client/continue/github",

                // Different list with bearer filter
                "/default/user/info"
        );

        // Default JSON Web Token settings
        if(encryptedToken != null) {
            try {
                decryptedToken = AES.decrypt(encryptedToken);
                userToken = JWT.getKeywordsFromToken(decryptedToken); // Text security here

                if(userToken != null) {
                    if(JWT.validateToken(decryptedToken, userToken)) {
                        JSONObject userjson = JSONObject.parseObject(userToken);

                        // Build the user session
                        UserEntity user = new UserEntity(
                                userjson.getString("email"),
                                userjson.getString("username"),
                                userjson.getString("login")
                        );

                        logger.info("{} -> {} passed {}, last login: {}.",
                                userjson.getString("email"),
                                userjson.getString("username"),
                                OnceAuthenticationFilter.class,
                                userjson.getString("login")
                        );

                        if(!userEntity.findByEmail(userjson.getString("email")).isEnabled()
                                || !userEntity.findByUsername(userjson.getString("username")).isEnabled()
                                || userEntity.findByUsername(userjson.getString("username")) == null
                        ) {
                            logger.info("{} -> {} was disabled, please contact with administrator.", userjson.getString("email"), userjson.getString("username"));
                            response.setStatus(403);
                            return;
                        }

                        // userEntity.findByUsername().getAuthorities() -> user.getAuthorities();
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        // Build end
                    }
                }
            } catch(IllegalArgumentException e) {
                logger.info("Unable to get the token");
                response.setStatus(401);
                return;
            } catch(ExpiredJwtException e) {
                logger.info("Expired token {}", encryptedToken);
                response.setStatus(401);
                return;
            } catch(Exception e) {
                e.printStackTrace();
                logger.info("Can not validate some request {}", encryptedToken);
                response.setStatus(401);
                return;
            }

            filterChain.doFilter(request, response);
        }
        else {
            for(String url : urls) {
                if (request.getRequestURI().equals(url)) {
                    filterChain.doFilter(request, response);
                }
            }

            response.setStatus(401);
        }
    }
}
