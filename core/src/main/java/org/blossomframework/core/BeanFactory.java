package org.blossomframework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

// bean 의 조회 가능(type 지정 가능)
public class BeanFactory {

	private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

	public BeanFactory() {
	}

	public BeanFactory(Class<?>... configClasses) {
		for (Class<?> configClass : configClasses) {
			processConfigClass(configClass);
		}
	}

	// TODO: 2023-11-29 빈 팩토리에 저장 후 정렬 필요
	private void processConfigClass(Class<?> configClass) {
		try {
			if (configClass.isAnnotationPresent(Configuration.class)) {
				Object configInstance = configClass.getDeclaredConstructor().newInstance();

				String configurationBeanName = resolveConfigurationBeanName(configInstance);

				beanDefinitions.put(configurationBeanName, new BeanDefinition(configurationBeanName, configClass, configInstance));

				registerMethodBean(configClass, configInstance);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO: 2023-11-29 팩토리 메서드의 파라미터 빈으로 등록되었는지 체크(이름, 타입 체크 필요)
	// TODO: 2023-11-29 DI 기능 개발
	// TODO: 2023-11-29 configClass 와일드카드 타입이 아닌, 타입 파라미터 받아 저장하기
	private void registerMethodBean(Class<?> configClass, Object configInstance) throws IllegalAccessException, InvocationTargetException {
		for (Method method : configClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Bean.class)) {
				String methodBeanName = method.getName();

				//빈 팩토리 메서드의 파라미터가 빈에 등록되었는지
//				Parameter[] parameters = method.getParameters();
//				for (Parameter parameter : parameters) {
//
//				}

				beanDefinitions.put(methodBeanName, new BeanDefinition(methodBeanName, method.getReturnType(), method.invoke(configInstance)));
			}
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

	public <T> T getBean(Class<T> requiredType) {
		for (String beanName : beanDefinitions.keySet()) {
			BeanDefinition beanDefinition = beanDefinitions.get(beanName);
			if (beanDefinition.getType() == requiredType) {
				return requiredType.cast(beanDefinition.getTargetBean());
			}
		}
		throw new RuntimeException("No bean named '" + requiredType.getName() + "' is defined");
	}

	private BeanDefinition getBeanDefinition(String name) {
		BeanDefinition beanDefinition = beanDefinitions.get(name);
		if (beanDefinition == null) {
			throw new RuntimeException("No bean named '" + name + "' is defined");
		}
		return beanDefinition;
	}


}
