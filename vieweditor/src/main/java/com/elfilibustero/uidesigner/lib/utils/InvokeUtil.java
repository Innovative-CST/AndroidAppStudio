package com.elfilibustero.uidesigner.lib.utils;

import android.content.Context;
import android.view.View;

import com.tscodeeditor.android.appstudio.vieweditor.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InvokeUtil {

    public static Object create(String className, Class<?>[] types, Object... params) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor =
                    (types != null)
                            ? clazz.getDeclaredConstructor(types)
                            : clazz.getDeclaredConstructor();
            return (params != null) ? constructor.newInstance(params) : constructor.newInstance();
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InstantiationException
                | InvocationTargetException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getDrawableId(String name) {
        try {
            Class cls = R.drawable.class;
            Field field = cls.getField(name);
            return field.getInt(cls);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String getSuperClassName(String clazz) {
        try {
            return Class.forName(clazz).getSuperclass().getName();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static boolean hasMethod(Object v, String name, Class... types) {
        for (Class<?> superClass = v.getClass();
                superClass != Object.class;
                superClass = superClass.getSuperclass()) {
            try {
                superClass.getDeclaredMethod(name, types);
                return true;
            } catch (NoSuchMethodException e) {
            }
        }
        return false;
    }

    public static Object getFiledValueFromClass(Class<?> o, String name) {
        Field f = getField(o, name);
        try {
            return f.get(o);
        } catch (Exception e) {

        }

        return null;
    }

    public static Object getFiledValue(Object o, String name) {
        return getFiledValueFromClass(o.getClass(), name);
    }

    public static void setField(Object o, String name, Object value) {
        try {
            Class<?> clazz = o.getClass();
            Field field = getField(clazz, name);
            if (field == null) return;
            field.setAccessible(true);
            field.set(o, value);
        } catch (Exception e) {
        }
    }

    public static Object invoke(Object v, String name, Class<?>[] types, Object... params) {
        try {
            Class<?> clazz = v.getClass();
            Method method = getMethod(clazz, name, types);
            if (method == null) return null;
            method.setAccessible(true);
            return method.invoke(v, params);

        } catch (Exception e) {

        }
        return null;
    }

    private static Field getField(Class<?> clazz, String name) {
        for (Class<?> superClass = clazz;
                superClass != Object.class;
                superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(name);
            } catch (Exception e) {

            }
        }
        return null;
    }

    private static Method getMethod(Class<?> clazz, String name, Class<?>... types) {
        for (Class<?> superClass = clazz;
                superClass != Object.class;
                superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(name, types);
            } catch (Exception e) {

            }
        }
        return null;
    }
}
