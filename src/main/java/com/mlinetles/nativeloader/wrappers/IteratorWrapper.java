package com.mlinetles.nativeloader.wrappers;

import com.mlinetles.nativeloader.LoadedLibraries;
import com.sun.jna.Function;
import com.sun.jna.Pointer;

import java.util.Iterator;

public class IteratorWrapper<T> implements Iterator<T> {
    private final Function _hasNext;

    private final Function _next;

    public IteratorWrapper(long hasNext, long next) {
        _hasNext = Function.getFunction(new Pointer(hasNext));
        _next = Function.getFunction(new Pointer(next));
    }

    public boolean hasNext() {
        return (Boolean)_hasNext.invoke(Boolean.class, null);
    }

    @SuppressWarnings("unchecked")
    public T next() {
        return (T)_next.invoke(Object.class, null, LoadedLibraries.options);
    }
}
