package oauth2.provider.v2.service.base.oauth;

import oauth2.provider.v2.model.user.info.entity.OAuthEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class OAuthEntityServiceImpl implements OAuthEntityService {
    @Resource
    private oauth2.provider.v2.user.repository.OAuthRepository OAuthRepository;

    @Override
    public OAuthEntity findByClientId(String clientId) {
        return OAuthRepository.findByClientId(clientId);
    }
}
