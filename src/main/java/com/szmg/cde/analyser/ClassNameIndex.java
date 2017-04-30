package com.szmg.cde.analyser;

import com.szmg.cde.domain.Clazz;
import com.szmg.cde.domain.Library;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class ClassNameIndex {

    private static Logger LOGGER = Logger.getLogger(ClassNameIndex.class.getName());

    private final Map<String, Clazz> index = new HashMap<>();


    public ClassNameIndex(Library rootLibrary) {
        addAllClasses(rootLibrary);
    }

    public Optional<Clazz> findByName(String fqn) {
        return Optional.ofNullable(index.get(fqn));
    }

    private void addAllClasses(Library rootLibrary) {
        Iterable<Library> allLibs = getAllLibs(rootLibrary);

        for (Library lib : allLibs) {
            for (Clazz clazz : lib.getOwnClasses()) {
                Clazz existing = index.get(clazz.getFullyQualifiedName());
                if (existing != null) {
                    LOGGER.warning(String.format("Class [%s] is present in [%s] and [%s]. The second one will be ignored.",
                            clazz.getFullyQualifiedName(),
                            existing.getLibrary().getName(),
                            clazz.getLibrary().getName()));
                } else {
                    index.put(clazz.getFullyQualifiedName(), clazz);
                }
            }
        }
    }

    private Iterable<Library> getAllLibs(Library library) {
        Set<Library> allLibraries = new HashSet<>();
        Deque<Library> toAdd = new LinkedList<>();
        toAdd.push(library);
        while (!toAdd.isEmpty()) {
            Library libToAdd = toAdd.pop();
            if (!allLibraries.contains(libToAdd)) {
                allLibraries.add(libToAdd);
                toAdd.addAll(libToAdd.getLibs());
            } else {
                throw new IllegalStateException(String.format("Library [%s] added twice... must be an error in naming?", libToAdd.getName()));
            }
        }
        return allLibraries;
    }
}
