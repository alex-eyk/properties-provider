package com.ximand.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FieldsInjector<T> {

    private final Class<T> configClass;
    private final String propertiesPath;
    private final T configObject;
    private InputStream propertiesInputStream;

    public FieldsInjector(Class<T> configClass, String propertiesPath) {
        this.configClass = configClass;
        this.propertiesPath = propertiesPath;
        try {
            this.configObject = configClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Unable to create class new instance; check that it have public " +
                    "constructor without parameters", e);
        }
    }

    public T inject() {
        final Map<Property, Field> propertyToFieldMap = getPropertyToFieldMap();
        try {
            this.propertiesInputStream = getInputStream(propertiesPath);
            readProperties(propertyToFieldMap);
        } catch (FileNotFoundException e) {
            createNewProperties(propertyToFieldMap);
        }
        return configObject;
    }

    private InputStream getInputStream(String path) throws FileNotFoundException {
        if (path.startsWith("jarpath:/")) {
            return PathUtils.getJarDirectoryFileStream(path, configClass);
        } else if (path.startsWith("libres:/")) {
            return PathUtils.getLibraryResourceStream(path);
        } else if (path.startsWith("res:/")) {
            return PathUtils.getResourceStream(path, configClass.getClassLoader());
        }
        return new FileInputStream(path);
    }

    private void readProperties(Map<Property, Field> propertyToFieldMap) {
        final PropertiesReader reader = new PropertiesReader(propertiesInputStream);
        propertyToFieldMap.forEach(
                (property, field) -> injectPropertyFromFile(reader, property, field)
        );
    }

    private void injectPropertyFromFile(PropertiesReader reader, Property property, Field field) {
        final Object value = reader.getPropertyOrDefault(property.name(), property.defaultValue(), field.getType());
        injectProperty(field, value);
    }

    private void createNewProperties(Map<Property, Field> propertyToFieldMap) {
        final PropertiesWriter writer = new PropertiesWriter(PathUtils.getJarDirectoryFilePath(propertiesPath, configClass));
        propertyToFieldMap.forEach(
                (property, field) -> injectPropertyFromNewFile(writer, property, field)
        );
        writer.save();
    }

    private Map<Property, Field> getPropertyToFieldMap() {
        final Map<Property, Field> propertyToFieldMap = new HashMap<>();
        for (Field field : configClass.getDeclaredFields()) {
            Property property = field.getAnnotation(Property.class);
            propertyToFieldMap.put(property, field);
        }
        return propertyToFieldMap;
    }

    private void injectPropertyFromNewFile(PropertiesWriter writer, Property property, Field field) {
        Object value = writer.addProperty(property.name(), property.defaultValue(), field.getType());
        injectProperty(field, value);
    }

    private void injectProperty(Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(configObject, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to change value of field :{" + field.getName() + "}, " + "check " +
                    "that variable not final and not static");
        }
    }

}
