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
package eda.async;

import eda.security.AuthManager;
import eda.security.UserRepository;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.cometd.bayeux.ChannelId;
import org.cometd.bayeux.server.*;
import org.cometd.java.annotation.Configure;
import org.cometd.java.annotation.Listener;
import org.cometd.java.annotation.Service;
import org.cometd.java.annotation.Session;
import org.cometd.server.authorizer.GrantAuthorizer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Service
public final class AsyncService {

    @Inject
    BayeuxServer server;

    @Inject
    UserRepository userRepository;

    @Session
    LocalSession localSession;

    @Inject
    AuthManager authManager;

    private final Authorizer loggedUserRequired = new Authorizer() {
        @Override
        public Result authorize(Operation operation, ChannelId channel, ServerSession session, ServerMessage message) {
            try {
                return server.getContext().getHttpSessionAttribute("user") == null ? Result.deny("User not logged !") : Result.grant();
            } catch (IllegalStateException e) {
                session.disconnect();
                return Result.deny("User not logged !");
            }
        }
    };

    @PostConstruct
    void init() {
        server.addListener(new BayeuxServer.SessionListener() {
            @Override
            public void sessionAdded(ServerSession session) {
                server.getContext().setHttpSessionAttribute("cometd", session.getId());
            }

            @Override
            public void sessionRemoved(ServerSession session, boolean timedout) {
            }
        });
    }

    @Configure("/**")
    void any(ConfigurableServerChannel channel) {
        channel.addAuthorizer(GrantAuthorizer.GRANT_NONE);
    }

    @Configure({"/event/user/status/changed", "/event/chatroom/message"})
    void statuc(ConfigurableServerChannel channel) {
        channel.setPersistent(true);
        channel.addAuthorizer(loggedUserRequired);
    }

    @Configure({"/event/server/session/expired", "/event/user/connected", "/event/user/disconnected"})
    void server_events(ConfigurableServerChannel channel) {
        channel.setPersistent(true);
        channel.addAuthorizer(new Authorizer() {
            @Override
            public Result authorize(Operation operation, ChannelId channel, ServerSession session, ServerMessage message) {
                switch (operation) {
                    case PUBLISH:
                        return session.getLocalSession() == localSession ? Result.grant() : Result.deny("");
                }
                return loggedUserRequired.authorize(operation, channel, session, message);
            }
        });
    }

    @Listener("/event/user/status/changed")
    void status_changed(ServerSession remote, ServerMessage.Mutable message) {
        try {
            String user = (String) server.getContext().getHttpSessionAttribute("user");
            String status = new JSONObject((String) message.getData()).getString("status");
            userRepository.setStatus(user, status);
            message.setData(new JSONObject()
                .put("user", user)
                .put("status", status));
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Listener("/event/chatroom/message")
    void chatroom(ServerSession remote, ServerMessage.Mutable message) {
        try {
            String user = (String) server.getContext().getHttpSessionAttribute("user");
            String msg = new JSONObject((String) message.getData()).getString("msg");
            message.setData(new JSONObject()
                .put("user", user)
                .put("at", System.currentTimeMillis())
                .put("msg", msg));
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void onExpired(HttpSession httpSession) {
        ServerSession session = findCurrentSession(httpSession);
        if (session != null) {
            session.deliver(localSession, "/event/server/session/expired", new JSONObject(), null);
        }
        try {
            server.getChannel("/event/user/disconnected").publish(localSession, new JSONObject().put("user", httpSession.getAttribute("user")), null);
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void onLogout(HttpSession httpSession) {
        try {
            server.getChannel("/event/user/disconnected").publish(localSession, new JSONObject().put("user", authManager.user()), null);
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        ServerSession session = findCurrentSession(httpSession);
        if (session != null) {
            session.disconnect();
        }
    }

    public void onLogin() {
        server.getChannel("/event/user/connected").publish(localSession, userRepository.get(authManager.user()), null);
    }

    private ServerSession findCurrentSession(HttpSession httpSession) {
        String session = (String) httpSession.getAttribute("cometd");
        if (session != null) {
            return server.getSession(session);
        }
        return null;
    }
}
