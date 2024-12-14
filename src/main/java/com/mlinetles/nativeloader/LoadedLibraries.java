package com.mlinetles.nativeloader;

import com.sun.jna.*;
import net.fabricmc.loader.api.MappingResolver;
import org.slf4j.Logger;

import java.util.*;

public class LoadedLibraries {
    private LoadedLibraries() {}

    public interface LoadedNativeLibrary extends Library {
        void Load(JNIEnv env, MappingResolver resolver, Logger logger);
    }

    private static final Set<String> libraries = new HashSet<>();

    public static final Map<String, Object> options = Collections.singletonMap(Library.OPTION_ALLOW_OBJECTS, Boolean.TRUE);

    public static void Load(String identifier, String path, MappingResolver resolver, Logger logger) {
        var library = Native.load(path, LoadedNativeLibrary.class, options);
        libraries.add(identifier);
        library.Load(JNIEnv.CURRENT, resolver, logger);
    }

    public static Set<String> getLibraries() {
        return libraries;
    }
}
