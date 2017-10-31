package me.stevenkin.boomvc.ioc.scanner;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
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

    @Override
    public Stream<Class<?>> scanClass(String packageName, Class<?> superClass, Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = new HashSet<>();
        String packagePath = packageName.replaceAll(".", "/");
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
            e.printStackTrace();
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
                    if(superClass!=null&&!clazz.isAssignableFrom(superClass))
                        continue;
                    if(annotationClass!=null&&clazz.getAnnotation(annotationClass)==null)
                        continue;
                    classSet.add(clazz);
                }
            }
            return classSet;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
                    if (superClass != null && !clazz.isAssignableFrom(superClass))
                        continue;
                    if (annotationClass != null && clazz.getAnnotation(annotationClass) == null)
                        continue;
                    classSet.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (file1.isDirectory()) {
                classSet.addAll(findClassPath(packageName + "." + file1.getName(), path + "/" + file1.getName(), superClass, annotationClass));
            }
        }
        return classSet;
    }


}
