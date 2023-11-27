package org.blossomframework.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

// TODO: 2023-11-27 get generic type
// TODO: 2023-11-27 DI
public class ApplicationContext {

    private Map<String, Object> beans = new HashMap<>();

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

                beans.put(getBeanName(configInstance), configInstance);
                for (Method method : configClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        Object bean = method.invoke(configInstance);
                        beans.put(method.getName(), bean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getBeanName(Object bean) {
        String simpleName = bean.getClass().getSimpleName();
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }

    public Object getBean(String name) {
        return beans.get(name);
    }

    public Map<String, Object> getBeans() {
        return beans;
    }
}
