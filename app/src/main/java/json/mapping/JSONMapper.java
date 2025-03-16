package json.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import json.Exceptions.JSONMappingException;
import json.types.JSONArray;
import json.types.JSONBoolean;
import json.types.JSONDictionary;
import json.types.JSONFloat;
import json.types.JSONInteger;
import json.types.JSONObject;
import json.types.JSONString;

public final class JSONMapper {
    public <T> T map(Class<T> clazz, JSONObject<?> jsonObject) throws JSONMappingException {
        if (!(jsonObject instanceof JSONDictionary jsonDictionary)) {
            throw new JSONMappingException("Invalid JSONObject");
        }

        try {
            return map(clazz, jsonDictionary);
        } catch (NoSuchFieldException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | JSONMappingException e) {
            throw new JSONMappingException("Failed to map JSON", e);
        }
    }

    private <T> T map(Class<T> clazz, JSONDictionary jsonDictionary) throws NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, JSONMappingException {
        T result = clazz.getDeclaredConstructor().newInstance();
        for (Map.Entry<String, JSONObject<?>> entry: jsonDictionary.getValue().entrySet()) {
            String key = entry.getKey();
            JSONObject<?> value = entry.getValue();
            map(result, clazz, value, key);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> void map(T result, Class<T> clazz, JSONObject<?> value, String key) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, JSONMappingException {
        Field field = clazz.getDeclaredField(key);
        field.setAccessible(true);
        Class<?> fieldClazz = field.getType();

        if (String.class.isAssignableFrom(fieldClazz) && (value instanceof JSONString jsonString)) {
            field.set(result, jsonString.getValue());
        } else if (Boolean.class.isAssignableFrom(fieldClazz) && (value instanceof JSONBoolean jsonBoolean)) {
            field.set(result, jsonBoolean.getValue());
        } else if (Integer.class.isAssignableFrom(fieldClazz) && (value instanceof JSONInteger jsonInteger)) {
            field.set(result, jsonInteger.getValue());
        } else if (Float.class.isAssignableFrom(fieldClazz) && (value instanceof JSONFloat jsonFloat)) {
            field.set(result, jsonFloat.getValue());
        } else if (List.class.isAssignableFrom(fieldClazz) && (value instanceof JSONArray jsonArray)) {
            Type genericeType = field.getGenericType();
            if (!(genericeType instanceof ParameterizedType parameterizedType)) {
                throw new JSONMappingException("List field is not a generic type");
            }

            Type objectType = parameterizedType.getActualTypeArguments()[0];
            if (!(objectType instanceof Class<?> genericeClassParameter)) {
                throw new JSONMappingException("Unssupported List generic type");
            }

            @SuppressWarnings("rawtypes")
            List valueList = new ArrayList();

            for (JSONObject<?> jsonObject: jsonArray.getValue()) {
                var newObject = map(genericeClassParameter, jsonObject);
                if (!(genericeClassParameter.isInstance(newObject))) {
                    throw new JSONMappingException("Invalid Object inside list: " + field.getName());
                }
                valueList.add(genericeClassParameter.cast(newObject));
            }
            field.set(result, valueList);
        } else if ((value instanceof JSONDictionary jsonDictionary)) {
            Object valueObject = map(fieldClazz, jsonDictionary);
            field.set(result, valueObject);
        } else {
            throw new JSONMappingException("Unexpected JSONObject");
        }
    }
}
