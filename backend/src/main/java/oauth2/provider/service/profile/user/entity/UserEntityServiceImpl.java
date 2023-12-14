package oauth2.provider.service.profile.user.entity;

import oauth2.provider.db.repository.UserRepository;
import oauth2.provider.model.user.info.entity.UserEntity;
import jakarta.annotation.Resource;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class UserEntityServiceImpl implements UserEntityService {
    @Resource
    private UserRepository dataTable;

    @Override
    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    public void insertUser(UserEntity userInfo) {
        dataTable.save(userInfo);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        // Noting to do with
        //
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void updatePassword(String keywords, String password) {
        dataTable.updatePassword(keywords, password);
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)       // may be unusable
    public void updateLastLoginIp(String keywords, String ip) {
        dataTable.updateLoginIp(keywords, ip);
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)        //
    @Lazy
    public UserEntity findByKeywords(String keywords) {
        return dataTable.findByKeywords(keywords);
    }

    @Override
    @Lock(LockModeType.OPTIMISTIC)
    @Lazy
    public UserEntity findByGithubId(BigInteger id) {
        return dataTable.findByGithubId(id);
    }

    @Override
    @Lock(LockModeType.OPTIMISTIC)              //
    public UserEntity findByUsername(String username) {
        return dataTable.findByUsername(username);
    }

    @Override
    @Lock(LockModeType.OPTIMISTIC)              //
    public UserEntity findByEmail(String email) {
        return dataTable.findByEmail(email);
    }
    
    @Override
    @Lock(LockModeType.OPTIMISTIC)
    public List<UserEntity> findAll() {
        return dataTable.findAll();
    }
}
