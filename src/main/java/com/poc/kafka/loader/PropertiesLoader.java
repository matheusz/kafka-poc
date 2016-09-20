package com.poc.kafka.loader;

import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties loadPropertiesFromFile(String fileName) {
        try (InputStream producerPropsStream = Resources.getResource(fileName).openStream()) {
            Properties properties = new Properties();
            properties.load(producerPropsStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
