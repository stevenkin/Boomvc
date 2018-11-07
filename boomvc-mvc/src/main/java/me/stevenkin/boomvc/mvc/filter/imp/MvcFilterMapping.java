package me.stevenkin.boomvc.mvc.filter.imp;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.mvc.MvcDispatcher;
import me.stevenkin.boomvc.mvc.filter.FilterChain;
import me.stevenkin.boomvc.mvc.filter.FilterMapping;
import me.stevenkin.boomvc.mvc.filter.FilterMappingInfo;
import me.stevenkin.boomvc.mvc.filter.FilterRegisterBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MvcFilterMapping implements FilterMapping {

    private List<FilterMappingInfo> registeredFilters = new ArrayList<>();

    private MvcDispatcher dispatcher;

    private PatternThreadLocal patternThreadLocal = new PatternThreadLocal();

    @Override
    public void registerFilter(FilterRegisterBean filter) {
        this.registeredFilters.add(new FilterMappingInfo(filter));
    }

    @Override
    public void registerDispatcher(MvcDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public FilterChain mappingFilters(HttpRequest request) {
        List<FilterMappingInfo> mappedFilters = this.registeredFilters.stream()
                .filter(filter->patternThreadLocal.get().match(filter.filterConfig().filterPathPattern(), request.uri()))
                .sorted(Comparator.comparing((FilterMappingInfo f)->f.filterConfig().order()).reversed())
                .collect(Collectors.toList());
        FilterChain filterChain = new MvcFilterChain();
        mappedFilters.forEach(f->filterChain.addFilter(f.filter()));
        filterChain.dispatcher(this.dispatcher);
        return filterChain;
    }
}
