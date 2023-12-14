package oauth2.provider.service.oauth.server.entity;

import oauth2.provider.model.user.info.entity.OAuthEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class OAuthEntityServiceImpl implements OAuthEntityService {
    @Resource
    private oauth2.provider.db.repository.OAuthRepository OAuthRepository;

    @Override
    public OAuthEntity findByClientId(String clientId) {
        return OAuthRepository.findByClientId(clientId);
    }
}
