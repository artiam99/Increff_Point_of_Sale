package com.increff.pos.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.springframework.util.ReflectionUtils;

public class ConvertUtilGeneric {

    public static <T, F> T convert(F from, Class<T> To){
        T to= null;
        try {
            to = To.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("Error");
        }

        List<Field> toList=getFields(to);

        //hashMap fieldName to field
        HashMap<String, Field> fromFieldsMap = new HashMap<>();
        for(Field f: getFields(from)) {
            fromFieldsMap.put(f.getName(), f);
        }

        //traverse new object and set common fields
        for (Field toField : toList) {
            String fieldName=toField.getName();
            if(fromFieldsMap.containsKey(fieldName)) {
                toField.setAccessible(true);
                Field fromField= fromFieldsMap.get(fieldName);
                fromField.setAccessible(true);
                ReflectionUtils.setField(toField, to, ReflectionUtils.getField(fromFieldsMap.get(fieldName), from));
            }
        }
        return to;
    }

    private static <T> List<Field> getFields(T t) {
        List<Field> fields = new ArrayList<>();
        Class clazz = t.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}