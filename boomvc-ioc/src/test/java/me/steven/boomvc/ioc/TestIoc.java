package me.steven.boomvc.ioc;

import me.steven.test.Config1;
import me.steven.test.Config2;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.ioc.IocFactory;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by wjg on 2017/12/29.
 */
public class TestIoc {

    @Test
    public void test(){
        Ioc ioc = IocFactory.buildIoc(Arrays.asList("me.steven.test"),"app1.properties","app2.properties");
        ioc.getNames().forEach(System.out::println);
        ioc.getBeans().forEach(System.out::println);
        Config1 config1 = ioc.getBean(Config1.class);
        Config2 config2 = ioc.getBean(Config2.class);
        System.out.println("config1: "+config1.getKey1()+" "+config1.getKey2());
        System.out.println("config2: "+config2.getKey1()+" "+config2.getKey2());
    }

}
