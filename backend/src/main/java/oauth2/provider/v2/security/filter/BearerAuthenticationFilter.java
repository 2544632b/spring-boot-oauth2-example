package oauth2.provider.v2.security.filter;

import oauth2.provider.v2.authentication.token.UsernamePasswordAuthenticationToken;
import oauth2.provider.v2.model.user.info.entity.UserEntity;
import oauth2.provider.v2.service.base.oauth.OAuthEntityService;
import oauth2.provider.v2.service.base.user.UserEntityService;
import oauth2.provider.v2.service.oauth.server.OAuthSessionInfoService;
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
import java.util.Map;


public class BearerAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private OAuthSessionInfoService oauthSessionInfoService;

    @Resource
    private UserEntityService userEntity;

    private static final Logger logger = LoggerFactory.getLogger(BearerAuthenticationFilter.class);
    /***
     * Extends from OnceAuthenticationFilter.class
     *
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
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Powered-By", "");

        response.setHeader("Set-Cookie", "HttpOnly;Secure;SameSite=None");

        // URI
        List<String> urls = List.of(
                "/default/user/info"
        );

        // Scope
        Map<String, String> url2Scope = Map.of(
                "/default/user/info", "profile"
        );

        // OAuth 2.0 Token
        final String userBearer = request.getHeader("Authorization");

        // had bearer
        if(userBearer != null) {
            String[] type = userBearer.split(" ");
            if(type[0].equals("Bearer")) {
                try {
                    UserEntity user = oauthSessionInfoService.getUserEntityFromBearer(type[1]);
                    String[] scope = oauthSessionInfoService.getScope(type[1]).split(",");

                    if(!userEntity.findByEmail(user.getEmail()).isEnabled()
                            || !userEntity.findByUsername(user.getUsername()).isEnabled()
                    ) {
                        logger.info("{} -> {} was disabled, please contact with administrator.", user.getEmail(), user.getUsername());
                        response.setStatus(403);
                        return;
                    }

                    String targetScope = (url2Scope.get(request.getRequestURI()) == null) ? null : url2Scope.get(request.getRequestURI());
                    if(targetScope == null) {
                        response.setStatus(403);
                        return;
                    }
                    boolean match = false;

                    for(String s : scope) {
                        if (s.equals(targetScope)) {
                            match = true;
                            break;
                        }
                    }
                    if(!match) {
                        response.setStatus(403);
                        return;
                    }

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                } catch(Exception e) {
                    logger.info("Can not validate the bearer token " + type[1] + ".");
                    response.setStatus(401);
                    return;
                }
            }

            boolean match = false;
            for(String url : urls) {
                if (request.getRequestURI().equals(url)) {
                    match = true;
                }
            }

            if(!match) {    // Not resource url
                response.setStatus(401);
            } else {        // Is resource url
                filterChain.doFilter(request, response);
            }
        }

        // No bearer
        else {
            boolean match = false;
            for(String url : urls) {
                if (request.getRequestURI().equals(url)) {
                    match = true;
                }
            }

            if(!match) {        // Not resource url
                filterChain.doFilter(request, response);
            } else {            // Is resource url
                response.setStatus(401);
            }
        }
    }
}