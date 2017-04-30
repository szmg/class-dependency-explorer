package com.szmg.cde;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClassMatcherTest {

    private ClassMatcher classMatcher;

    @Before
    public void setup() {
        List<String> patterns = new ArrayList<>();
        patterns.add("this.is.AClass");
        patterns.add("java.*");
        patterns.add("*Test");
        classMatcher = new ClassMatcher(patterns);
    }

    @Test
    public void shouldMatchExactClass() {
        assertTrue(classMatcher.matches("this.is.AClass"));
    }

    @Test
    public void shouldMatchWildchar() {
        assertTrue(classMatcher.matches("java.lang.String"));
        assertTrue(classMatcher.matches("this.is.AClassTest"));
    }

    @Test
    public void shouldNotMatchThingsNotAmongPatterns() {
        assertFalse(classMatcher.matches("this.is.BClass"));
        assertFalse(classMatcher.matches("javalang.Nothing"));
        assertFalse(classMatcher.matches("this.is.NotATestForSure"));
    }

}