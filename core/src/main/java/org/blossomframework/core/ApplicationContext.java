package org.blossomframework.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

// bean 의 조회 가능(type 지정 가능)
public class ApplicationContext {

	private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

	public ApplicationContext() {
	}

	public ApplicationContext(Class<?>... configClasses) {
		for (Class<?> configClass : configClasses) {
			processConfigClass(configClass);
		}
	}

	private void processConfigClass(Class<?> configClass) {
		try {
			if (configClass.isAnnotationPresent(org.blossomframework.core.Configuration.class)) {
				Object configInstance = configClass.getDeclaredConstructor().newInstance();

				String configurationBeanName = resolveConfigurationBeanName(configInstance);

				beanDefinitions.put(configurationBeanName, new BeanDefinition(configurationBeanName, configClass, configInstance));

				for (Method method : configClass.getDeclaredMethods()) {
					if (method.isAnnotationPresent(Bean.class)) {
						String methodBeanName = method.getName();
						beanDefinitions.put(methodBeanName, new BeanDefinition(methodBeanName, method.getDeclaringClass(), method.invoke(configInstance)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String resolveConfigurationBeanName(Object bean) {
		String simpleName = bean.getClass().getSimpleName();
		return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
	}

	public Object getBean(String name) {
		return getBeanDefinition(name).getTargetBean();

	}

	public <T> T getBean(String name, Class<T> requiredType) {
		return requiredType.cast(getBeanDefinition(name).getTargetBean());

	}

	private BeanDefinition getBeanDefinition(String name) {
		BeanDefinition beanDefinition = beanDefinitions.get(name);
		if (beanDefinition == null) {
			throw new RuntimeException("No bean named '" + name + "' is defined");
		}
		return beanDefinition;
	}


}
