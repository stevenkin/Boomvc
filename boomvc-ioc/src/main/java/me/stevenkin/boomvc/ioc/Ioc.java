package me.stevenkin.boomvc.ioc;

import me.stevenkin.boomvc.ioc.define.BeanDefine;

import java.util.List;
import java.util.Set;

/**
 * Created by wjg on 2017/10/24.
 */
public interface Ioc {

    void addBean(Object bean);

    void addBean(String name, Object bean);

    void addBean(Class<?> type);

    void addBean(String name, Class<?> type);

    Object getBean(String name, Class<?> type);

    <T> T getBean(Class<T> type);

    <T> List<T> getBeans(Class<T> type);

    void setBean(Object bean);

    void setBean(String name, Object bean);

    Set<String> getNames();

    List<BeanDefine> getBeanDefines();

    BeanDefine getBeanDefine(String name);

    void addBeanDefine(BeanDefine beanDefine);

    List<Object> getBeans();

    void remove(String name);

    void init(Environment environment);

    void clear();
}
