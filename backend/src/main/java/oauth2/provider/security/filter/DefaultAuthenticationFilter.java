package oauth2.provider.security.filter;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import oauth2.provider.authentication.token.UsernamePasswordAuthenticationToken;
import oauth2.provider.model.form.response.Response;
import oauth2.provider.model.user.info.entity.UserEntity;
import oauth2.provider.service.base.user.UserEntityService;
import oauth2.provider.util.aes.AESUtil;
import oauth2.provider.util.jwt.JSONWebToken;
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
import java.io.OutputStream;
import java.util.List;


public class DefaultAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private AESUtil AES;
    @Resource
    private JSONWebToken JWT;
    @Resource
    private UserEntityService userEntity;
    private static final Logger logger = LoggerFactory.getLogger(DefaultAuthenticationFilter.class);

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
        String userToken, decryptedToken;
        OutputStream os = response.getOutputStream();

        List<String> urls = List.of(
                "/login",
                "/register",
                "/register/verify",
                "/user/forgot/password/email",
                "/user/forgot/password/email/verify",

                "/oauth/token", // Server side access, see https://www.rfc-editor.org/rfc/rfc6749.html
                "/oauth/client/continue/github", // -> "/login" (Publicly request)

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

                        /*
                        logger.info("{} -> {} passed {}, last login: {}.",
                                userjson.getString("email"),
                                userjson.getString("username"),
                                DefaultAuthenticationFilter.class,
                                userjson.getString("login")
                        );
                        */

                        if(!userEntity.findByEmail(userjson.getString("email")).isEnabled()
                                || !userEntity.findByUsername(userjson.getString("username")).isEnabled()
                                || userEntity.findByUsername(userjson.getString("username")) == null
                        ) {
                            logger.info("{} -> {} was disabled, please contact with administrator.", userjson.getString("email"), userjson.getString("username"));
                            response.setStatus(403);
                            os.write(new ObjectMapper().writeValueAsString(Response.responseForbidden(userjson.get("email") + " was disabled, please contact with administrator.")).getBytes());
                            os.flush();
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
                os.write(new ObjectMapper().writeValueAsString(Response.responseNotAuthorized("Not Authorized")).getBytes());
                os.flush();
                return;
            } catch(ExpiredJwtException e) {
                logger.info("Expired token {}", encryptedToken);
                response.setStatus(401);
                os.write(new ObjectMapper().writeValueAsString(Response.responseNotAuthorized("Not Authorized")).getBytes());
                os.flush();
                return;
            } catch(Exception e) {
                e.printStackTrace();
                logger.info("Can not validate some request {}", encryptedToken);
                response.setStatus(401);
                os.write(new ObjectMapper().writeValueAsString(Response.responseNotAuthorized("Not Authorized")).getBytes());
                os.flush();
                return;
            }

            filterChain.doFilter(request, response);
        }
        else {
            for(String url : urls) {
                if (request.getRequestURI().equals(url)) {
                    filterChain.doFilter(request, response);
                    return; // Quit
                }
            }

            response.setStatus(401);
            os.write(new ObjectMapper().writeValueAsString(Response.responseNotAuthorized("Not Authorized")).getBytes());
            os.flush();
        }
    }
}
