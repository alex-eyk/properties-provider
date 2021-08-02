package com.ximand.properties;

import com.ximand.properties.mapper.StringToRightTypeMapper;
import com.ximand.properties.mapper.StringToRightTypeMapperImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertiesWriter {

    private final StringToRightTypeMapper toRightTypeMapper = new StringToRightTypeMapperImpl();

    private final File file;
    private final Properties properties;

    public PropertiesWriter(String absolutePath) {
        File file = new File(absolutePath);
        try {
            boolean success = file.createNewFile();
            if (success == false) {
                throw new IOException("Unable to create file");
            }
            this.file = file;
            this.properties = new Properties();
        } catch (IOException e) {
            throw new IllegalStateException("Properties file was not found, " +
                    "but unable to create new file; path: " + absolutePath, e);
        }
    }

    public Object addProperty(String key, String value, Class<?> valueClass) {
        properties.put(key, value);
        return toRightTypeMapper.map(value, valueClass);
    }

    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            properties.store(fileWriter, null);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write data to new " +
                    "properties file with path: " + file.getAbsolutePath(), e);
        }
    }

}
