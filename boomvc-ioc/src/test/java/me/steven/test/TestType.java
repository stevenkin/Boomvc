package me.steven.test;

import me.stevenkin.boomvc.ioc.annotation.Autowired;
import me.stevenkin.boomvc.ioc.annotation.Bean;

/**
 * Created by wjg on 2017/12/29.
 */
@Bean
public class TestType {

    @Autowired
    private TestWired1 wired1;
    @Autowired
    private TestWired2 wired2;

    @Override
    public String toString() {
        return "TestType{" +
                "wired1=" + wired1 +
                ", wired2=" + wired2 +
                '}';
    }
}
