package com.mlinetles.nativeloader.wrappers;

public class StringWrapper {
    private String string;

    public StringWrapper(String text) {
        string = text;
    }

    public String getString() {
        return string;
    }

    public void setString(String value) {
        string = value;
    }
}
