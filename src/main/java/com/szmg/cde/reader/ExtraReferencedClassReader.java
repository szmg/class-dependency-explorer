package com.szmg.cde.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface ExtraReferencedClassReader {

    Collection<String> readAll(String name, InputStream inputStream, String libName, boolean rootLib) throws IOException;

}
