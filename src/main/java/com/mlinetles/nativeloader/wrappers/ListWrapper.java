package com.mlinetles.nativeloader.wrappers;

import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListWrapper<T> extends AbstractList<T> {
    private final ListStructure invoker;

    public ListWrapper(long handle) {
        invoker = new ListStructure(new Pointer(handle));
        invoker.read();
    }

    public static class ListStructure extends Structure {
        public ListStructure(Pointer pointer) {
            super(pointer);
        }

        public Pointer get;
        public Pointer size;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.stream(ListStructure.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        return (T)Function.getFunction(invoker.get).invokeObject(new Object[] { index });
    }

    @Override
    public int size() {
        return Function.getFunction(invoker.size).invokeInt(null);
    }
}
