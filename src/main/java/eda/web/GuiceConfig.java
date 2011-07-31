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

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.jsr250.Jsr250Injector;
import com.mycila.inject.service.ServiceModules;
import eda.support.log4j.JdkOverLog4j;

import javax.servlet.ServletContextEvent;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class GuiceConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        JdkOverLog4j.install();
        return Jsr250.createInjector(Stage.PRODUCTION, ServiceModules.loadFromClasspath(Module.class));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Jsr250Injector injector = (Jsr250Injector) servletContextEvent.getServletContext().getAttribute(Injector.class.getName());
        injector.destroy();
    }
}
