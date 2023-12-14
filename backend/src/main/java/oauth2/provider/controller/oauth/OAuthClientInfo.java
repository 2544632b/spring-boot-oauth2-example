package oauth2.provider.controller.oauth;

import oauth2.provider.model.form.response.oauth.OAuthClientInfoResp;
import oauth2.provider.model.user.info.entity.OAuthEntity;
import oauth2.provider.service.oauth.server.entity.OAuthEntityService;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OAuthClientInfo {

    @Resource
    private OAuthEntityService oauthEntity;

    @RequestMapping(value = "/oauth/info/{cid}", method = RequestMethod.POST)
    public Object getInfo(@PathVariable("cid") String cid) throws Exception {
        OAuthEntity oauthInfo = oauthEntity.findByClientId(StringEscapeUtils.escapeJava(cid));

        String[] scopes = oauthInfo.getScope().split(",");
        List<String> info = new ArrayList<>();
        for(String str : scopes) {
            if(str.equals("profile")) {
                info.add("Profile (except password)");
            }
            if(str.equals("email")) {
                info.add("Email");
            }
            if(str.equals("openid")) {
                info.add("User authentication (Open ID Connect)");
            }
        }
        return new OAuthClientInfoResp(oauthInfo.getClientName(), info);
    }

}
