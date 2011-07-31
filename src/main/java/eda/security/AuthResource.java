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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Path("security")
public final class AuthResource {

    @Inject
    AuthManager authManager;

    @Inject
    UserRepository userRepository;

    @POST
    @Path("login")
    public JSONObject login(@FormParam("user") String user) throws JSONException {
        if (userRepository.add(user)) {
            authManager.login(user);
            return new JSONObject().put("user", authManager.user());
        } else {
            return new JSONObject().put("error", "User already exist !");
        }
    }

    @POST
    @Path("logout")
    public void logout() throws JSONException {
        if (authManager.isLogged()) {
            userRepository.remove(authManager.user());
            authManager.logout();
        }
    }
}
