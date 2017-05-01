package com.szmg.cde.config;

import java.util.Set;

public class InputConfig {

    private String path;
    private Set<String> excludedClasses;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<String> getExcludedClasses() {
        return excludedClasses;
    }

    public void setExcludedClasses(Set<String> excludedClasses) {
        this.excludedClasses = excludedClasses;
    }
}
