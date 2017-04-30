package com.szmg.cde;

import java.util.HashSet;
import java.util.Set;

public class InputConfig {

    private String path;
    private Set<String> packagesToIgnore;

    public InputConfig(String path) {
        this.path = path;
        packagesToIgnore = new HashSet<>();
        packagesToIgnore.add("java");
        packagesToIgnore.add("javax");
        packagesToIgnore.add("sun.reflect");
        packagesToIgnore.add("org.xml.sax");
        packagesToIgnore.add("groovy.lang");
        packagesToIgnore.add("org.codehaus.groovy");
    }

    public String getPath() {
        return path;
    }

    public Set<String> getPackagesToIgnore() {
        return packagesToIgnore;
    }
}
