package me.stevenkin.boomvc.ioc;

import me.stevenkin.boomvc.ioc.annotation.Annotations;
import me.stevenkin.boomvc.ioc.annotation.Bean;
import me.stevenkin.boomvc.ioc.annotation.ConfigProperties;
import me.stevenkin.boomvc.ioc.define.BeanDefine;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by wjg on 2017/10/30.
 */
public class SimpleIoc implements Ioc {

    private Map<String, BeanDefine> beanDefineMap = new ConcurrentHashMap<>();

    @Override
    public void addBean(Object bean) {
        Bean bean1 = (Bean) Annotations.annotationOfType(bean.getClass(), Bean.class, new HashSet<>());
        if(bean1==null)
            throw new IllegalStateException("bean is error");
        this.addBean(bean1.value(),bean);
    }

    @Override
    public void addBean(String name, Object bean) {
        name = "".equals(name) ? bean.getClass().getName() : name;
        addBeanDefine(new BeanDefine(name,bean.getClass(),bean,true));
    }

    @Override
    public void addBean(Class<?> type) {
        Bean bean1 = (Bean) Annotations.annotationOfType(type, Bean.class, new HashSet<>());
        if(bean1==null)
            throw new IllegalStateException("bean is error");
        this.addBean(bean1.value(),type);
    }

    @Override
    public void addBean(String name, Class<?> type) {
        name = "".equals(name) ? type.getName() : name;
        addBeanDefine(new BeanDefine(name,type,true));
    }

    @Override
    public Object getBean(String name, Class<?> type) {
        BeanDefine beanDefine = this.beanDefineMap.get(name);
        if(beanDefine==null)
            throw new IllegalStateException("name is error");
        if(!type.isAssignableFrom(beanDefine.getClazz()))
            throw new IllegalStateException("type is error");
        return beanDefine.getObject();
    }

    @Override
    public <T> T getBean(Class<T> type) {
        List<T> beans = this.getBeans(type);
        if(beans.size()==1)
            return beans.get(0);
        if(beans.size()==0)
            throw new IllegalStateException("no found bean");
        throw new IllegalStateException("found more one beans");
    }

    @Override
    public <T> List<T> getBeans(Class<T> type) {
        List<T> beans = this.beanDefineMap.values().stream()
                .filter(b -> type.isAssignableFrom(b.getClazz()))
                .map(b->type.cast(b.getObject()))
                .collect(Collectors.toList());
        return beans;
    }

    @Override
    public void setBean(Object bean) {
        Bean bean1 = (Bean) Annotations.annotationOfType(bean.getClass(), Bean.class, new HashSet<>());
        if(bean1==null)
            throw new IllegalStateException("bean is error");
        this.setBean(bean1.value(),bean);
    }

    @Override
    public void setBean(String name, Object bean) {
        name = "".equals(name) ? bean.getClass().getName() : name;
        if(this.beanDefineMap.get(name)==null){
            throw new IllegalStateException();
        }
        this.beanDefineMap.get(name).setObject(bean);
    }

    @Override
    public Set<String> getNames() {
        return this.beanDefineMap.keySet();
    }

    @Override
    public List<BeanDefine> getBeanDefines() {
        return new ArrayList<>(this.beanDefineMap.values());
    }

    @Override
    public BeanDefine getBeanDefine(String name) {
        return this.beanDefineMap.get(name);
    }

    @Override
    public void addBeanDefine(BeanDefine beanDefine) {
        this.beanDefineMap.put(beanDefine.getBeanName(),beanDefine);
    }

    @Override
    public List<Object> getBeans() {
        return this.beanDefineMap.values().stream()
                .map(BeanDefine::getObject)
                .collect(Collectors.toList());
    }

    @Override
    public void remove(String name) {
        this.beanDefineMap.remove(name);
    }

    @Override
    public void init() {
        Set<String> locations = new HashSet<>();
        this.beanDefineMap.values().stream()
                .filter(b->b.getClazz().getAnnotation(ConfigProperties.class)!=null)
                .forEach(b->{
                    b.setEnvironment(Environment.of(b.getClazz().getAnnotation(ConfigProperties.class).locations()));
                });

        this.beanDefineMap.values().forEach(b->b.init(this));
        this.beanDefineMap.values().forEach(b->b.inject());
    }

    @Override
    public void clear() {
        this.beanDefineMap.clear();
    }
}
