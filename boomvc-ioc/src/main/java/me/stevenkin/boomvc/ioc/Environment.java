package me.stevenkin.boomvc.ioc;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Environment {

    private static final String PREFIX_CLASSPATH = "classpath:";

    private static final String PREFIX_FILE = "file:";

    private static final String PREFIX_URL = "url:";

    private Map<String, String> props = new ConcurrentHashMap<>();

    private Environment(){

    }

    public static Environment empty(){
        return new Environment();
    }

    public static Environment of(Map<String, String> map){
        Environment environment = new Environment();
        environment.props.putAll(map);
        return environment;
    }

    public static Environment of(File file){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(inputStream!=null){
            return of(inputStream);
        }
        return new Environment();
    }

    public static Environment of(InputStream inputStream){
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            Environment environment = new Environment();
            properties.stringPropertyNames().stream().forEach(name->{
                environment.props.put(name,properties.getProperty(name));
            });
            return environment;
        } catch (IOException e) {
            e.printStackTrace();
            return new Environment();
        }
    }

    public static Environment of(URL url) {
        try {
            return of(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return new Environment();
        }
    }

    public static Environment of(String location) {
        if (location.startsWith(PREFIX_CLASSPATH)) {
            location = location.substring(PREFIX_CLASSPATH.length());
            return loadClasspath(location);
        } else if (location.startsWith(PREFIX_FILE)) {
            location = location.substring(PREFIX_FILE.length());
            return of(new File(location));
        } else if (location.startsWith(PREFIX_URL)) {
            location = location.substring(PREFIX_URL.length());
            try {
                return of(new URL(location));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return new Environment();
            }
        } else {
            return loadClasspath(location);
        }
    }

    public static Environment of(String... locations){
        List<Environment> envs = new ArrayList<>();
        for(String location : locations){
            envs.add(of(location));
        }
        Environment environment = new Environment();
        envs.forEach(e->environment.putAll(e.props));
        return environment;
    }

    private static Environment loadClasspath(String classpath) {
        String path = classpath;
        if (classpath.startsWith("/")) {
            path = classpath.substring(1);
        }
        InputStream is = getDefault().getResourceAsStream(path);
        if (null == is) {
            return new Environment();
        }
        return of(is);
    }

    public static ClassLoader getDefault() {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (Exception ignored) {
        }
        if (loader == null) {
            loader = Environment.class.getClassLoader();
            if (loader == null) {
                try {
                    loader = ClassLoader.getSystemClassLoader();
                } catch (Exception e) {
                }
            }
        }
        return loader;
    }

    public void put(String key, String value){
        this.props.put(key, value);
    }

    public void putifAbsent(String key, String value){
        this.props.putIfAbsent(key, value);
    }

    public void putAll(Map map){
        this.props.putAll(map);
    }

    public String getValue(String key){
        return this.props.get(key);
    }

    public String getValue(String key, String defaultValue){
        return Optional.ofNullable(getValue(key)).orElse(defaultValue);
    }

    public Set<String> getKeys(){
        return this.props.keySet();
    }

    public boolean isEmpty(){
        return this.props.isEmpty();
    }

    public Map<String, String> toMap(){
        return new HashMap<>(this.props);
    }




}
