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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MemoryUserRepository implements UserRepository {

    private final ConcurrentMap<String, JSONObject> users = new ConcurrentHashMap<String, JSONObject>();

    @Override
    public boolean create(String user) {
        try {
            return users.putIfAbsent(user, new JSONObject().put("name", user).put("status", "online")) == null;
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean exist(String user) {
        return users.containsKey(user);
    }

    @Override
    public boolean remove(String user) {
        return users.remove(user) != null;
    }

    @Override
    public List<JSONObject> users() {
        List<JSONObject> list = new ArrayList<JSONObject>(users.values());
        Collections.sort(list, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                try {
                    return o1.getString("name").compareTo(o2.getString("name"));
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        });
        return list;
    }

    @Override
    public JSONObject get(String user) {
        JSONObject o = users.get(user);
        if (o == null) {
            throw new IllegalArgumentException("User not found: " + user);
        }
        return o;
    }

}
