package com.ximand.properties;

import com.ximand.properties.mapper.StringToRightTypeMapper;
import com.ximand.properties.mapper.StringToRightTypeMapperImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesReader {

    private final StringToRightTypeMapper toRightTypeMapper = new StringToRightTypeMapperImpl();

    private final Properties properties = new Properties();

    PropertiesReader(InputStream inputStream) {
        final BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            properties.load(fileReader);
            fileReader.close();
        } catch (IOException e) {
            throw new IllegalStateException("Exception while reading properties", e);
        }
    }

    Object getPropertyOrDefault(String name, String def, Class<?> valueClass) {
        String value = properties.getProperty(name);
        if (value != null) {
            return toRightTypeMapper.map(value, valueClass);
        } else {
            return toRightTypeMapper.map(def, valueClass);
        }
    }

}
