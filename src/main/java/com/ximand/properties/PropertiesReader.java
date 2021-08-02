package com.ximand.properties;

import com.ximand.properties.mapper.StringToRightTypeMapper;
import com.ximand.properties.mapper.StringToRightTypeMapperImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private final StringToRightTypeMapper toRightTypeMapper = new StringToRightTypeMapperImpl();

    private final Properties properties = new Properties();

    public PropertiesReader(String absolutePath) throws FileNotFoundException {
        System.out.println(absolutePath);
        FileReader fileReader = new FileReader(absolutePath);
        try {
            properties.load(fileReader);
            fileReader.close();
        } catch (IOException e) {
            throw new IllegalStateException("Exception while reading properties " +
                    "file with path: " + absolutePath, e);
        }
    }

    public Object getPropertyOrDefault(String name, String def, Class<?> valueClass) {
        String value = properties.getProperty(name);
        if (value != null) {
            return toRightTypeMapper.map(value, valueClass);
        } else {
            return toRightTypeMapper.map(def, valueClass);
        }
    }

}
