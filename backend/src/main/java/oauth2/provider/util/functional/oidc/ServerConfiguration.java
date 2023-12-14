package oauth2.provider.util.functional.oidc;

import oauth2.provider.util.functional.FunctionalTreeMap;

import java.util.List;
import java.util.TreeMap;

public class ServerConfiguration {

    private final FunctionalTreeMap<String, Object> treeMap;

    public ServerConfiguration() {
        treeMap = new FunctionalTreeMap<String, Object>();
    }

    public ServerConfiguration setIssuer(String url) {
        treeMap.putEx("issuer", url);
        return this;
    }

    public ServerConfiguration setJwk(String url) {
        treeMap.putEx("jwks_uri", url);
        return this;
    }

    public ServerConfiguration setAuthorizationEndpoint(String url) {
        treeMap.putEx("authorization_endpoint", url);
        return this;
    }

    public ServerConfiguration setTokenEndpoint(String url) {
        treeMap.putEx("token_endpoint", url);
        return this;
    }

    public ServerConfiguration setUserInfoEndpoint(String url) {
        treeMap.putEx("userinfo_endpoint", url);
        return this;
    }

    public ServerConfiguration setScopesSupported(List<String> scopes) {
        treeMap.putEx("scopes_supported", scopes);
        return this;
    }

    public ServerConfiguration setClaimsSupported(List<String> claims) {
        treeMap.putEx("claims_supported", claims);
        return this;
    }

    public ServerConfiguration setGrantTypeSupported(List<String> grants) {
        treeMap.putEx("grant_types_supported", grants);
        return this;
    }

    public ServerConfiguration setAuthorizationGrant() {
        return setGrantTypeSupported(List.of("authorization_code"));
    }

    public ServerConfiguration setResponseTypeSupported(List<String> type) {
        treeMap.putEx("response_types_supported", type);
        return this;
    }

    public ServerConfiguration setCodeResponseType() {
        return setResponseTypeSupported(List.of("code"));
    }

    public ServerConfiguration setResponseModeSupported(List<String> mode) {
        treeMap.putEx("response_mode_supported", mode);
        return this;
    }

    public ServerConfiguration setResponseFormPostMode() {
        return setResponseModeSupported(List.of("form_post"));
    }

    public ServerConfiguration setTokenEndpointAuthMethodSupported(List<String> method) {
        treeMap.putEx("token_endpoint_auth_methods_supported", method);
        return this;
    }

    public ServerConfiguration setTokenClientSecretPost() {
        return setTokenEndpointAuthMethodSupported(List.of("client_secret_post"));
    }

    public ServerConfiguration setIdTokenSigningAlgValSupported(List<String> alg) {
        treeMap.putEx("id_token_signing_alg_values_supported", alg);
        return this;
    }

    public ServerConfiguration setIdTokenRS256AlgSupported() {
        return setIdTokenSigningAlgValSupported(List.of("RS256"));
    }

    public ServerConfiguration setSubjectTypesSupported(List<String> typesSupported) {
        treeMap.putEx("subject_types_supported", typesSupported);
        return  this;
    }

    public ServerConfiguration setSubjectPublic() {
        return setSubjectTypesSupported(List.of("public"));
    }

    public ServerConfiguration setCodeChallengeMethodsSupported(List<String> method) {
        treeMap.putEx("code_challenge_methods_supported", method);
        return this;
    }

    public ServerConfiguration setPlainCodeChallenge() {
        return setCodeChallengeMethodsSupported(List.of("plain"));
    }

    public TreeMap<String, Object> finish() {
        return treeMap.finish();
    }

}
