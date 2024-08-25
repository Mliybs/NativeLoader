package com.mlinetles.nativeloader;

import com.sun.jna.*;
import net.fabricmc.loader.api.MappingResolver;

import java.util.*;

public class LoadedLibraries {
    private LoadedLibraries() {}

    public interface LoadedNativeLibrary extends Library {
        void Load(JNIEnv env, MappingResolver resolver);
        void Initialize();
    }

    private static final List<LoadedNativeLibrary> libraries = new ArrayList<>();

    public static final Map<String, Object> options = Collections.singletonMap(Library.OPTION_ALLOW_OBJECTS, Boolean.TRUE);

    public static void Load(String path, MappingResolver resolver) {
        var library = Native.load(path, LoadedNativeLibrary.class, options);
        libraries.add(library);
        library.Load(JNIEnv.CURRENT, resolver);
        try { library.Initialize(); }
        catch (UnsatisfiedLinkError ignored) {}
    }

    public static List<LoadedNativeLibrary> getLibraries() {
        return libraries;
    }
}
