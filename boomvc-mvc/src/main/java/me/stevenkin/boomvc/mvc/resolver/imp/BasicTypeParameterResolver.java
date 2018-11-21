package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.http.HttpQueryParameter;
import me.stevenkin.boomvc.mvc.exception.ParameterResolverException;
import me.stevenkin.boomvc.mvc.kit.DateKit;
import me.stevenkin.boomvc.mvc.resolver.ParameterResolver;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

//TODO test
public abstract class BasicTypeParameterResolver implements ParameterResolver {

    public Object basicTypeParameterResolve(String value, Type type) throws Exception{
        if(value == null || "".equals(value)){
            if(int.class.equals(type) || Integer.class.equals(type))
                return new Integer(0);
            if(byte.class.equals(type) || Byte.class.equals(type))
                return new Byte((byte)0);
            if(short.class.equals(type) || Short.class.equals(type))
                return new Short((short)0);
            if(long.class.equals(type) || Long.class.equals(type))
                return new Long(0);
            if(double.class.equals(type) || Double.class.equals(type))
                return new Double(0);
            if(float.class.equals(type) || Float.class.equals(type))
                return new Float(0);
            if(char.class.equals(type) || Character.class.equals(type))
                return '0';
            if(BigDecimal.class.equals(type))
                return new BigDecimal(0);
            if(Date.class.equals(type))
                return new Date(1970, 1, 1);
            if(LocalDate.class.equals(type))
                return LocalDate.of(1970, 1, 1);
            if(LocalDateTime.class.equals(type))
                return LocalDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.NOON);
            return "";
        }
        if(int.class.equals(type) || Integer.class.equals(type))
            return Integer.parseInt(value);
        if(byte.class.equals(type) || Byte.class.equals(type))
            return Byte.parseByte(value);
        if(short.class.equals(type) || Short.class.equals(type))
            return Short.parseShort(value);
        if(long.class.equals(type) || Long.class.equals(type))
            return Long.parseLong(value);
        if(double.class.equals(type) || Double.class.equals(type))
            return Double.parseDouble(value);
        if(float.class.equals(type) || Float.class.equals(type))
            return Float.parseFloat(value);
        if(char.class.equals(type) || Character.class.equals(type))
            if(value.length() == 1)
                return value.charAt(0);
            else
                throw new ParameterResolverException("string can not resolve to char");
        if(BigDecimal.class.equals(type))
            return new BigDecimal(value);
        if(Date.class.equals(type))
            return DateKit.toDate(value, "yyyy-MM-dd");
        if(LocalDate.class.equals(type))
            return DateKit.toLocalDate(value, "yyyy-MM-dd");
        if(LocalDateTime.class.equals(type))
            return DateKit.toLocalDateTime(value, "yyyy-MM-dd HH:mm:ss");
        return value;
    }

    public Object collectionTypeResolve(List<HttpQueryParameter> querys, Type type) throws Exception{
        Type elemType;
        if(type instanceof ParameterizedType) {
            elemType = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (elemType instanceof WildcardType) {
                if (((WildcardType) elemType).getLowerBounds() != null && ((WildcardType) elemType).getUpperBounds() == null) {
                    elemType = ((WildcardType) elemType).getLowerBounds()[0];
                } else {
                    throw new ParameterResolverException("the type [" + elemType + "] is wrong");
                }
            }
        }else if(((Class<?>)type).isArray()){
            elemType = ((Class<?>) type).getComponentType();
        }else
            throw new ParameterResolverException("the type [" + type + "] is wrong");
        Type elemtype1 = elemType;
        List<Object> values = querys.stream().map(p->{
            try {
                return basicTypeParameterResolve(p.value(), elemtype1);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(new ParameterResolverException("type resolve fail"));
            }
        }).collect(Collectors.toList());
        Class<?> rawType;
        if(type instanceof ParameterizedType)
            rawType = (Class<?>) ((ParameterizedType) type).getRawType();
        else
            //type is array
            rawType = (Class<?>) type;
        if(rawType.isArray())
            return values.toArray();
        if(List.class.isAssignableFrom(rawType)){
            Class<? extends List> implType = ArrayList.class;
            if(!Modifier.isAbstract(rawType.getModifiers()) )
                implType = (Class<? extends List>) rawType;
            List values1 = implType.newInstance();
            values1.addAll(values);
            return values1;
        }
        if(Set.class.isAssignableFrom(rawType)){
            Class<? extends Set> implType = HashSet.class;
            if(!Modifier.isAbstract(rawType.getModifiers()) )
                implType = (Class<? extends Set>) rawType;
            Set values1 = implType.newInstance();
            values1.addAll(values);
            return values1;
        }
        return values;
    }

}
