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
package eda.guice;

import com.google.common.collect.ImmutableMap;
import com.google.inject.servlet.ServletModule;
import com.ovea.cometd.guice.GuiceCometdServlet;
import com.ovea.resource.web.WebResourceServlet;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import eda.jersey.GzipEncoder;
import eda.web.SecurityFilter;

import javax.inject.Singleton;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class WebModule extends ServletModule {
    @Override
    protected void configureServlets() {
        bind(WebResourceServlet.class).in(Singleton.class);
        bind(SecurityFilter.class).in(Singleton.class);
        bind(GuiceCometdServlet.class).in(Singleton.class);

        filter("/*").through(SecurityFilter.class, ImmutableMap.of("exclude", ""));

        serve("/async/*").with(GuiceCometdServlet.class);

        serve("/static/*").with(WebResourceServlet.class, ImmutableMap.of(
            WebResourceServlet.DESCRIPTOR, "virtual-resources.xml",
            "debug", System.getProperty("debug.static", "false")));

        serve("/service/*").with(GuiceContainer.class, ImmutableMap.of(
            "com.sun.jersey.spi.container.ContainerResponseFilters", GzipEncoder.class.getName()
        ));
    }
}
