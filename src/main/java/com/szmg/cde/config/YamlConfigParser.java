package com.szmg.cde.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class YamlConfigParser {

    public InputConfig parse(String path) throws IOException {
        try (InputStream stream = new FileInputStream(path)) {
            return parse(stream);
        }
    }

    public InputConfig parse(InputStream inputStream) throws IOException {
        Yaml yaml = new Yaml(new Constructor(InputConfig.class));
        return (InputConfig) yaml.load(inputStream);
    }

}
