package utils;

import java.lang.reflect.Field;

import annotations.EnvVar;

public class EnvLoader {
	public static <T> T load(Class<T> clazz) throws RuntimeException {
		try {
			T instance = clazz.getDeclaredConstructor().newInstance();
			for (Field field : clazz.getDeclaredFields()) {
				EnvVar annotation = field.getAnnotation(EnvVar.class);
				if (annotation == null) {
					throw new RuntimeException(String.format(
							"Please using annotation to naming field '%s' of class %s in env",
							field.getName(), clazz.getName()));
				}
				String envVarName = annotation.value();
				String envValue = System.getenv(envVarName);
				if (envValue == null) {
					throw new RuntimeException(String
							.format("Value of %s is null",
									field.getName()));
				}

				field.setAccessible(true);
				setFieldValue(field, instance, envValue);
			}
			return instance;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private static void setFieldValue(Field field, Object instance, String value) throws IllegalAccessException {
		Class<?> type = field.getType();

		if (type == String.class) {
			field.set(instance, value);
		} else if (type == Integer.class) {
			field.set(instance, Integer.parseInt(value));
		} else if (type == Boolean.class) {
			field.set(instance, Boolean.parseBoolean(value));
		} else {
			throw new IllegalArgumentException(
					"EnvLoader unsupported field type: " + type.getName());
		}

	}

}
