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

import org.codehaus.jettison.json.JSONObject;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface UserRepository {
    /**
     * Check if a user exist
     */
    public boolean exist(String user);

    /**
     * Add a user, returns false if already exist and not added
     */
    public boolean create(String user);

    /**
     * Remove a user and returns true if existed
     */
    public boolean remove(String user);

    public List<JSONObject> users();

    JSONObject get(String user);

    void setStatus(String user, String status);
}
