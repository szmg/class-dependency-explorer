package com.szmg.cde.analyser;

import com.szmg.cde.domain.Clazz;
import com.szmg.cde.domain.Library;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UnusedLibraryFinder {

    private static Logger LOGGER = Logger.getLogger(UnusedLibraryFinder.class.getName());

    public Set<Library> findUnusedLibraries(Library rootLibrary) {
        ClassNameIndex classNameIndex = new ClassNameIndex(rootLibrary);

        // This should contain all libs; are there nested ones?
        Set<Library> unusedLibs = new HashSet<>(rootLibrary.getLibs());

        Deque<Clazz> usedClasses = getKnownToBeUsedClasses(rootLibrary, classNameIndex);

        Set<String> alreadyScheduledClasses = usedClasses.stream()
                .map(Clazz::getFullyQualifiedName)
                .collect(Collectors.toSet());

        while (!(usedClasses.isEmpty() || unusedLibs.isEmpty())) {
            Clazz c = usedClasses.pop();
            unusedLibs.remove(c.getLibrary());

            for (String referencedClassName : c.getReferencedClasses()) {
                if (alreadyScheduledClasses.contains(referencedClassName)) {
                    continue;
                }

                Optional<Clazz> referencedClass = classNameIndex.findByName(referencedClassName);
                if (referencedClass.isPresent()) {
                    usedClasses.push(referencedClass.get());
                    alreadyScheduledClasses.add(referencedClassName);
                } else {
                    LOGGER.warning(String.format("Class [%s] is referenced by class [%s], but cannot be found.",
                            referencedClassName, c.getFullyQualifiedName()));
                }
            }
        }

        return unusedLibs;
    }

    private Deque<Clazz> getKnownToBeUsedClasses(Library library, ClassNameIndex classNameIndex) {
        Deque<Clazz> usedClasses = new LinkedList<>(library.getOwnClasses());

        for (String className : library.getExtraReferencedClasses()) {
            Optional<Clazz> clazz = classNameIndex.findByName(className);
            if (clazz.isPresent()) {
                usedClasses.push(clazz.get());
            } else {
                LOGGER.warning(String.format("Class [%s] is referenced in lib [%s], but cannot be found.",
                        className, library.getName()));
            }
        }
        return usedClasses;
    }
}
