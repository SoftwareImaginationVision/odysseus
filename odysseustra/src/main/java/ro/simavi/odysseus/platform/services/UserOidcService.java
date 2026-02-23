package ro.simavi.odysseus.platform.services;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

@Service
public interface UserOidcService {
    DefaultOidcUser getCurrentUser();

    String getCurrentUserEmail();

    String getCurrentUserSub();

}
