package me.stevenkin.boomvc.mvc.filter;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.mvc.MvcDispatcher;

public interface FilterMapping {

    void registerFilter(FilterRegisterBean filter);

    void registerDispatcher(MvcDispatcher dispatcher);

    FilterChain mappingFilters(HttpRequest request);

    void distory();

}
