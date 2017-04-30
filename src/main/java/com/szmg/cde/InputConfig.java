package com.szmg.cde;

import java.util.HashSet;
import java.util.Set;

public class InputConfig {

    private String path;
    private Set<String> excludedClasses;

    public InputConfig(String path) {
        this.path = path;
        excludedClasses = new HashSet<>();
        excludedClasses.add("java.*");
        excludedClasses.add("javax.*");
        excludedClasses.add("sun.reflect.*");
        excludedClasses.add("org.ietf.jgss.*");
        excludedClasses.add("org.omg.*");
        excludedClasses.add("org.xml.sax.*");
        excludedClasses.add("org.w3c.dom.*");
        excludedClasses.add("groovy.lang.*");
        excludedClasses.add("org.codehaus.groovy.*");
        excludedClasses.add("*Test");
    }

    public String getPath() {
        return path;
    }

    public Set<String> getExcludedClasses() {
        return excludedClasses;
    }
}
