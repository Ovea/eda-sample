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
package eda.web;

import eda.AuthManager;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SecurityFilter implements Filter {

    private final AuthManager authManager;
    private Pattern[] excludes = new Pattern[]{
        Pattern.compile("/login\\.html"),
        Pattern.compile("/static/.*")
    };

    @Inject
    public SecurityFilter(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();
        for (Pattern exclude : excludes) {
            if (exclude.matcher(path).matches()) {
                chain.doFilter(req, res);
                return;
            }
        }
        if (!authManager.isLogged()) {
            if ("XMLHttpRequest".equalsIgnoreCase(request.getParameter("X-Requested-With"))) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else {
                res.sendRedirect("login.html");
            }
        }
    }
}
