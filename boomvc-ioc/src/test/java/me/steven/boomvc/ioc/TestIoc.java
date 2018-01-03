package me.steven.boomvc.ioc;

import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.ioc.IocContext;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by wjg on 2017/12/29.
 */
public class TestIoc {

    @Test
    public void test(){
        Ioc ioc = IocContext.buildIoc(Arrays.asList("me.steven.test"));
        ioc.getNames().forEach(System.out::println);
        ioc.getBeans().forEach(System.out::println);
    }

}
