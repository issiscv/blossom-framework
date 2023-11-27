package org.blossomframework.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationContextTest {

    @Test
    void ApplicationContextTest() {
        ApplicationContext applicationContext = new ApplicationContext(Foo.class);

        assertEquals(applicationContext.getBeans().size(), 2);
        assertEquals(applicationContext.getBean("foo").getClass(), Foo.class);
        assertEquals(applicationContext.getBean("bar").getClass(), Bar.class);
    }

    @Configuration
    static class Foo {

        @Bean
        public Bar bar() {
            return new Bar();
        }

    }

    static class Bar {
    }

}