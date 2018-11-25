package me.stevenkin.boomvc.mvc.filter;

import me.stevenkin.boomvc.common.dispatcher.MvcDispatcher;
import me.stevenkin.boomvc.common.filter.FilterChain;
import me.stevenkin.boomvc.http.HttpRequest;

public interface FilterMapping {

    void registerFilter(FilterRegisterBean filter);

    void registerDispatcher(MvcDispatcher dispatcher);

    FilterChain mappingFilters(HttpRequest request);

    void distory();

}
