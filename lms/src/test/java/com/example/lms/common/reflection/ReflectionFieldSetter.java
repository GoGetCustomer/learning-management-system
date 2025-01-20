package com.example.lms.common.reflection;

import java.lang.reflect.Field;

public class ReflectionFieldSetter {

    public static void setId(Object target, Long id) {
        setField(target, "id", id);
    }

    public static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = getFieldFromClass(target.getClass(), fieldName);
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            field.set(target, value);
            field.setAccessible(isAccessible);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(fieldName + " 설정 중 오류 발생", e);
        }
    }

    private static Field getFieldFromClass(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName + " 필드를 찾을 수 없습니다.");
    }
}

