package me.stevenkin.boomvc.mvc;

public interface WebMvcConfigurer {

    void addInterceptors(WebMvcRegistry registry);

    void addFilters(WebMvcRegistry registry);

}
