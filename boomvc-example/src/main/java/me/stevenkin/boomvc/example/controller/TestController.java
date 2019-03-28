package me.stevenkin.boomvc.example.controller;

import me.stevenkin.boomvc.mvc.annotation.GetRoute;
import me.stevenkin.boomvc.mvc.annotation.Path;
import me.stevenkin.boomvc.mvc.annotation.RestPath;
import me.stevenkin.boomvc.mvc.annotation.Restful;

@Path
public class TestController {

    @GetRoute("/hello")
    @Restful
    public String hello(){
        return "hello world";
    }

    @GetRoute("/")
    @Restful
    public String index(){
        return "hello world";
    }
}
