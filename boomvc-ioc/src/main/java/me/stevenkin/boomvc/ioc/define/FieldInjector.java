package me.stevenkin.boomvc.ioc.define;

import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.ioc.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by wjg on 2017/10/25.
 */
public class FieldInjector implements Injector {
    private static final Logger logger = LoggerFactory.getLogger(FieldInjector.class);

    private Ioc ioc;

    private Field field;

    public FieldInjector(Ioc ioc, Field field) {
        this.ioc = ioc;
        this.field = field;
    }

    @Override
    public void inject(Object bean) {
        String name = this.field.getAnnotation(Autowired.class).value();
        Class<?> fieldType = this.field.getType();
        Object value = null;
        if("".equals(name))
            value = this.ioc.getBean(fieldType);
        else
            value = this.ioc.getBean(name, fieldType);
        this.field.setAccessible(true);
        try {
            this.field.set(bean,value);
        } catch (IllegalAccessException e) {
            logger.error("the field {} inject value {} happen error", this.field, value);
        }
    }
}
