package org.example.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BeanFactory {
	// 클래스 타입의 객체를 확인할 수 있다.
	private final Set<Class<?>> preInstantiatedClazz;
	// 클래스 타입 뿐만 아니라 인스턴스도 필요하다. --> HashMap 사용한다.
	private Map<Class<?>, Object> beans = new HashMap<>();

	public BeanFactory(Set<Class<?>> preInstantiatedClazz) {
		this.preInstantiatedClazz = preInstantiatedClazz;
		initialize();
	}

	private void initialize() {
		for (Class<?> clazz : preInstantiatedClazz) {
			// 클래스 타입의 객체를 가지고 인스턴스를 생성해서 초기화를 시켜준다.
			Object instance = createInstance(clazz);
			beans.put(clazz, instance);
		}
	}

	private Object createInstance(Class<?> clazz) {
		// 생성자
		// 클래스 타입 객체로 생성자를 조회한다.
		Constructor<?> constructor = findConstructor(clazz);

		// 파라미터
		// 생성자의 파라미터 정보를 조회한다.
		List<Object> parameters = new ArrayList<>();
		for (Class<?> typeClass : constructor.getParameterTypes()) {
			parameters.add(getParameterByClass(typeClass));
		}

		// 인스턴스 생성
		// 위의 정보를 가지고 인스턴스를 생성한다.
		try {
			return constructor.newInstance(parameters.toArray());
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private Constructor<?> findConstructor(Class<?> clazz) {
		Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

		if (Objects.nonNull(constructor)) {
			return constructor;
		}

		return clazz.getConstructors()[0];
	}

	private Object getParameterByClass(Class<?> typeClass) {
		// 클래스 타입 객체를 key로 가지는 인스턴스가 있는지 확인한다.
		Object instanceBean = getBean(typeClass);

		// 위의 기준에 해당하는 인스턴스가 있다면, 바로 반환한다.
		if (Objects.nonNull(instanceBean)) {
			return instanceBean;
		}

		// 없다면, 해당 부분을 재귀호춣로 필요한 대상부터 인스턴스를 호출하게 한다.
		return createInstance(typeClass);
	}

	public <T> T getBean(Class<T> requiredType) {
		return (T) beans.get(requiredType);
	}
}
