package oauth2.provider.controller.user;

import oauth2.provider.model.form.request.user.change.PasswordChangeForm;
import oauth2.provider.model.form.response.Response;
import jakarta.annotation.Resource;
import oauth2.provider.service.authentication.UserAuthenticateService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserChange {

    @Resource
    private UserAuthenticateService UserAuthenticateService;
    @Resource
    private oauth2.provider.service.profile.user.change.ChangeUserDetailsService ChangeUserDetailsService;

    @RequestMapping(value = "/user/change/password", method = RequestMethod.POST)
    @ResponseBody
    public Object changePassword(@Validated @RequestBody PasswordChangeForm PasswordChangeForm) throws BadCredentialsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(UserAuthenticateService.attemptLogin(username, PasswordChangeForm.getPassword())) {
            if(!PasswordChangeForm.getNewPassword().isEmpty() || PasswordChangeForm.getPassword().equals(PasswordChangeForm.getNewPassword())) {
                if(ChangeUserDetailsService.changePassword(username, PasswordChangeForm.getNewPassword())) {
                    return Response.response("Update success");
                }
            }
        }

        throw new BadCredentialsException("Invalid credential");
    }
}
