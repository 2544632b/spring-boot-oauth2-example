package oauth2.provider.model.form.response.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;

@JsonSerialize
public class OAuthClientInfoResp implements Serializable {
    @JsonProperty("clientName")
    private String clientName;
    @JsonProperty("clientGets")
    private List<String> clientGets;

    @JsonCreator
    public OAuthClientInfoResp(String clientName, List<String> clientGets) {
        this.clientName = clientName;
        this.clientGets = clientGets;
    }
}
