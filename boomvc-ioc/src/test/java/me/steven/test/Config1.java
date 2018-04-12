package me.steven.test;

import me.stevenkin.boomvc.ioc.annotation.ConfigProperties;

@ConfigProperties(prefix = "me.stevenkin", locations = {"classpath:app1.properties"})
public class Config1 {
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
}
