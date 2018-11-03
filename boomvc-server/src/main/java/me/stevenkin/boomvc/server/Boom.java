package me.stevenkin.boomvc.server;

import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.ioc.IocFactory;
import me.stevenkin.boomvc.server.imp.TinyServer;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static me.stevenkin.boomvc.http.Const.*;

/**
 * Boomvc framework core operating class
 *
 *
 */

public class Boom {

    private List<String> packages = new ArrayList<>(Arrays.asList(DEFAULT_SCAN_PACKAGE));

    private List<String> statics = new ArrayList<>(DEFAULT_STATICS);

    private Ioc ioc;

    private Server server;

    private Environment environment = Environment.empty();

    private Class<?> bootClass;

    private String bannerText;

    private Boom() {
    }

    public static Boom me(){
        return of();
    }

    public static Boom of(){
        return new Boom();
    }

    public Ioc ioc(){
        return this.ioc;
    }

    public Environment environment() {
        return this.environment;
    }

    public Boom environment(String key, String value) {
        this.environment.put(key, value);
        return this;
    }

    public Boom register(Object bean){
        if(null == bean)
            throw new NullPointerException();
        this.ioc.addBean(bean);
        return this;
    }

    public Boom addStatics(String... statics){
        if(null == statics)
            throw new NullPointerException();
        this.statics.addAll(Arrays.asList(statics));
        String staticsStr = this.statics.stream().collect(Collectors.joining(","));
        this.environment.put(ENV_KEY_STATIC_DIRS,staticsStr);
        return this;
    }

    public Boom addPackages(String... packages){
        if(null == packages)
            throw new NullPointerException();
        this.packages.addAll(Arrays.asList(packages));
        String packagesStr = this.packages.stream().collect(Collectors.joining(","));
        this.environment.put(ENV_KEY_IOC_PACKAGES,packagesStr);
        return this;
    }

    public Boom showFileList(boolean fileList) {
        this.environment.put(ENV_KEY_STATIC_LIST, Boolean.toString(fileList));
        return this;
    }

    public Boom threadName(String threadName){
        this.environment.put(ENV_KEY_APP_THREAD_NAME, threadName);
        return this;
    }

    public Class<?> bootClass() {
        return this.bootClass;
    }

    public List<String> statics(){
        return this.statics;
    }

    public List<String> packages(){
        return this.packages;
    }

    public Boom listen(int port){
        if(port<1024)
            throw new IllegalStateException("server port is illegal");
        environment(ENV_KEY_SERVER_PORT, Integer.toString(port));
        return this;
    }

    public Boom listen(String address, int port) {
        if(null == address)
            throw new NullPointerException();
        environment(ENV_KEY_SERVER_ADDRESS, address);
        listen(port);
        return this;
    }

    public void start(Class<?> mainCls, String... args) {
        this.bootClass = mainCls;
        addPackages(bootClass.getPackage().getName());
        initEnv(args);
        initBanner();
        this.ioc = IocFactory.buildIoc(Arrays.asList(this.environment.getValue(ENV_KEY_IOC_PACKAGES).split(",")), this.environment);
        this.server = new TinyServer();
        this.server.init(this);
        this.server.start();
    }

    public Boom bannerPath(String bannerPath){
        this.environment.put(ENV_KEY_BANNER_PATH, bannerPath);
        return this;
    }

    private void initBanner(){
        String bannerPath = this.environment.getValue(ENV_KEY_BANNER_PATH, DEFAULT_BANNER_PATH);
        this.bannerText = BANNER_TEXT;
        URL url = Boom.class.getResource(bannerPath);
        if(null != url) {
            try {
                this.bannerText = Files.newBufferedReader(new File(url.getFile()).toPath()).lines().collect(Collectors.joining("/r/n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void initEnv(String... args){
        Environment environment1 = Environment.of(PROP_NAME);
        if(environment1.isEmpty())
            environment1 = Environment.of(PROP_NAME0);
        Map<String, String> map = environment1.toMap();
        if(!environment1.isEmpty()){
            environment1.getKeys().stream().forEach(key->
                this.environment.putifAbsent(key, map.get(key))
            );
        }
        Map<String, String> argsMap = parseArgs(args);
        if(!argsMap.isEmpty())
            argsMap.forEach((key, value)->
                    this.environment.put(key, value)
            );
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> argsMap = new HashMap<>();
        if (null == args || args.length == 0) {
            return argsMap;
        }
        for (String arg : args) {
            if (arg.startsWith("--") && arg.contains("=")) {
                String[] param = arg.substring(2).split("=");
                argsMap.put(param[0], param[1]);
            }
        }
        return argsMap;
    }










    }
