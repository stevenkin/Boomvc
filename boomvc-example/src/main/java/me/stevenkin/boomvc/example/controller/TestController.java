package me.stevenkin.boomvc.example.controller;

import me.stevenkin.boomvc.mvc.annotation.GetRoute;
import me.stevenkin.boomvc.mvc.annotation.RestPath;

@RestPath
public class TestController {

    @GetRoute("/hello")
    public String hello(){
        return "hello world";
    }
}
