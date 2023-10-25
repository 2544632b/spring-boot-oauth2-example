package oauth2.provider.util.jwt.factory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.function.Function;

public abstract class AbstractJSONWebToken {
    protected long JSON_WEB_TOKEN_VALIDITY;
    protected String secret;
    protected SignatureAlgorithm algorithm;

    public String getKeywordsFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String keywords) {
        Claims claims = Jwts.claims();
        return doGenerateToken(claims, keywords);
    }

    public String doGenerateToken(Claims claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JSON_WEB_TOKEN_VALIDITY * 1000))
                .signWith(algorithm, secret).compact();
    }

    public Boolean validateToken(String token, String keywords) {
        final String target_key = getKeywordsFromToken(token);
        return (target_key.equals(keywords) && !isTokenExpired(token));
    }
}
