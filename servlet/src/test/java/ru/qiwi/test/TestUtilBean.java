package ru.qiwi.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.testng.TestException;

public class TestUtilBean{

	public static Object getPrivateField(final Class targetClass, final String fieldName, final Object object) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException e) {
        	throw new TestException(e);
        } catch (SecurityException e) {
            throw new TestException(e);
        } catch (IllegalAccessException e) {
            throw new TestException(e);
        } catch (IllegalArgumentException e) {
            throw new TestException(e);
        }
    }
    
    public static Object invokeMethod(final Class targetClass, final String methodName, final Class[] argClasses,
            final Object[] argObjects, final Object object) throws InvocationTargetException {
        try {
            Method method = targetClass.getDeclaredMethod(methodName, argClasses);
            method.setAccessible(true);
            return method.invoke(object, argObjects);
        } catch (NoSuchMethodException e) {
            throw new TestException(e);
        } catch (SecurityException e) {
            throw new TestException(e);
        } catch (IllegalAccessException e) {
            throw new TestException(e);
        } catch (IllegalArgumentException e) {
            throw new TestException(e);
        }
    }
    
    public static void setPrivateField(final Class targetClass, final String fieldName, final Object object, final Object value) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException e) {
            throw new TestException(e);
        } catch (SecurityException e) {
            throw new TestException(e);
        } catch (IllegalAccessException e) {
            throw new TestException(e);
        } catch (IllegalArgumentException e) {
            throw new TestException(e);
        }
    }
}
