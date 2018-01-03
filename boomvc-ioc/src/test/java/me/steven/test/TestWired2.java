package me.steven.test;

import me.stevenkin.boomvc.ioc.annotation.Autowired;
import me.stevenkin.boomvc.ioc.annotation.Bean;

/**
 * Created by wjg on 2017/12/29.
 */
@Bean
public class TestWired2 {

    @Autowired
    private TestType testType;

    @Override
    public String toString() {
        return "TestWired2{}";
    }
}
