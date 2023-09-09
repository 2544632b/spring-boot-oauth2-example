package oauth2.provider.v2.model.user.info.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "OauthClients")
@EntityListeners(AuditingEntityListener.class)
public class OAuthEntity {
    @Id
    @Column(name = "ClientId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String clientId;

    @Column(name = "ClientSecret")
    private String clientSecret;

    @Column(name = "Scope")
    private String scope;

    @Column(name = "RedirectUri")
    private String redirectURI;

    @Column(name = "ClientName")
    private String clientName;

    public OAuthEntity() {}

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public String getClientName() { return clientName; }
}
