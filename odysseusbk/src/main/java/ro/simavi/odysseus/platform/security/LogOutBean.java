package ro.simavi.odysseus.platform.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

@Component
@ApplicationScope
public class LogOutBean {
    @Value("${server.servlet.context-path}")
    private String contextPath;

    public void logOut() {
        try {
            var request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.getSession().invalidate();
            request.logout();
        } catch (Exception e) {
            Logger.getLogger("LogOutBean").warning(e.getMessage());
        } finally {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/index.xhtml");
            } catch (IOException e) {
                Logger.getLogger("LogOutBean").severe("Error redirecting to index"+ e.getMessage());
            }
        }
    }

    public void logIn() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/index.xhtml");
        } catch (IOException e) {
            Logger.getLogger("LogOutBean").severe("Error redirecting to index"+e.getMessage());
        }
    }
}