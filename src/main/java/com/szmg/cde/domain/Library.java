package com.szmg.cde.domain;

import java.util.Objects;
import java.util.Set;

public class Library {

    private final String name;

    private final Set<Library> libs;

    private final Set<Clazz> ownClasses;

    private final Set<String> extraReferencedClasses;

    public Library(String name, Set<Library> libs, Set<Clazz> ownClasses, Set<String> extraReferencedClasses) {
        this.name = name;
        this.libs = libs; //HACK: do not copy this... it's being modified. create builder instead.
        this.ownClasses = ownClasses; //HACK: do not copy this... it's being modified. create builder instead.
        this.extraReferencedClasses = extraReferencedClasses;
    }

    public String getName() {
        return name;
    }

    public Set<Library> getLibs() {
        return libs;
    }

    public Set<Clazz> getOwnClasses() {
        return ownClasses;
    }

    public Set<String> getExtraReferencedClasses() {
        return extraReferencedClasses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Library library = (Library) o;
        return Objects.equals(name, library.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
