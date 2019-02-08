package me.stevenkin.boomvc.example;

import me.stevenkin.boomvc.common.filter.Filter;
import me.stevenkin.boomvc.common.interceptor.Interceptor;
import me.stevenkin.boomvc.example.filter.MyFilter;
import me.stevenkin.boomvc.example.interceptor.MyInterceptor;
import me.stevenkin.boomvc.mvc.WebMvcConfigurerAdapter;
import me.stevenkin.boomvc.mvc.WebMvcRegistry;
import me.stevenkin.boomvc.mvc.annotation.Configuration;

@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addInterceptors(WebMvcRegistry registry) {
        Interceptor interceptor = new MyInterceptor();
        registry.addInterceptor(interceptor)
                .order(1)
                .patternUrl("/*");
    }

    @Override
    public void addFilters(WebMvcRegistry registry) {
        Filter filter = new MyFilter();
        registry.addFilter(filter)
                .addFilterInitParameter("hello", "world")
                .addFilterInitParameter("ni", "hao")
                .addFilterPathPattern("/*")
                .order(1);
    }
}
