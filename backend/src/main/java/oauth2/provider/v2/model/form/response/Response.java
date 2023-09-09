package oauth2.provider.v2.model.form.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.http.HttpStatus;

import java.io.Serializable;

@JsonSerialize
public class Response implements Serializable {
    public int statusCode;
    public String reason;

    @JsonCreator
    public Response(int statusCode, String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
    }

    public static ResponsePreview responseForbidden(String content) {
        return new ResponsePreview(HttpStatus.SC_FORBIDDEN, content);
    }

    public static ResponsePreview response(String content) {
        return new ResponsePreview(HttpStatus.SC_OK, content);
    }

    public static ResponsePreview responseNotAuthorized(String content) {
        return new ResponsePreview(HttpStatus.SC_UNAUTHORIZED, content);
    }

    public static ResponsePreview responseResultNotFound(String content) {
        return new ResponsePreview(HttpStatus.SC_NOT_FOUND, content);
    }

    public static ResponsePreview responseRedirected(String url) {
        return new ResponsePreview(HttpStatus.SC_TEMPORARY_REDIRECT, url);
    }

    public static ResponsePreview responseError(String content) {
        return new ResponsePreview(HttpStatus.SC_INTERNAL_SERVER_ERROR, content);
    }

    // ...

    public int getStatusCode() {
        return statusCode;
    }
    public String getReason() {
        return reason;
    }
}
