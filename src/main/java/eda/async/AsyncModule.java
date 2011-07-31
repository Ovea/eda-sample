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

import com.google.inject.AbstractModule;
import com.ovea.cometd.guice.GuiceCometdServlet;
import org.cometd.server.ext.AcknowledgedMessagesExtension;

import javax.inject.Singleton;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AsyncModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new CometdModule());
        bind(GuiceCometdServlet.class).in(Singleton.class);
        bind(AsyncService.class).in(Singleton.class);
        bind(AcknowledgedMessagesExtension.class).in(Singleton.class);
    }
}
