package me.stevenkin.boomvc.ioc.define;

import me.stevenkin.boomvc.ioc.Environment;

import java.lang.reflect.Field;

public class ValueInjector implements Injector {

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
            e.printStackTrace();
        }
    }
}
