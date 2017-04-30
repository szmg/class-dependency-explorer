package com.szmg.cde.reader;

import com.szmg.cde.InputConfig;
import com.szmg.cde.domain.Clazz;
import com.szmg.cde.domain.Library;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import sun.jvm.hotspot.runtime.ClassConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LibraryReader {

    // https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3
    private static final Pattern PRIMITIVE = Pattern.compile("\\[*[BCDFIJSZ]");
    private static final Logger LOGGER = Logger.getLogger(LibraryReader.class.getName());

    private InputConfig inputConfig;
    private long libCount;
    private long classCount;

    public LibraryReader(InputConfig inputConfig) {
        this.inputConfig = inputConfig;
    }

    // synchronized to get the stats right
    public synchronized Library readRecursively() throws IOException {
        LOGGER.info(String.format("Starting parsing [%s]", inputConfig.getPath()));
        libCount = 0;
        classCount = 0;
        long start = System.currentTimeMillis();
        try (InputStream inputStream = new FileInputStream(inputConfig.getPath())) {
            Library library = readLibrary(inputStream, getLibraryNameFromPath(inputConfig.getPath()));
            long duration = System.currentTimeMillis() - start;
            LOGGER.info(String.format("Finished parsing library. %d zip files and %d classes were parsed in %d ms.",
                    libCount, classCount, duration));
            return library;
        }
    }

    private Library readLibrary(InputStream inputStream, String libName) throws IOException {
        libCount++;
        Set<Library> libs = new HashSet<>();
        Set<Clazz> classes = new HashSet<>();

        Library library = new Library(libName, libs, classes, Collections.emptySet());

        ZipInputStream zip = new ZipInputStream(inputStream);
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            String name = entry.getName();
            if (name.endsWith(".class")) {
                classes.add(readClass(zip, name, library));
            } else if (name.endsWith(".jar")) {
                libs.add(readLibrary(zip, getLibraryNameFromPath(name)));
            }
        }

        return library;
    }

    private Clazz readClass(InputStream inputStream, String fileName, Library library) throws IOException {
        classCount++;
        JavaClass javaClass = new ClassParser(inputStream, fileName).parse();
        ConstantPool constantPool = javaClass.getConstantPool();

        Set<String> referencedClasses = new HashSet<>();
        for (Constant constant : constantPool.getConstantPool()) {
            if (constant != null && constant.getTag() == ClassConstants.JVM_CONSTANT_Class) {
                String className = constantPool.constantToString(constant);

                if (!PRIMITIVE.matcher(className).matches()) {
                    int i = className.indexOf("[L");
                    if (i > -1) {
                        // e.g., Integer[][][] --> "[[[Ljava.lang.Integer;"
                        className = className.substring(i + 2, className.length() - 1);
                    }

                    if (!isClassExcluded(className)) {
                        referencedClasses.add(className);
                    }
                }

            }
        }

        return new Clazz(javaClass.getClassName(), referencedClasses, library);
    }

    private boolean isClassExcluded(String fqn) {
        for (String packageToIgnore : inputConfig.getPackagesToIgnore()) {
            if (fqn.startsWith(packageToIgnore + ".")) {
                return true;
            }
        }
        return false;
    }

    private String getLibraryNameFromPath(String path) {
        int i = path.lastIndexOf(File.pathSeparator);
        return i < 0 ? path : path.substring(i);
    }

}
