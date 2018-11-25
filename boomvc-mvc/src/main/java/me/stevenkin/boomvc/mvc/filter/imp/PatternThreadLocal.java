package me.stevenkin.boomvc.mvc.filter.imp;

import me.stevenkin.boomvc.common.kit.AntPathMatcher;
import me.stevenkin.boomvc.common.kit.PathMatcher;


public class PatternThreadLocal extends ThreadLocal<PathMatcher> {

    public PatternThreadLocal() {
    }

    @Override
    public PathMatcher initialValue(){
        return new AntPathMatcher();
    }
}
