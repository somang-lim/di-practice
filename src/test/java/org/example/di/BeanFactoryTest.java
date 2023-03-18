package org.example.di;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class BeanFactoryTest {

	private Reflections reflections;
	private BeanFactory beanFactory;

	@BeforeEach
	void setUp() {
		// Reflections 기술을 사용해서 "org.example" 패키지 아래를 주목한다.
		reflections = new Reflections("org.example");
		// Annotation이 Controller, Service인 클래스 타입의 객체를 모두 조회한다. --> UserController, UserService
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

	@Test
	void diTest() {
		UserController userController = beanFactory.getBean(UserController.class);

		assertThat(userController).isNotNull();
		assertThat(userController.getUserService()).isNotNull();
	}
}
