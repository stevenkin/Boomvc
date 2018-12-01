package me.stevenkin.boomvc.ioc.define;

import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.ioc.annotation.Autowired;
import me.stevenkin.boomvc.ioc.annotation.ConfigProperties;
import me.stevenkin.boomvc.ioc.annotation.Value;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BeanDefine {

    private String beanName;

    private Class<?> clazz;

    private Object object;

    private boolean isSingle;

    private List<Injector> injectors = new LinkedList<>();

    private Environment environment;

    public BeanDefine(Class<?> clazz) {
        this(clazz.getName(),clazz,true);
    }

    public BeanDefine(Class<?> clazz, boolean isSingle) {
        this(clazz.getName(),clazz,isSingle);
    }

    public BeanDefine(String beanName, Class<?> clazz, boolean isSingle) {
        this.beanName = beanName;
        this.clazz = clazz;
        this.isSingle = isSingle;
    }

    public BeanDefine(String beanName, Class<?> clazz, Object object, boolean isSingle) {
        this.beanName = beanName;
        this.clazz = clazz;
        this.object = object;
        this.isSingle = isSingle;
    }

    public BeanDefine(String beanName, Class<?> clazz, Object object, boolean isSingle, Environment environment) {
        this.beanName = beanName;
        this.clazz = clazz;
        this.object = object;
        this.isSingle = isSingle;
        this.environment = environment;
    }

    public String getBeanName() {
        return beanName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Object getObject() {
        return this.object;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void init(Ioc ioc){
        Field[] fields = this.clazz.getDeclaredFields();
        for(Field field : fields){
            if(field.getAnnotation(Autowired.class)!=null)
                this.injectors.add(new FieldInjector(ioc,field));
        }
        ConfigProperties configProperties = this.clazz.getAnnotation(ConfigProperties.class);
        if(configProperties!=null){
            for(Field field : fields) {
                if (field.getType().equals(String.class)) {
                    String name = field.getAnnotation(Value.class) != null ? field.getAnnotation(Value.class).value() : field.getName();
                    String key = configProperties.prefix() + "." + name;
                    if(key.startsWith("."))
                        key = key.substring(1);
                    this.injectors.add(new ValueInjector(this.environment, field, key));
                }
            }
        }
        if(this.isSingle&&this.object==null){
            try {
                this.object = this.clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void inject(){
        this.injectors.forEach(i->i.inject(this.object));
    }

}
