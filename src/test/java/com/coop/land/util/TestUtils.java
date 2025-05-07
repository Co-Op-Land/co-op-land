package com.coop.land.util;

import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.Map;

@Slf4j
public class TestUtils {

    public static <T> T spy(Class<T> clazz, Map<String, Object> fieldValues) {
        T entity = createEntity(clazz, fieldValues);
        return Mockito.spy(entity); //자동으로 spy() 적용
    }

    public static <T> T createEntity(Class<T> clazz, Map<String, Object> fieldValues) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);  //protected/private 생성자 접근 허용
            T instance = constructor.newInstance();

            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                try {
                    ReflectionTestUtils.setField(instance, entry.getKey(), entry.getValue());
                } catch (IllegalArgumentException e) {
                    log.debug("⚠️ Warning: Field '{}' not found in class {}", entry.getKey(), clazz.getName());
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("객체 생성 실패: " + clazz.getSimpleName(), e);
        }
    }
}