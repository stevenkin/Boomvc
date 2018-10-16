package me.stevenkin.boomvc.http;

public enum HttpMethod {
    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), HEAD("HEAD"), TRACE("TRACE"), OPTIONS("OPTIONS");

    private String text;

    HttpMethod(String text){
        this.text = text;
    }

    public String text(){
        return this.text;
    }
}
