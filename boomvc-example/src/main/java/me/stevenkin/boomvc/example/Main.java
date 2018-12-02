package me.stevenkin.boomvc.example;

import me.stevenkin.boomvc.server.Boom;

public class Main {
    public static void main(String[] args) {
        Boom.me().start(Main.class, args);
    }
}
