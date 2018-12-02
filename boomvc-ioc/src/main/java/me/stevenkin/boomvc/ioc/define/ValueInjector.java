package me.stevenkin.boomvc.ioc.define;

import me.stevenkin.boomvc.ioc.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ValueInjector implements Injector {
    private static final Logger logger = LoggerFactory.getLogger(ValueInjector.class);

    private Environment environment;

    private Field field;

    private String key;

    public ValueInjector(Environment environment, Field field, String key) {
        this.environment = environment;
        this.field = field;
        this.key = key;
    }

    @Override
    public void inject(Object bean) {
        field.setAccessible(true);
        try {
            field.set(bean,environment.getValue(key));
        } catch (IllegalAccessException e) {
            logger.error("the field {} inject value {} happen error", field, environment.getValue(key));
        }
    }
}
