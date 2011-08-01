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

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Singleton;
import java.util.logging.Logger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SecurityModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SecurityFilter.class).in(Singleton.class);
        bind(AuthManager.class).to(SessionBasedAuthManager.class).in(Singleton.class);
        bind(UserRepository.class).to(MemoryUserRepository.class).in(Singleton.class);
        bind(AuthResource.class).in(Singleton.class);
        bindInterceptor(Matchers.subclassesOf(UserRepository.class), Matchers.any(), new MethodInterceptor() {
            private final Logger logger = Logger.getLogger(UserRepository.class.getName());

            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                Object res = invocation.proceed();
                if(invocation.getArguments().length == 1) {
                    logger.info(invocation.getMethod().getName() + "(" + invocation.getArguments()[0] + ") => " + res);
                }
                return res;
            }
        });
    }
}
