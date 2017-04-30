package com.szmg.cde.domain;

import java.util.Objects;
import java.util.Set;

public class Clazz {

    private final String fullyQualifiedName;

    private final Set<String> referencedClasses;

    private final Library library;

    public Clazz(String fullyQualifiedName, Set<String> referencedClasses, Library library) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.referencedClasses = referencedClasses;
        this.library = library;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public Set<String> getReferencedClasses() {
        return referencedClasses;
    }

    public Library getLibrary() {
        return library;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clazz clazz = (Clazz) o;
        return Objects.equals(fullyQualifiedName, clazz.fullyQualifiedName) &&
                Objects.equals(library, clazz.library);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullyQualifiedName, library);
    }
}
