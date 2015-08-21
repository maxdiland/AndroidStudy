    package com.gmail.maxdiland.drebedengireports.db.util;

import android.database.Cursor;

import android.util.Log;
import com.gmail.maxdiland.drebedengireports.db.util.annotation.Field;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * author Max Diland
 */
public class CursorOnObjectMapper {
    private static final String TAG = "CursorOnObjectMapper";

    public static <T> T[] mapObjects(Cursor cursor, Class<T> aClass) {
        List<T> objects = new ArrayList<T>(cursor.getColumnCount());
        while (cursor.moveToNext()) {
            objects.add( mapObjectFromCurrentCursorPosition(cursor, aClass) );
        }

        T[] array = (T[]) Array.newInstance(aClass, objects.size());
        return objects.toArray(array);
    }

    public static <T> T mapObjectFromCurrentCursorPosition(Cursor cursor, Class<T> aClass) {
        T instance = createInstance(aClass);
        for (java.lang.reflect.Field field : aClass.getDeclaredFields()) {
            if ( !Modifier.isFinal(field.getModifiers())
                    && isFieldMarkedWithAnnotation(field, Field.class) ) {
                String cursorColumnName = getCursorColumnName(field);
                int columnIndex = cursor.getColumnIndex(cursorColumnName);

                Object columnData = getColumnData(cursor, columnIndex);
                try {
                    if( Modifier.isPrivate(field.getModifiers()) ) {
                        field.setAccessible(true);
                    }
                    field.set(instance, columnData);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "Unable to map field. " + field.getName() + ", " + columnData);
                }
            }
        }
        return instance;
    }

    private static Object getColumnData(Cursor cursor, int columnIndex) {
        int dataType = cursor.getType(columnIndex);
        Object dataToReturn;
        switch (dataType) {
            case Cursor.FIELD_TYPE_BLOB:
                dataToReturn = cursor.getBlob(columnIndex);
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                dataToReturn = cursor.getDouble(columnIndex);
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                dataToReturn = cursor.getInt(columnIndex);
                break;
            case Cursor.FIELD_TYPE_STRING:
                dataToReturn = cursor.getString(columnIndex);
                break;
            default:
                dataToReturn = null;
        }
        return dataToReturn;
    }

    private static String getCursorColumnName(java.lang.reflect.Field field) {
        Field fieldAnnotation = field.getAnnotation(Field.class);
        String columnName;
        if (fieldAnnotation.value() != null) {
            columnName = fieldAnnotation.value();
        } else {
            columnName = field.getName();
        }
        return columnName;
    }

    private static boolean isFieldMarkedWithAnnotation(java.lang.reflect.Field field,
                                                       Class<? extends Annotation> fieldAnnotationClass) {

        for (Annotation annotation : field.getDeclaredAnnotations()) {
            if (annotation.annotationType() == fieldAnnotationClass) {
                return true;
            }
        }
        return false;
    }

    private static <T> T createInstance(Class<T> aClass) {
        try {
            return aClass.newInstance();
        } catch (Exception e) {
            Log.e(TAG, "Unable to instantiate object. Class: " + aClass.getName());
            throw new RuntimeException("Unable to instantiate object", e);
        }
    }
}
