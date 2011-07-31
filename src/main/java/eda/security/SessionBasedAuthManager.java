/**
 * Copyright (C) 2011 Ovea <dev@ovea.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eda.security;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SessionBasedAuthManager implements AuthManager {

    @Inject
    Provider<HttpServletRequest> req;

    @Override
    public boolean isLogged() {
        HttpSession session = session();
        return session != null && session.getAttribute("user") != null;
    }

    @Override
    public String user() {
        HttpSession session = session();
        String user;
        if (session == null || (user = (String) session.getAttribute("user")) == null) {
            throw new IllegalStateException("User not logged !");
        }
        return user;
    }

    @Override
    public void login(String user) {
        req.get().getSession().setAttribute("user", user);
    }

    @Override
    public void logout() {
        HttpSession session = session();
        if (session != null) {
            session.removeAttribute("user");
            session.invalidate();
        }
    }

    private HttpSession session() {
        return req.get().getSession(false);
    }
}
