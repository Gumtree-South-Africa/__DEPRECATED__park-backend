package com.ebay.park.elasticsearch.utils;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class ESUtils {
    public ESUtils() {
    }

    public Map esPropertiesMapper(Class clazz) {
        Map typeMap;
        Map classPropertiesMap = new HashMap();
        Map propertiesMap = new HashMap();
        classPropertiesMap.put("properties", propertiesMap);

        try {
            for (Field field : clazz.getDeclaredFields()) {
                String name = field.getName();
                Class type = field.getType();

                if (!Modifier.isStatic(field.getModifiers())) {
                    if (EnumUtils.isValidEnum(FieldTypes.class, type.getSimpleName())) {
                        typeMap = new HashMap();
                        if (type.getSimpleName().equalsIgnoreCase(FieldTypes.GeoPoint.toString())) {
                            typeMap.put("type", "geo_point");
                            typeMap.put("lat_lon", "true");
                        } else if (Collection.class.isAssignableFrom(type)) {
                            Class<?> nestedType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                            typeMap = esPropertiesMapper(nestedType);
                            typeMap.put("type", "nested");
                        } else {
                            typeMap.put("type", StringUtils.lowerCase(type.getSimpleName()));
                        }
                        if (fieldHasAnnotation(field, MultiField.class)) {
                            Map fields = new HashMap();
                            if (null != field.getAnnotation(MultiField.class).otherFields()) {
                                for (InnerField nestedField : field.getAnnotation(MultiField.class).otherFields()) {
                                    Map fieldProperties = new HashMap();
                                    fieldProperties.put("type", StringUtils.lowerCase(nestedField.type().toString()));
                                    if (StringUtils.isNoneEmpty(nestedField.indexAnalyzer())) {
                                        fieldProperties.put("index_analyzer", nestedField.indexAnalyzer());
                                    }
                                    if (StringUtils.isNoneEmpty(nestedField.searchAnalyzer())) {
                                        fieldProperties.put("search_analyzer", nestedField.searchAnalyzer());
                                    }
                                    fields.put(nestedField.suffix(), fieldProperties);
                                }
                            }
                            typeMap.put("fields", fields);
                        }
                        propertiesMap.put(name, typeMap);

                    } else {
                        propertiesMap.put(name, esPropertiesMapper(type));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classPropertiesMap;
    }

    protected Boolean fieldHasAnnotation(Field field, Class annotation) {
        try {
            if (null != field.getAnnotation(annotation)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}