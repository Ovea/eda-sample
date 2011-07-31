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
package eda.cometd;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.java.annotation.Configure;
import org.cometd.java.annotation.Service;
import org.cometd.server.authorizer.GrantAuthorizer;

import javax.inject.Inject;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Service
public final class AsyncService {

    @Inject
    BayeuxServer server;

    @Configure("/**")
    void any(ConfigurableServerChannel channel) {
        channel.addAuthorizer(GrantAuthorizer.GRANT_NONE);
    }

    
    /*@PostConstruct
    void init() {
        server.addListener(new BayeuxServer.SessionListener() {
            @Override
            public void sessionAdded(ServerSession session) {
                session.setAttribute("user", server.getContext().getHttpSessionAttribute("user"));
                server.getChannel("/chatroom").publish(session, "connected !", null);
            }

            @Override
            public void sessionRemoved(ServerSession session, boolean timedout) {
                server.getChannel("/chatroom").publish(session, "disconnected !", null);
                session.removeAttribute("user");
            }
        });
    }

    @Configure("/chatroom")
    void configure(ConfigurableServerChannel channel) {
        channel.addAuthorizer(new Authorizer() {
            @Override
            public Result authorize(Operation operation, ChannelId channel, ServerSession session, ServerMessage message) {
                return session.getAttribute("user") != null ? Result.grant() : Result.deny("no user in session");
            }
        });
    }

    @Listener("/chatroom")
    void appendUser(ServerSession remote, ServerMessage.Mutable message) {
        message.setData("[" + remote.getAttribute("user") + "] " + message.getData());
    }*/

}
