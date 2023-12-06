package org.blossomframework.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanFactory {

	private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
	private Map<String, Object> beanRegistry = new HashMap<>();

	public BeanFactory() {
	}

	public BeanFactory(Class<?>... configClasses) {
		for (Class<?> configClass : configClasses) {
			register(configClass);
		}
		refresh();
	}

	private void refresh() {
		for (String beanClassName : beanDefinitions.keySet()) {
			registerBeanRegistry(beanClassName);
		}
	}

	private void register(Class<?> configClass) {
		try {
			if (configClass.isAnnotationPresent(Configuration.class)) {
				processFactoryBeanClass(configClass);
				processFactoryMethodBean(configClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private <T> void processFactoryBeanClass(Class<T> configClass) {
		Field[] declaredFields = configClass.getDeclaredFields();

		List<String> propertyNames = new ArrayList<>();

		for (Field declaredField : declaredFields) {
			if (declaredField.isAnnotationPresent(Autowire.class)) {
				propertyNames.add(declaredField.getName());
			}
		}

		String beanClassName = resolveBeanName(configClass);

		BeanDefinition beanDefinition = new BeanDefinition();
		beanDefinition.setBeanClassName(beanClassName);
		beanDefinition.setBeanClass(configClass);
		beanDefinition.setFactoryBeanName(beanClassName);
		beanDefinition.setBeanPropertyNames(propertyNames);


		beanDefinitions.put(beanClassName, beanDefinition);
	}

	private void processFactoryMethodBean(Class<?> configClass) {
		for (Method method : configClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Bean.class)) {
				String methodBeanName = method.getName();
				List<String> propertyNames = new ArrayList<>();

				Class<?> factoryMethodBeanType = method.getReturnType(); //팩토리 메서드, 빈의 반환 타입
				Field[] declaredFields = factoryMethodBeanType.getDeclaredFields();
				for (Field declaredField : declaredFields) {
					propertyNames.add(declaredField.getName());
				}

				BeanDefinition beanDefinition = new BeanDefinition();
				beanDefinition.setBeanClass(factoryMethodBeanType);
				beanDefinition.setBeanClassName(resolveBeanName(factoryMethodBeanType));
				beanDefinition.setFactoryBeanName(resolveBeanName(configClass)); //팩토리 빈의 이름
				beanDefinition.setFactoryMethodBeanName(method.getName()); //팩토리 메서드의 이름
				beanDefinition.setBeanPropertyNames(propertyNames);

				beanDefinitions.put(methodBeanName, beanDefinition);
			}
		}
	}

	private <T> String resolveBeanName(Class<T> configClass) {
		String simpleName = configClass.getSimpleName();
		return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
	}

	public Object getBean(String name) {
		return beanRegistry.getOrDefault(name, null);
	}

	public Object registerBeanRegistry(String name) {
		if (beanRegistry.containsKey(name)) {
			return beanRegistry.get(name);
		} else {
			BeanDefinition beanDefinition = getBeanDefinition(name);
			if (beanDefinition == null) {
				return null;
			}

			try {
				Class<?> beanClass = beanDefinition.getBeanClass();
				Object bean = beanClass.getDeclaredConstructor().newInstance();

				List<String> beanPropertyNames = beanDefinition.getBeanPropertyNames();
				for (String beanPropertyName : beanPropertyNames) {
					Field field = beanClass.getDeclaredField(beanPropertyName);
					field.setAccessible(true); // private 필드에 접근하기 위해

					String autowiredBeanName = resolveBeanName(field.getType());
					Object autowiredBean = registerBeanRegistry(autowiredBeanName); // 의존성 빈 인스턴스
					field.set(bean, autowiredBean); // 의존성 주입
				}

				beanRegistry.put(name, bean); // 생성된 빈을 레지스트리에 저장
				return bean;

			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
					 InvocationTargetException | NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}
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

//	public <T> T getBean(Class<T> requiredType) {
//		for (String beanName : beanDefinitions.keySet()) {
//			BeanDefinition beanDefinition = beanDefinitions.get(beanName);
//			if (beanDefinition.getBeanClass() == requiredType) {
//				getbean
//				return requiredType.cast();
//			}
//		}
//	}

	private BeanDefinition getBeanDefinition(String name) {
		return beanDefinitions.getOrDefault(name, null);
	}


}
