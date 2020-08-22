package com.example.foo;

import com.example.bar.Library;

public class App {

    public String getGreeting() {
        return "Computer says: " + new Library().someLibraryMethod();
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
