package org.blossomframework.beans;

import java.util.List;

public class BeanDefinition {

	private String beanClassName;
	private Class beanClass;
	private String factoryBeanName;
	private String factoryMethodBeanName;

	private List<String> beanPropertyNames;

	public BeanDefinition() {
	}

	public Class getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class beanClass) {
		this.beanClass = beanClass;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public String getFactoryBeanName() {
		return factoryBeanName;
	}

	public void setFactoryBeanName(String factoryBeanName) {
		this.factoryBeanName = factoryBeanName;
	}

	public String getFactoryMethodBeanName() {
		return factoryMethodBeanName;
	}

	public void setFactoryMethodBeanName(String factoryMethodBeanName) {
		this.factoryMethodBeanName = factoryMethodBeanName;
	}

	public List<String> getBeanPropertyNames() {
		return beanPropertyNames;
	}

	public void setBeanPropertyNames(List<String> beanPropertyNames) {
		this.beanPropertyNames = beanPropertyNames;
	}
}
