package me.steven.test;

import me.stevenkin.boomvc.ioc.BeanProcessor;
import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.ioc.annotation.ConfigProperties;

@ConfigProperties(prefix = "me.stevenkin")
public class Config2 implements BeanProcessor{
    private String key1;

    private String key2;

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    @Override
    public void processor(Ioc ioc, Environment environment) {
        System.out.println("config2 beanprocessor");
    }
}
