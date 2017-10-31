package me.stevenkin.boomvc.ioc.define;

import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.ioc.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjg on 2017/10/24.
 */
public class BeanDefine {

    private String beanName;

    private Class<?> clazz;

    private Object object;

    private boolean isSingle;

    private List<Injector> injectors = new ArrayList<>();

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

    public void init(Ioc ioc){
        Field[] fields = this.clazz.getDeclaredFields();
        for(Field field : fields){
            if(field.getAnnotationsByType(Autowired.class)!=null)
                this.injectors.add(new FieldInjector(ioc,field));
        }
        if(this.isSingle&&this.object==null){
            try {
                this.object = this.clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void inject(){
        this.injectors.forEach(i->i.inject(BeanDefine.this.object));
    }

}
