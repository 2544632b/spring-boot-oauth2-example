package oauth2.provider.service.base.oauth;

import oauth2.provider.model.user.info.entity.OAuthEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class OAuthEntityServiceImpl implements OAuthEntityService {
    @Resource
    private oauth2.provider.user.repository.OAuthRepository OAuthRepository;

    @Override
    public OAuthEntity findByClientId(String clientId) {
        return OAuthRepository.findByClientId(clientId);
    }
}
