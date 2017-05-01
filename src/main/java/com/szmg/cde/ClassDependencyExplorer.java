package com.szmg.cde;

import com.szmg.cde.analyser.UnusedLibraryFinder;
import com.szmg.cde.config.InputConfig;
import com.szmg.cde.config.YamlConfigParser;
import com.szmg.cde.domain.Library;
import com.szmg.cde.reader.ExtraReferencedClassReader;
import com.szmg.cde.reader.LibraryReader;
import com.szmg.cde.reader.VeryNaiveSpringXmlReader;

import java.io.IOException;
import java.util.Set;

import static com.szmg.cde.reader.OnlyRootExtraReferencedClassReader.onlyForRoot;

public class ClassDependencyExplorer {

    public static void main(String args[]) throws IOException {
        System.out.println("Hello!");

        InputConfig inputConfig = new YamlConfigParser().parse("config.yaml");

        ExtraReferencedClassReader ercr = onlyForRoot(new VeryNaiveSpringXmlReader());
        Library library = new LibraryReader(inputConfig, ercr).readRecursively();

        Set<Library> unusedLibraries = new UnusedLibraryFinder().findUnusedLibraries(library);

        System.out.println("Unused libs:");
        unusedLibraries.stream().map(Library::getName).forEach(System.out::println);
    }

}
