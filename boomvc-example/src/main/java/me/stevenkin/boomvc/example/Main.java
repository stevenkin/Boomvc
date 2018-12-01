package me.stevenkin.boomvc.example;

import me.stevenkin.boomvc.server.Boom;

public class Main {
    public static void main(String[] args) {
        Boom.of().start(Main.class, args);
    }
}
