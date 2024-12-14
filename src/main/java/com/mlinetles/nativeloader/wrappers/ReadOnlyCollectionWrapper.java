package com.mlinetles.nativeloader.wrappers;

import com.mlinetles.nativeloader.LoadedLibraries;
import com.sun.jna.Function;
import com.sun.jna.Pointer;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;
import java.util.Iterator;

public class ReadOnlyCollectionWrapper<T> extends AbstractCollection<T> {
    private final Function size;
    private final Function iterator;

    public ReadOnlyCollectionWrapper(long size, long iterator) {
        this.size = Function.getFunction(new Pointer(size));
        this.iterator = Function.getFunction(new Pointer(iterator));
    }

    @Override
    public int size() {
        return size.invokeInt(null);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return (Iterator<T>) iterator.invoke(Object.class,null, LoadedLibraries.options);
    }
}
