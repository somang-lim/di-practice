package org.example.di;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.junit.jupiter.api.BeforeEach;
import org.reflections.Reflections;

class BeanFactoryTest {

	private Reflections reflections;
	private BeanFactory beanFactory;

	@BeforeEach
	void setUp() {
		reflections = new Reflections("org.example");
		// UserController, UserService
		Set<Class<?>> preInstantiatedClazz = getTypesAnnotatedWith(Controller.class, Service.class);

		beanFactory = new BeanFactory(preInstantiatedClazz);
	}

	private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
		Set<Class<?>> beans = new HashSet<>();

		for (Class<? extends Annotation> annotation : annotations) {
			beans.addAll(reflections.getTypesAnnotatedWith(annotation));
		}

		return beans;
	}
}
