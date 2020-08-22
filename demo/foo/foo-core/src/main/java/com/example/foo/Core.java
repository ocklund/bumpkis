package com.example.foo;

import com.example.baz.BazLibrary;

public class Core {

    public boolean isCore() {
        return new BazLibrary().someLibraryMethod();
    }
}
