package com.prosperi.argeo;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8081);

        System.out.println("Server is running on port 8081");
    }
}