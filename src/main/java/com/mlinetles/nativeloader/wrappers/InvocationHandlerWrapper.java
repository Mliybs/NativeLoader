package com.mlinetles.nativeloader.wrappers;

import com.mlinetles.nativeloader.LoadedLibraries;
import com.sun.jna.Function;
import com.sun.jna.Pointer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;

/**
 * 用于无返回值函数式接口的包装器
 */
public class InvocationHandlerWrapper implements InvocationHandler {
    private final Function function;

    private InvocationHandlerWrapper(long handle) {
        function = Function.getFunction(new Pointer(handle));
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        var name = method.getName();
        switch (name) {
            case "hashCode": return proxy.hashCode();
            case "equals": return proxy.equals(args[0]);
            case "toString": return proxy.toString();
        }
        var type = method.getReturnType();
        if (type == String.class) type = Object.class;
        return function.invoke(type, args, LoadedLibraries.options);
    }

    public static Object getProxyOf(Class<?> clazz, long handle) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{ clazz }, new InvocationHandlerWrapper(handle));
    }
}
