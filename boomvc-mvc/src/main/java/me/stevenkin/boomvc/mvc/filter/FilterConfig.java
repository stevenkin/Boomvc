package me.stevenkin.boomvc.mvc.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FilterConfig {

    private String filterName;

    private Map<String, String> initParameter = new HashMap<>();

    private String filterPathPattern;

    private int order = Integer.MAX_VALUE;

    public FilterConfig() {
    }

    public FilterConfig filterPathPattern(String filterPathPattern) {
        this.filterPathPattern = filterPathPattern;
        return this;
    }

    public FilterConfig filterName(String filterName){
        this.filterName = filterName;
        return this;
    }

    public FilterConfig addInitParameter(String key, String value){
        this.initParameter.put(key, value);
        return this;
    }

    public String filterName(){
        return this.filterName;
    }

    public String filterPathPattern(){
        return this.filterPathPattern;
    }

    public Optional<String> initParameter(String key){
        return Optional.ofNullable(this.initParameter.get(key));
    }

    public Set<String> initParameterKeys(){
        return this.initParameter.keySet();
    }

    public FilterConfig order(int order) {
        this.order = order;
        return this;
    }

    public int order() {
        return order;
    }
}

