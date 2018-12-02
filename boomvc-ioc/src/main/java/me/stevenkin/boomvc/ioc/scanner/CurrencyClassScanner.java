package me.stevenkin.boomvc.ioc.scanner;

import me.stevenkin.boomvc.ioc.annotation.Annotations;
import me.stevenkin.boomvc.ioc.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * Created by wjg on 2017/10/28.
 */
public class CurrencyClassScanner implements ClassScanner {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyClassScanner.class);

    @Override
    public Stream<Class<?>> scanClass(String packageName, Class<?> superClass, Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = new HashSet<>();
        String packagePath = packageName.replace(".", "/");
        try {
            Enumeration<URL> urls = this.getClass().getClassLoader().getResources(packagePath);
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                boolean isJar = this.checkIsJar(url);
                if(isJar){
                    classSet.addAll(findJar(url,packagePath,superClass,annotationClass));
                }else{
                    classSet.addAll(findClassPath(packageName,url.getPath(),superClass,annotationClass));
                }
            }
            return classSet.stream();

        } catch (IOException e) {
            logger.error("some errors happened", e);
        }
        return null;
    }

    private boolean checkIsJar(URL url){
            String urlPath = url.toString();
            if(urlPath.startsWith("jar:file:")&&(urlPath.indexOf(".jar!")!= -1)){
                return true;
            }
            return false;
    }

    private Set<Class<?>> findJar(URL url, String packagePath, Class<?> superClass, Class<? extends Annotation> annotationClass){
        Set<Class<?>> classSet = new HashSet<>();
        try {
            JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration<JarEntry> entrys = jarFile.entries();
            while(entrys.hasMoreElements()){
                JarEntry entry = entrys.nextElement();
                String name = entry.getName();
                if(name.startsWith(packagePath)&&name.indexOf(".class")!=-1){
                    String classStr = name.substring(0,name.indexOf(".class")).replaceAll("/",".");
                    Class<?> clazz = Class.forName(classStr);
                    if(superClass!=null&&!superClass.isAssignableFrom(clazz))
                        continue;
                    if(annotationClass != null && Annotations.annotationOfType(clazz, Bean.class, new HashSet<>())==null)
                        continue;
                    if(Modifier.isAbstract(clazz.getModifiers()))
                        continue;
                    classSet.add(clazz);
                }
            }
            return classSet;
        } catch (IOException e) {
            logger.error("errors happened", e);
        } catch (ClassNotFoundException e) {
            logger.error("errors happened", e);
        }
        return new HashSet<>();
    }

    private Set<Class<?>> findClassPath(String packageName, String path, Class<?> superClass, Class<? extends Annotation> annotationClass){
        Set<Class<?>> classSet = new HashSet<>();
        File file = new File(path);
        if(!file.isDirectory())
            throw new IllegalStateException("path is error");
        File[] files = file.listFiles();
        for(File file1 : files) {
            if (file1.isFile() && file1.getName().endsWith(".class")) {
                String className = file1.getName().substring(0, file1.getName().indexOf(".class"));
                try {
                    Class<?> clazz = Class.forName(packageName + "." + className);
                    if (superClass != null && !superClass.isAssignableFrom(clazz))
                        continue;
                    if (annotationClass != null && Annotations.annotationOfType(clazz, Bean.class, new HashSet<>())==null)
                        continue;
                    if(Modifier.isAbstract(clazz.getModifiers()))
                        continue;
                    classSet.add(clazz);
                } catch (ClassNotFoundException e) {
                    logger.error("class can not be founded", e);
                }
            }
            if (file1.isDirectory()) {
                classSet.addAll(findClassPath(packageName + "." + file1.getName(), path + "/" + file1.getName(), superClass, annotationClass));
            }
        }
        return classSet;
    }

}
