package org.blossomframework.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextTest {

	@Test
	void getBeanTypeTest() {
		ApplicationContext applicationContext = new ApplicationContext(Foo.class);

		String foo = "foo";
		String bar = "bar";

		assertNotNull(applicationContext.getBean(foo, Foo.class));
		assertNotNull(applicationContext.getBean(bar, Bar.class));

		assertEquals(bar, applicationContext.getBean(bar, Bar.class).getBar());

	}

	@Test
	void getBeanTest() {
		ApplicationContext applicationContext = new ApplicationContext(Foo.class);

		Object foo = applicationContext.getBean("foo");
		Object bar = applicationContext.getBean("bar");

		assertInstanceOf(Foo.class, foo);
		assertInstanceOf(Bar.class, bar);
	}


	@Configuration
	static class Foo {

		@Bean
		public Bar bar() {
			return new Bar("bar");
		}

	}

	static class Bar {
		private String bar;

		public Bar(String bar) {
			this.bar = bar;
		}

		public String getBar() {
			return bar;
		}

		public void setBar(String bar) {
			this.bar = bar;
		}
	}

}