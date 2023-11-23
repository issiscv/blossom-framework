package org.blossomframework.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private Map<String, Object> beans = new HashMap<>();

    // TODO: 2023-11-23 create generic type
    public ApplicationContext(Class<?>... configClasses) {
        for (Class<?> configClass : configClasses) {
            processConfigClass(configClass);
        }
    }

    private void processConfigClass(Class<?> configClass) {
        try {
            if (configClass.isAnnotationPresent(Configuration.class)) {
                Object configInstance = configClass.getDeclaredConstructor().newInstance();
                // TODO: 2023-11-23 start with class name lowercase
                beans.put(configInstance.getClass().getSimpleName(), configInstance);
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

    public Object getBean(String name) {
        return beans.get(name);
    }

    public Map<String, Object> getBeans() {
        return beans;
    }
}
