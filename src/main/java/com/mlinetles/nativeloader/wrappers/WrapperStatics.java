package com.mlinetles.nativeloader.wrappers;

import javax.naming.OperationNotSupportedException;
import java.lang.ref.Cleaner;

public class WrapperStatics {
    private WrapperStatics() {
        throw new RuntimeException();
    }

    public static final Cleaner CLEANER = Cleaner.create();
}
