package me.stevenkin.boomvc.ioc.define;

import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.ioc.annotation.Autowired;

import java.lang.reflect.Field;

/**
 * Created by wjg on 2017/10/25.
 */
public class FieldInjector implements Injector {

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
            e.printStackTrace();
        }
    }
}
