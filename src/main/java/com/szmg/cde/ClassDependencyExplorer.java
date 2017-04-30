package com.szmg.cde;

import com.szmg.cde.analyser.UnusedLibraryFinder;
import com.szmg.cde.domain.Library;
import com.szmg.cde.reader.LibraryReader;

import java.io.IOException;
import java.util.Set;

public class ClassDependencyExplorer {

    public static void main(String args[]) throws IOException {
        System.out.println("Hello!");

        InputConfig inputConfig = new InputConfig("/Users/mateszvoboda/gitexp/componentsvc/web/target/component-svc-web-58.0-SNAPSHOT.war");

        Library library = new LibraryReader(inputConfig).readRecursively();

        Set<Library> unusedLibraries = new UnusedLibraryFinder().findUnusedLibraries(library);

        System.out.println("Unused libs:");
        unusedLibraries.stream().map(Library::getName).forEach(System.out::println);
    }

}
