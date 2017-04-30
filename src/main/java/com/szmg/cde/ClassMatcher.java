package com.szmg.cde;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClassMatcher {

    private Pattern classPattern;

    public ClassMatcher(Collection<String> patterns) {
        String sum = patterns.stream().map(ClassMatcher::toRegexString).collect(Collectors.joining("|", "(", ")"));
        classPattern = Pattern.compile(sum);
    }

    public boolean matches(String className) {
        return classPattern.matcher(className).matches();
    }

    private static String toRegexString(String pattern) {
        return pattern.replaceAll("[.$]", "\\\\$0").replaceAll("\\*", ".*");
        //return Pattern.quote(pattern).replaceAll("\\*", ".*");
    }
}
