package oauth2.provider.util.functional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

public class FunctionalHttpHeaders extends HttpHeaders {

    public FunctionalHttpHeaders setContentTypeEx(MediaType mediaType) {
        super.setContentType(mediaType);
        return this;
    }

    public FunctionalHttpHeaders setBearerAuthEx(String bearer) {
        super.setBearerAuth(bearer);
        return this;
    }

    public FunctionalHttpHeaders setEx(String headerName, @Nullable String headerValue) {
        super.set(headerName, headerValue);
        return this;
    }

    public HttpHeaders finish() {
        return this;
    }

}
