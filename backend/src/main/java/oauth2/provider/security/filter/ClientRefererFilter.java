package oauth2.provider.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oauth2.provider.model.form.response.Response;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ClientRefererFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<String> urls = List.of("/oauth/client/continue/github");
        for(String s : urls) {
            if(request.getRequestURI().equals(s)) {
                String referer = request.getHeader("Referer");
                if(referer.equals("http://localhost/")) {
                    break;
                }

                OutputStream os = response.getOutputStream();
                os.write(new ObjectMapper().writeValueAsString(Response.responseForbidden("Wrong referer, are you using the website?")).getBytes());
                os.flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
