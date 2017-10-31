package me.stevenkin.boomvc.ioc;

import me.stevenkin.boomvc.ioc.annotation.Bean;
import me.stevenkin.boomvc.ioc.scanner.ClassScanner;
import me.stevenkin.boomvc.ioc.scanner.CurrencyClassScanner;

import java.util.List;

/**
 * Created by wjg on 2017/10/30.
 */
public class IocContext {
    public static Ioc buildIoc(List<String> packageNames){
        Ioc ioc = null;
        ClassScanner scanner = new CurrencyClassScanner();
        packageNames.stream()
                .flatMap(p->scanner.scanClassByAnnotation(p, Bean.class))
                .forEach(c->ioc.addBean(c));
        return ioc;
    }
}
