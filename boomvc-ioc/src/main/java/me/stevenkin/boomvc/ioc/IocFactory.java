package me.stevenkin.boomvc.ioc;

import me.stevenkin.boomvc.ioc.annotation.Bean;
import me.stevenkin.boomvc.ioc.scanner.ClassScanner;
import me.stevenkin.boomvc.ioc.scanner.CurrencyClassScanner;

import java.util.List;

/**
 * Created by wjg on 2017/10/30.
 */
public class IocFactory {

    public static Ioc buildIoc(List<String> packageNames,  String... configLocations){
        Environment environment = Environment.of(configLocations);
        return buildIoc(packageNames, environment);
    }

    public static Ioc buildIoc(List<String> packageNames, Environment environment){
        Ioc ioc = new SimpleIoc();
        ClassScanner scanner = new CurrencyClassScanner();
        packageNames.stream()
                .flatMap(p->scanner.scanClassByAnnotation(p, Bean.class))
                .forEach(ioc::addBean);
        ioc.init(environment);
        return ioc;
    }
}
