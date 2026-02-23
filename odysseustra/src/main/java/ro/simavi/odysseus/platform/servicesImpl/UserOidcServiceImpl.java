package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.services.UserOidcService;

@Service
public class UserOidcServiceImpl implements UserOidcService {

    @Override
    public DefaultOidcUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            return (DefaultOidcUser) oauthToken.getPrincipal();  // Retrieves user info
        }

        return null;
    }

    @Override
    public String getCurrentUserEmail() {
        DefaultOidcUser user = getCurrentUser();
        return user != null ? user.getUserInfo().getEmail() : null;
    }


    @Override
    public String getCurrentUserSub() {
        DefaultOidcUser user = getCurrentUser();
        return user != null ? user.getSubject() : null;
    }

}
