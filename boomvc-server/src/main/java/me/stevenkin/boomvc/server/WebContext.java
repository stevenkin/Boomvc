package me.stevenkin.boomvc.server;

public class WebContext {

    private static String contextPath;

    public static void init(String contextPath){
        WebContext.contextPath = contextPath;
    }

    public static String contextPath(){
        return WebContext.contextPath;
    }


}
