package org.blossomframework.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationContextTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    void beforeEach() {
        applicationContext = new ApplicationContext(Foo.class);
    }

    @Test
    void ApplicationContextTest() {
        Foo foo = (Foo) applicationContext.getBean("Foo");
        Bar bar = (Bar) applicationContext.getBean("bar");

        assertEquals(applicationContext.getBeans().size(), 2);
        assertEquals(foo.getClass().getSimpleName(), "Foo");
        assertEquals(bar.getClass().getSimpleName(), "Bar");
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