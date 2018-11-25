package me.stevenkin.boomvc.mvc.filter;

import me.stevenkin.boomvc.common.filter.Filter;
import me.stevenkin.boomvc.common.filter.FilterConfig;

public class FilterRegisterBean {

    private FilterConfig filterConfig;

    private Filter filter;

    public FilterRegisterBean() {
        this.filterConfig = new FilterConfig();
    }

    public FilterRegisterBean addFilter(Filter filter){
        this.filter = filter;
        return this;
    }

    public FilterRegisterBean addFilterPathPattern(String filterPathPattern){
        this.filterConfig.filterPathPattern(filterPathPattern);
        return this;
    }

    public FilterRegisterBean addFilterName(String filterName){
        this.filterConfig.filterName(filterName);
        return this;
    }

    public FilterRegisterBean addFilterInitParameter(String key, String value) {
        this.filterConfig.addInitParameter(key, value);
        return this;
    }

    public FilterConfig filterConfig() {
        return filterConfig;
    }

    public Filter filter() {
        if(this.filter == null)
            throw new NullPointerException();
        this.filter.init(this.filterConfig);
        return filter;
    }

    public FilterRegisterBean order(int order){
        this.filterConfig.order(order);
        return this;
    }
}
