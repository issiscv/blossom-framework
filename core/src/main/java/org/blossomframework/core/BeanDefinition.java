package org.blossomframework.core;

public class BeanDefinition {

	private String beanName;

	private Class type;

	private Object targetBean;

	public BeanDefinition() {
	}

	public BeanDefinition(String beanName, Class type, Object targetBean) {
		this.beanName = beanName;
		this.type = type;
		this.targetBean = targetBean;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public Object getTargetBean() {
		return targetBean;
	}

	public void setTargetBean(Object targetBean) {
		this.targetBean = targetBean;
	}
}
