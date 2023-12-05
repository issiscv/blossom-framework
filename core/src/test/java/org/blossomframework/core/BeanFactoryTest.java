package org.blossomframework.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryTest {

	private	BeanFactory beanFactory = new BeanFactory(ConfigClass.class);
	private String foo = "foo";
	private String bar = "bar";
	private String baz = "baz";

	@Test
	void getBeanByNameTest() {
		Object fooBean = beanFactory.getBean(foo);
		assertNotNull(fooBean);
		assertInstanceOf(Foo.class, fooBean);

		Object barBean = beanFactory.getBean(bar);
		assertNotNull(barBean);
		assertInstanceOf(Bar.class, barBean);

		Object bazBean = beanFactory.getBean(baz);
		assertNotNull(bazBean);
		assertInstanceOf(Baz.class, bazBean);
	}

	@Test
	void getBeanByNameAndTypeTest() {
		Foo fooBean = beanFactory.getBean(foo, Foo.class);
		assertNotNull(fooBean);
		assertInstanceOf(Foo.class, fooBean);

		Bar barBean = beanFactory.getBean(bar, Bar.class);
		assertNotNull(barBean);
		assertInstanceOf(Bar.class, barBean);

		Baz bazBean = beanFactory.getBean(baz, Baz.class);
		assertNotNull(bazBean);
		assertInstanceOf(Baz.class, bazBean);
	}

//	@Test
//	void getBeanByTypeTest() {
//		assertNotNull(beanFactory.getBean(Foo.class));
//		assertNotNull(beanFactory.getBean(Bar.class));
//		assertNotNull(beanFactory.getBean(Baz.class));
//
//		assertThrows(RuntimeException.class, () -> beanFactory.getBean(Bean.class));
//	}

	// DI 기능 개발 후 테스트 통과 시키기
//	@Test
//	void DITest() {
//		// given
//		Foo foo = beanFactory.getBean("foo", Foo.class);
//		Foo foo2 = beanFactory.getBean("foo", Foo.class);
//
//		Bar bar = beanFactory.getBean("bar", Bar.class);
//
//		// when
//		Baz baz1 = foo.getBaz();
//		Baz baz2 = bar.getBaz();
//
//		// then
//		assertEquals(foo, foo2);
//		assertEquals(baz1, baz2);
//
//	}

	@Configuration
	static class ConfigClass {

		@Bean
		public Foo foo() {
			return new Foo(bar(), baz());
		}

		@Bean
		public Bar bar() {
			return new Bar(baz());
		}

		@Bean
		public Baz baz() {
			return new Baz();
		}

	}

	static class Foo {

		private Bar bar;
		private Baz baz;

		public Foo() {
		}

		public Foo(Bar bar, Baz baz) {
			this.bar = bar;
			this.baz = baz;
		}

		public Bar getBar() {
			return bar;
		}

		public void setBar(Bar bar) {
			this.bar = bar;
		}

		public Baz getBaz() {
			return baz;
		}

		public void setBaz(Baz baz) {
			this.baz = baz;
		}
	}

	static class Bar {
		private Baz baz;

		public Bar() {
		}

		public Bar(Baz baz) {
			this.baz = baz;
		}

		public Baz getBaz() {
			return baz;
		}

		public void setBaz(Baz baz) {
			this.baz = baz;
		}
	}

	static class Baz {

		public Baz() {
		}
	}

}