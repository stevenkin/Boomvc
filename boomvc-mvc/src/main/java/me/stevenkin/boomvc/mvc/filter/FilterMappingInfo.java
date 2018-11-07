package me.stevenkin.boomvc.mvc.filter;


public class FilterMappingInfo {

    private Filter filter;

    private FilterConfig filterConfig;

    public FilterMappingInfo(FilterRegisterBean filterRegisterBean) {
        this.filter = filterRegisterBean.filter();
        this.filterConfig = filterRegisterBean.filterConfig();
    }

    public Filter filter() {
        return filter;
    }

    public FilterConfig filterConfig() {
        return filterConfig;
    }

}
