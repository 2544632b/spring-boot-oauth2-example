package oauth2.provider.v2.controller.oauth;

import oauth2.provider.v2.model.form.response.oauth.OAuthClientInfoResp;
import oauth2.provider.v2.model.user.info.entity.OAuthEntity;
import oauth2.provider.v2.service.base.oauth.OAuthEntityService;
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
        List<String> finalInfo = new ArrayList<>();
        for(String str : scopes) {
            if(str.equals("profile")) {
                finalInfo.add("Profile(except password)");
            }
            if(str.equals("email")) {
                finalInfo.add("Email");
            }
        }
        return new OAuthClientInfoResp(oauthInfo.getClientName(), finalInfo);
    }
}
