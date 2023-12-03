package org.blossomframework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

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
	// TODO: 2023-12-03 parent type 의 bean 등록
	// TODO: 2023-12-03 getBean 메서드 추상화 
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


	/**
	 * 지정된 이름과 타입에 해당하는 빈(bean)을 검색하고 반환합니다.
	 * 이 메서드는 컨테이너에서 빈의 이름으로 빈을 찾은 후, 요청된 타입으로 캐스팅합니다.
	 *
	 * @param name         조회할 빈의 이름
	 * @param requiredType 반환할 빈의 예상 타입. 이 타입으로 빈이 캐스팅됩니다.
	 * @param <T>          제네릭 타입 매개변수. 반환될 빈의 타입을 지정합니다.
	 * @return 이름과 타입에 해당하는 빈. 존재하지 않거나 타입이 일치하지 않는 경우 예외가 발생할 수 있습니다.
	 */
	public <T> T getBean(String name, Class<T> requiredType) {
		return requiredType.cast(this.getBean(name));
	}

	public <T> T getBean(Class<T> requiredType) {
		Optional<T> beanIfExists = getBeanIfExists(requiredType);
		if (beanIfExists.isPresent()) {
			return beanIfExists.get();
		} else {
			throw new RuntimeException("No bean named '" + requiredType.getName() + "' is defined");
		}

	}

	private <T> Optional<T> getBeanIfExists(Class<T> requiredType) {
		for (String beanName : beanDefinitions.keySet()) {
			BeanDefinition beanDefinition = beanDefinitions.get(beanName);
			if (beanDefinition.getType() == requiredType) {
				return Optional.ofNullable(requiredType.cast(beanDefinition.getTargetBean()));
			}
		}
		return Optional.empty();
	}

	private BeanDefinition getBeanDefinition(String name) {
		BeanDefinition beanDefinition = beanDefinitions.get(name);
		if (beanDefinition == null) {
			throw new RuntimeException("No bean named '" + name + "' is defined");
		}
		return beanDefinition;
	}


}
