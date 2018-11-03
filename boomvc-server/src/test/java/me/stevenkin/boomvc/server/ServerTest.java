package me.stevenkin.boomvc.server;



public class ServerTest {

    public static void main(String[] args){
        Boom.me().listen(9000)
                .start(ServerTest.class, args);
    }
}
