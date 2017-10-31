package me.stevenkin.boomvc.ioc;

import me.stevenkin.boomvc.ioc.annotation.Bean;
import me.stevenkin.boomvc.ioc.define.BeanDefine;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by wjg on 2017/10/30.
 */
public class SimpleIoc implements Ioc {

    private Map<String, BeanDefine> beanDefineMap = new ConcurrentHashMap<>();

    @Override
    public void addBean(Object bean) {
        Bean bean1 = bean.getClass().getAnnotation(Bean.class);
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
        Bean bean1 = type.getAnnotation(Bean.class);
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
        if(!beanDefine.getClazz().isAssignableFrom(type))
            throw new IllegalStateException("type is error");
        return beanDefine.getObject();
    }

    @Override
    public <T> T getBean(Class<T> type) {
        List<BeanDefine> classList = this.beanDefineMap.values().stream()
                .filter(b->b.getClazz().isAssignableFrom(type))
                .collect(Collectors.toList());
        if(classList.size()==0)
            return null;
        if(classList.size()==1)
            return type.cast(classList.get(0).getObject());
        throw new IllegalStateException("more than one class");
    }

    @Override
    public void setBean(Object bean) {
        Bean bean1 = bean.getClass().getAnnotation(Bean.class);
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
        this.beanDefineMap.values().forEach(b->b.init(SimpleIoc.this));
        this.beanDefineMap.values().forEach(b->b.inject());
    }

    @Override
    public void clear() {
        this.beanDefineMap.clear();
    }
}
