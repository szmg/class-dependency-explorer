package com.szmg.cde.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

public class OnlyRootExtraReferencedClassReader implements ExtraReferencedClassReader {

    private ExtraReferencedClassReader delegate;

    public OnlyRootExtraReferencedClassReader(ExtraReferencedClassReader delegate) {
        this.delegate = delegate;
    }

    @Override
    public Collection<String> readAll(String name, InputStream inputStream, String libName, boolean rootLib) throws IOException {
        if (rootLib) {
            return delegate.readAll(name, inputStream, libName, rootLib);
        }
        return Collections.emptySet();
    }

    public static OnlyRootExtraReferencedClassReader onlyForRoot(ExtraReferencedClassReader delegate) {
        return new OnlyRootExtraReferencedClassReader(delegate);
    }
}
