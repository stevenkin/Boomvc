package me.stevenkin.boomvc.mvc.kit;

import jdk.internal.org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ReflectKit {

    private static final Map<Method, String[]> METHOD_NAMES_POOL = new ConcurrentHashMap<>(64);

    public static String[] getMethodVariableNames(Method method) {
        if(METHOD_NAMES_POOL.containsKey(method))
            return METHOD_NAMES_POOL.get(method);

        Class<?> clazz = method.getDeclaringClass();
        String[] variableNames = getMethodParameterNamesByAsm4(clazz, method);
        METHOD_NAMES_POOL.put(method, variableNames);
        return variableNames;
    }

    public static boolean isBasicType(java.lang.reflect.Type type){
        return type.equals(String.class) || type.equals(Integer.class) ||
                type.equals(Long.class) || type.equals(Double.class) ||
                type.equals(Float.class) || type.equals(Short.class) ||
                type.equals(Boolean.class) || type.equals(Byte.class) ||
                type.equals(Character.class) || type.equals(int.class) ||
                type.equals(long.class) || type.equals(double.class) ||
                type.equals(float.class) || type.equals(short.class) ||
                type.equals(boolean.class) || type.equals(byte.class) ||
                type.equals(char.class) || type.equals(Date.class) ||
                type.equals(BigDecimal.class) || type.equals(LocalDate.class) ||
                type.equals(LocalDateTime.class);
    }

    public static boolean isCollectionType(java.lang.reflect.Type type){
        Class<?> clazz;
        if(type instanceof ParameterizedType){
            clazz = (Class<?>) ((ParameterizedType) type).getRawType();
        }else{
            clazz = (Class<?>) type;
        }
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);

    }





    /**
     * 获取指定类指定方法的参数名
     *
     * @param clazz 要获取参数名的方法所属的类
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表，如果没有参数，则返回null
     */
    private static String[] getMethodParameterNamesByAsm4(Class<?> clazz, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return null;
        }
        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final String[] parameterNames = new String[parameterTypes.length];

        String className = clazz.getName();
        try {
            ClassReader classReader = new ClassReader(className);
            classReader.accept(new ClassVisitor(Opcodes.ASM4) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
                        return null;
                    }
                    return new MethodVisitor(Opcodes.ASM4) {
                        @Override
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                            // 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
                            if (Modifier.isStatic(method.getModifiers()) && index < parameterNames.length) {
                                parameterNames[index] = name;
                            }
                            else if (!Modifier.isStatic(method.getModifiers()) && index > 0 && index <parameterNames.length + 1) {
                                parameterNames[index - 1] = name;
                            }
                        }
                    };

                }
            }, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parameterNames;
    }

}
