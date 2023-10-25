package oauth2.provider.model.user.info.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.util.Collection;


// Username and Email are unique and without id, username should in `utf8-bin`.
@Entity // Hibernate
@Data   // Lombok
@Table(name = "Users")  // Hibernate
@EntityListeners(AuditingEntityListener.class)
public class UserEntity implements UserDetails {

    @Id
    @Column(name = "UserLogin")  // Hibernate
    private String username;     // Mybatis & Hibernate

    @Column(name = "UserPass")
    private String password;

    @Column(name = "UserEmail")
    private String email;

    @Column(name = "UserRegIp")
    private String regAddress;

    @Column(name = "UserLastLoginIp")
    private String lastLoginIp;

    @Column(name = "UserRegDate")
    private long registerDate;

    @Column(name = "UserStatus")
    private int userStatus;

    @Column(name = "UserTotpToken")
    private String userTotp;

    @Column(name = "GithubId")
    private BigInteger githubId;

    @Transient
    private String lastLoginDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    public UserEntity(String email, String username, String password, String regAddress, String userTotp) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.registerDate = System.currentTimeMillis();
        this.userStatus = 1;
        this.regAddress = regAddress;
        this.userTotp = userTotp;
    }

    /*
    * Create a constructor for session only
    */
    public UserEntity(String email, String username, String lastLoginDate) {
        this.email = email;
        this.username = username;
        this.userStatus = 1;    // Fake status
        this.lastLoginDate = lastLoginDate;
    }

    // 空构造保证可以返回
    public UserEntity() {}  // Real result from database

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return regAddress;
    }

    public long getRegisterDate() {
        return registerDate;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public String getUserTotp() {
        return userTotp;
    }

    public BigInteger getGithubId() {
        return githubId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userStatus == 1;
    }
}
