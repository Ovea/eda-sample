package eda.security;

import com.google.inject.Injector;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SecuritySessionListener implements HttpSessionListener, ServletContextListener {

    @Inject
    UserRepository userRepository;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Injector injector = (Injector) sce.getServletContext().getAttribute(Injector.class.getName());
        injector.injectMembers(this);
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String user = (String) se.getSession().getAttribute("user");
        if (user != null) {
            userRepository.remove(user);
        }
    }
}
