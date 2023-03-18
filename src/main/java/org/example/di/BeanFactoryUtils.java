package org.example.di;

import java.lang.reflect.Constructor;
import java.util.Set;

import org.example.annotation.Inject;
import org.reflections.ReflectionUtils;

public class BeanFactoryUtils {

	public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
		// 클래스 타입 객체로 @Inject가 붙어 있는 생성자를 가지고 온다.
		Set<Constructor> injectedConstructors = ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(
			Inject.class));

		// 위의 기준에 해당하는 생성자가 없다면, null
		if (injectedConstructors.isEmpty()) {
			return null;
		}

		// 있다면, @Inject가 붙어있는 생성자를 반환한다.
		return injectedConstructors.iterator().next();
	}
}
